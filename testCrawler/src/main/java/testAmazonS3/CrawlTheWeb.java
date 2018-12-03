package testAmazonS3;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class CrawlTheWeb {

	final static String SAMPLE_CSV_FILE = "testCrawling.csv";
	final static String NEW_LINE_SEPARATOR = "\n";

	private static Long start, end;

	private static S3Object object;
	private static InputStream gzipStream;
	private static Reader decoder;
	private static BufferedReader buffered;

	private static ArrayList<String> sitemaps;
	
	static int count=0;

	static public void main(String[] args) throws IOException {

		@SuppressWarnings("deprecation")
		AmazonS3Client s3 = new AmazonS3Client();

		process(s3, "commoncrawl", "crawl-data/CC-MAIN-2018-43");

	}

	static public void process(AmazonS3 s3, String bucketName, String prefix) throws IOException {

		ArrayList<String> keys = new ArrayList<>();
		keys = readCSV(); // Prendo i file che devono essere poi scansionati

		// Utility per stampare l'output.
		CSVFormat csvFileFormat = CSVFormat.EXCEL.withRecordSeparator(NEW_LINE_SEPARATOR);
		FileWriter fileWriter = new FileWriter(SAMPLE_CSV_FILE);
		CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

		for (String key : keys) {

			start = System.currentTimeMillis();

			//csvFilePrinter.printRecord("File: " + key);

			object = s3.getObject(new GetObjectRequest(bucketName, key));

			gzipStream = new GZIPInputStream(object.getObjectContent());
			decoder = new InputStreamReader(gzipStream);
			buffered = new BufferedReader(decoder);

			sitemaps = new ArrayList<>();

			String line = buffered.readLine();

			while (line != null) {
				if (line.matches("Sitemap:.*")) {
					// System.out.println(line);
					line = line.trim();
					//csvFilePrinter.printRecord("--> " + line);
					sitemaps.add(line);
				}
				line = buffered.readLine();
			}

			buffered.close();
			
			//csvFilePrinter.print(sitemaps.size());

			csvFilePrinter.flush();
			
			count+=sitemaps.size();

			end = System.currentTimeMillis();

			System.out.println("Size delle sitemaps del file con chiave: " + key + " : " + sitemaps.size());
			System.out.println("   ---> Ha impiegato: " + ((end - start) / 1000) + " secondi");
			
			System.out.println(count);

		}

		csvFilePrinter.close();

	}

	public static ArrayList<String> readCSV() throws IOException {

		final String SAMPLE_CSV_FILE_PATH = "warcFile.csv";

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
