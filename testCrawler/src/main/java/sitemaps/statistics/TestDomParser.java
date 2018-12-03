package sitemaps.statistics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.xml.sax.SAXException;

public class TestDomParser {

	public static void main(String[] args) {

		try {
			// Lettura sitemaps da file di testo
			ArrayList<String> sitemaps = readFile("fileDaTestare/Lista.txt");

			// Printing csv
			//final Object[] FILE_HEADER = { "Sitemap", "Google YES/NO", "Result Classifier" };
			final Object[] FILE_HEADER = { "Sitemap", "Google YES/NO"};
			
			
			final String SAMPLE_CSV_FILE = "fileDaTestare/testFinale1.csv";
			// Delimiter used in CSV file
			final String NEW_LINE_SEPARATOR = "\n";

			CSVFormat csvFileFormat = CSVFormat.EXCEL.withRecordSeparator(NEW_LINE_SEPARATOR);
			FileWriter fileWriter = null;
			CSVPrinter csvFilePrinter = null;

			// Dichiarazione del mio parser 
			DomParserXML parser = new DomParserXML();
			
			// Dichiarazione del mio classificatore
			//ClassificatoreSitemap cs = new ClassificatoreSitemap();

			int count = 0;

			try {

				// initialize FileWriter object
				fileWriter = new FileWriter(SAMPLE_CSV_FILE);
				// initialize CSVPrinter object
				csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
				// Create CSV file header
				csvFilePrinter.printRecord(FILE_HEADER);

				for (String s : sitemaps) {

					// Stampa di utilità per vedere a che punto sono.
					System.out.println(count++);

					try {

						if (parser.isGoogleNewsSitemap(s))
							csvFilePrinter.printRecord(s, "YES");
						else
							csvFilePrinter.printRecord(s, "NO");
						

					} catch (SAXException e) {
						
					}

					csvFilePrinter.flush();

				}

				csvFilePrinter.close();

			} catch (IOException e1) {
				e1.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<String> readFile(String file) throws IOException {

		FileReader f = new FileReader(file);
		BufferedReader b = new BufferedReader(f);

		ArrayList<String> files = new ArrayList<String>();

		String s;
		s = b.readLine();

		while (s != null) {

			files.add(s);

			s = b.readLine();
		}

		b.close();

		return files;

	}

}
