package sitemaps.statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.SocketException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.xml.sax.SAXException;

import crawlercommons.sitemaps.AbstractSiteMap;
import crawlercommons.sitemaps.SiteMapParser;
import crawlercommons.sitemaps.UnknownFormatException;

public class OutputFinaleCSV {

	public static void main(String[] args) {

		/*
		 * SitemapURL, Index(YES/No),Google YES/NO,fileNameLocale Non eseguire il tuo
		 * classificatore per il momento. Ci serve questa tabella come input. SALVATI il
		 * file sitemap quando lo scarichi, e metti nella colonna fileNameLocale come lo
		 * salvi (mantieni la struttura dell'url il piu' possibile, il nome del file
		 * deve essere una funzione dell'url per esempio sostituendo i caratteri non
		 * ammessi, tipo http://www.xx.com/sitemap.xml --> http-__www.xx.com_sitemap.xml
		 * 
		 */

		ArrayList<String> sitemaps = new ArrayList<>();

		SiteMapParser parserSitemap = new SiteMapParser();
		AbstractSiteMap sm = null;

		try {

			sitemaps = readCSV();

			// Printing csv
			final Object[] FILE_HEADER = { "SitemapURL", "Index(YES/NO)", "Google(YES/NO)", "FileNameLocale" };
			final String SAMPLE_CSV_FILE = "Output_Finale.csv";
			// Delimiter used in CSV file
			final String NEW_LINE_SEPARATOR = "\n";

			CSVFormat csvFileFormat = CSVFormat.EXCEL.withRecordSeparator(NEW_LINE_SEPARATOR);
			FileWriter fileWriter = null;
			CSVPrinter csvFilePrinter = null;

			// Dichiarazione del mio parser
			DomParserXML parser = new DomParserXML();

			String pathFolder = "xmlSitemap/";

			try {

				// initialize FileWriter object
				fileWriter = new FileWriter(SAMPLE_CSV_FILE);
				// initialize CSVPrinter object
				csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
				// Create CSV file header
				csvFilePrinter.printRecord(FILE_HEADER);

				int count = 0;

				for (String s : sitemaps) {

					// Stampa di utilità per vedere a che punto sono.
					System.out.println("Passo iterazione: " + count++);

					try {

						URL url = new URL(s);
						sm = parserSitemap.parseSiteMap(url);

						String filename;

						Pattern p = Pattern.compile(".+?(?=\\.)");
						Matcher m = p.matcher(url.getFile());
						if (m.find())
							filename = url.getHost() + m.group() + ".xml";
						else
							filename = url.getHost() + url.getFile() + ".xml";

						filename = filename.replace('/', '_');

						try {

							if (sm.isIndex()) {
								// è un indice e rispetta protocollo google.
								if (parser.isGoogleNewsSitemap(s, pathFolder+filename))
									csvFilePrinter.printRecord(s, "YES", "YES", filename);
								// è un indice ma non rispetta protocollo google.
								else
									csvFilePrinter.printRecord(s, "YES", "NO", filename);
							} else {
								// non è un indice e rispetta protocollo google.
								if (parser.isGoogleNewsSitemap(s, pathFolder+filename))
									csvFilePrinter.printRecord(s, "NO", "YES", filename);
								// non è un indice e non rispetta protocollo google.
								else
									csvFilePrinter.printRecord(s, "NO", "NO", filename);
							}

						} catch (SAXException e) {
							// Per ora nelle eccezioni non faccio nulla.
						}

					} catch (UnknownFormatException e) {
						// Per ora nelle eccezioni non faccio nulla.
					} catch (SocketException e) {
						// Per ora nelle eccezioni non faccio nulla.
					} catch (IOException e) {
						// Per ora nelle eccezioni non faccio nulla.
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

	public static ArrayList<String> readCSV() throws IOException {

		final String SAMPLE_CSV_FILE_PATH = "ListaSitemaps.csv";

		Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

		ArrayList<String> links = new ArrayList<String>();

		for (CSVRecord csvRecord : csvParser) {
			links.add(csvRecord.get(0));
		}

		csvParser.close();

		return links;

	}

}
