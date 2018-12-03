package sitemaps.statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class OutputClassifierGoogleSM_CSV {

	// Esempio per verificare quante sitemaps di quelle testate secondo il mio classificatore sono googleSM.
	public static void main(String[] args) throws IOException {
		
		final String SAMPLE_CSV_FILE_PATH = "test100.csv";

		ArrayList<String> googleSM = new ArrayList<String>();
		
		Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

		for (CSVRecord csvRecord : csvParser) {
			
			if ( csvRecord.get(2).equals("YES") )
				googleSM.add(csvRecord.get(0)+" --> "+csvRecord.get(1));
			
		}

		csvParser.close();

		final String SAMPLE_CSV_OUTPUT = "OutputClassificatore.csv";
		
		final Object[] FILE_HEADER = { "URL --> Google Sitemap" };
		final String NEW_LINE_SEPARATOR = "\n";
		CSVFormat csvFileFormat = CSVFormat.EXCEL.withRecordSeparator(NEW_LINE_SEPARATOR);
		
		FileWriter fileWriter = new FileWriter(SAMPLE_CSV_OUTPUT);;
		CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
		
		csvFilePrinter.printRecord(FILE_HEADER);
		
		for (String s : googleSM)
			csvFilePrinter.printRecord(s);

		fileWriter.flush();
		fileWriter.close();
		csvFilePrinter.close();
		
	}
	
}
