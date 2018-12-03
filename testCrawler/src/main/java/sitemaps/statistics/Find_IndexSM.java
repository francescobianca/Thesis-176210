package sitemaps.statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.client.ClientProtocolException;

import crawlercommons.sitemaps.AbstractSiteMap;
import crawlercommons.sitemaps.SiteMapParser;
import crawlercommons.sitemaps.UnknownFormatException;

public class Find_IndexSM {

	static SiteMapParser parser = new SiteMapParser();
	
	public static void main(String[] args) {
		
		
		try {
			
			ArrayList<String> links = readCSV();
		
			printCSV(links);
				
		} catch (IOException e) {
			e.printStackTrace();
		} 

		
	}
	
	public static ArrayList<String> readCSV() throws IOException {

		final String SAMPLE_CSV_FILE_PATH = "ManyURL.csv";

		Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

		ArrayList<String> links = new ArrayList<String>();

		for (CSVRecord csvRecord : csvParser) {
			links.add(csvRecord.get(0));
		}

		csvParser.close();

		return links;

	}
	
	public static void printCSV(ArrayList<String> links) {
		
		final Object[] FILE_HEADER = { "IndexSitemap" };
		final String SAMPLE_CSV_FILE = "indexSM_ManyURL4.csv";
		// Delimiter used in CSV file
		final String NEW_LINE_SEPARATOR = "\n";

		CSVFormat csvFileFormat = CSVFormat.EXCEL.withRecordSeparator(NEW_LINE_SEPARATOR);
		FileWriter fileWriter = null;
		CSVPrinter csvFilePrinter = null;

		try {

			// initialize FileWriter object
			fileWriter = new FileWriter(SAMPLE_CSV_FILE);

			// initialize CSVPrinter object
			csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
			// Create CSV file header
			csvFilePrinter.printRecord(FILE_HEADER);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		AbstractSiteMap asm=null;
		
		for (String s : links) {
			
			ClassificatoreSitemap cs = new ClassificatoreSitemap(s);
			
			ArrayList<String> sm = new ArrayList<>();
			try {
				sm = (ArrayList<String>) cs.getSitemaps();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (sm!=null)
			for (String string : sm) {
				try {
					asm = parser.parseSiteMap(new URL(string));
				} catch (UnknownFormatException e) {
					break;
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (asm.isIndex())
					try {
						csvFilePrinter.printRecord(string);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				try {
					csvFilePrinter.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			
				
		}
		
		try {
			fileWriter.flush();
			fileWriter.close();
			csvFilePrinter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
