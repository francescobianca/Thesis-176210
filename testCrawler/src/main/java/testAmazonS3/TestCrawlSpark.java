package testAmazonS3;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class TestCrawlSpark {
	
	// Utility per prendere il tempo.
	private static Long start, end;
	
	// Utility per la stampa in csv.
	final static String SAMPLE_CSV_FILE = "testSparkCrawling.csv";
	final static String NEW_LINE_SEPARATOR = "\n";

	// Pattern da rispettare nello scorrimento del file.
	private static Pattern p1 = Pattern.compile("WARC-Target-URI:\\s(.*)");
	private static Pattern p2 = Pattern.compile("Sitemap:\\s(.*)");
	private static Matcher matcher;

	// HashMap che contiene per ogni sito un elenco delle sue sitemaps.
	private static HashMap<String, List<String>> url_sitemaps = new HashMap<>();

	public static void main(String[] args) throws IOException {

		// Oggetto di configurazione di Apache Spark.
		SparkConf sparkConf = new SparkConf().setAppName("SOME APP NAME").setMaster("local")
				.set("spark.executor.memory", "1g");

		@SuppressWarnings("resource")
		JavaSparkContext sc = new JavaSparkContext(sparkConf);


		// Credenziali per utilizzo API AmazonAWS
		BasicAWSCredentials credenziali = new BasicAWSCredentials("***AccessKey***",
				"***secretKey***");

		@SuppressWarnings("deprecation")
		AmazonS3Client s3 = new AmazonS3Client(credenziali);

		// Il bucket che devo andare ad esaminare.
		ObjectListing listing = s3.listObjects("commoncrawl", "crawl-data/CC-MAIN-2018-43/");
		ArrayList<S3ObjectSummary> summary = (ArrayList<S3ObjectSummary>) listing.getObjectSummaries();

		JavaRDD<String> rdd = null;

		String accessKey = credenziali.getAWSAccessKeyId();
		String secretKey = credenziali.getAWSSecretKey();

		// Utility per stampare l'output.
		CSVFormat csvFileFormat = CSVFormat.EXCEL.withRecordSeparator(NEW_LINE_SEPARATOR);
		FileWriter fileWriter = new FileWriter(SAMPLE_CSV_FILE);
		CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

		Object[] FILE_HEADER = { "URL site", "Sitemap" };

		csvFilePrinter.printRecord(FILE_HEADER);

		// in automatico spark crea un albero contenente i file che mi interessano.
		for (S3ObjectSummary s : summary) {
			
			// Per ogni oggetto S3 mi cerco quello che contiene i robots.txt.
			if (s.getKey().matches(".*robotstxt.*")) {

				start = System.currentTimeMillis();
				
				// Salvo tutto in un JavaRDD --> utility di spark. Va con il download a cascata.
				rdd = sc.textFile("s3n://" + accessKey + ":" + secretKey + "@commoncrawl/" + s.getKey());

				for (String line : rdd.collect()) {

					line = line.trim();

					matcher = p1.matcher(line);

					if (matcher.find()) {
						URL site_url = new URL(matcher.group(1));
						String site_string = site_url.getProtocol() + "://" + site_url.getHost() + "/";

						// Una volta assemblato il sito vedo se ho già una chiave per quel sito.
						if (url_sitemaps.get(site_string) == null)
							url_sitemaps.put(site_string, new ArrayList<>());
					}

					matcher = p2.matcher(line);

					if (matcher.find()) {

						String sm = matcher.group(1);

						URL sm_url = null;

						try {
							sm_url = new URL(sm);
						} catch (MalformedURLException e) {

						}

						String site_string = null;

						if (sm_url != null) {
							site_string = sm_url.getProtocol() + "://" + sm_url.getHost() + "/";
							
							// Aggiungo i valori nell'hash delle sitemaps per un determinato sito.
							if (url_sitemaps.containsKey(site_string)) {
								url_sitemaps.get(site_string).add(sm);
							}

						}
					}

				}

				end = System.currentTimeMillis();

				// rdd.saveAsTextFile("spark_output/" + s.getKey() + ".txt");
				System.out.println("   ---> Ha impiegato: " + ((end - start) / 1000) + " secondi");

				// Utility per stampa e scorrimento dell'hash. --> Output finale un file csv.
				Set<String> keys = url_sitemaps.keySet();
				Iterator<String> iter = keys.iterator();

				while (iter.hasNext()) {

					String key = iter.next();
					// csvFilePrinter.printRecord(key);
					ArrayList<String> values = (ArrayList<String>) url_sitemaps.get(key);

					if (values.size() == 0)
						csvFilePrinter.printRecord(key, "NO_SITEMAPS");
					else
						for (String v : values)
							csvFilePrinter.printRecord(key, v);

				}

				csvFilePrinter.flush();

				url_sitemaps = new HashMap<>();
			}

		}

		csvFilePrinter.close();

	}

}
