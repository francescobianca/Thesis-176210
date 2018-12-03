package sitemaps.statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import crawlercommons.sitemaps.UnknownFormatException;

public class OutputClassifierCSV {

	public static void main(String[] args) {

		try {

			ArrayList<String> links = readCSV();

			Long start = System.currentTimeMillis();

			printCSV(links);

			Long end = System.currentTimeMillis();

			System.out.println("Tempo impiegato: " + (end - start));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<String> readCSV() throws IOException {

		final String SAMPLE_CSV_FILE_PATH = "URL_Gnews.csv";

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

		final Object[] FILE_HEADER = { "URL site", "Sitemap", "Google YES/NO", "Error" };
		final String SAMPLE_CSV_FILE = "testManyURL_GoogleNews.csv";
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

		ClassificatoreSitemap classificatore;

		for (String url : links) {

			classificatore = new ClassificatoreSitemap(url);
			List<String> sitemaps;
			try {
				sitemaps = classificatore.getNoIndexSitemaps();
				if (sitemaps != null) // Può essere che qualche file sia escluso per via di estensione o non abbia
										// sm.
					for (String s : sitemaps) {
						if (classificatore.isGoogleSitemaps(s)) {
							// System.out.println("L'url: " + s + " contiene una sitemap google");
							csvFilePrinter.printRecord(url, s, "YES", "/");
						} else {
							// System.out.println("L'url: " + s + " non contiene una sitemap google");
							csvFilePrinter.printRecord(url, s, "NO", "/");
						}
						csvFilePrinter.flush();
						// System.out.println("Sitemap: "+s);
					}

			} catch (UnknownFormatException e) {
				try {
					csvFilePrinter.printRecord(url, "/", "/", e.toString());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
				try {
					csvFilePrinter.printRecord(url, "/", "/", e.toString());
				} catch (IOException e1) {
					e1.printStackTrace();
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
