package sitemaps.statistics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class Tokenizer {

	public static void main(String[] args) throws IOException {

		FileReader f = new FileReader("sitemaps.txt");
		BufferedReader b = new BufferedReader(f);

		ArrayList<String> filenames = new ArrayList<String>();
		ArrayList<String> hosts = new ArrayList<>();

		// Struttura per tenermi le occorrenze di ogni termine.
		HashMap<String, Integer> occorrenze = new HashMap<String, Integer>();
		HashMap<String, ArrayList<String>> differentSite = new HashMap<String, ArrayList<String>>(); //Occorrenze per sito differente 

		String s;
		s = b.readLine();

		/*
		 * Creo un oggetto URL così facendo url.getFile() mi prendo solamente la parte
		 * di link che mi interessa. Attraverso funzioni di pattern matching mi
		 * seleziono solo la parte che mi interessa rimuovendo l'estensione non utile ai
		 * fini del calcolo
		 */

		while (s != null) {
			
			s = s.toLowerCase(); // per non avere differenze tra termini come SITEMAP - sitemap

			URL url = new URL(s);

			String filename;

			Pattern p = Pattern.compile(".+?(?=\\.)");
			Matcher m = p.matcher(url.getFile());
			
			if (m.find())
				filename = m.group();
			else
				filename = url.getFile();

			filenames.add(filename);
			hosts.add(url.getHost()); //Mi salvo anche l'host.
			
			s = b.readLine();
		}

		b.close();

		// Qui inizia la parte di tokenizer
		Pattern p = Pattern.compile("(\\w+[A-Za-z])"); // Qua si può migliorare la funzionalità di tokenizer.
			
		for (int j=0; j<filenames.size(); j++) {
			
			String currentHost = hosts.get(j);

			Matcher m = p.matcher(filenames.get(j));

			while (m.find()) {

				String keyword = m.group(1);
				String[] splitted = keyword.split("_"); //Devo eliminare gli underscore.

				for (int i = 0; i < splitted.length; i++) {
					Integer value = occorrenze.get(splitted[i]);
					if (value == null)
						value = 0;
					
					value++;
					
					occorrenze.put(splitted[i], value);
					
					ArrayList<String> array = differentSite.get(splitted[i]);
					if (array == null)
						array = new ArrayList<>();
					if (!array.contains(currentHost))
						array.add(currentHost);
					differentSite.put(splitted[i], array);
				}
			}
		}

		TreeMap<String, Integer> sortedMap = sortMapByValue(occorrenze);

		// Mi stampo l'output in un file csv.
		final String SAMPLE_CSV_OUTPUT = "TokenizerManySites.csv";
		final Object[] FILE_HEADER = { "KeyWord-->Occurences-->TotalSites-->Sites" };

		final String NEW_LINE_SEPARATOR = "\n";
		CSVFormat csvFileFormat = CSVFormat.EXCEL.withRecordSeparator(NEW_LINE_SEPARATOR);

		FileWriter fileWriter = new FileWriter(SAMPLE_CSV_OUTPUT);
		CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

		csvFilePrinter.printRecord(FILE_HEADER);
		
		for (Map.Entry<String, Integer> entry : sortedMap.entrySet())
			for (Map.Entry<String, ArrayList<String>> differentSites : differentSite.entrySet())
				if (entry.getKey().equals(differentSites.getKey())) {
					int sizeSiteList = differentSites.getValue().size();
					csvFilePrinter.printRecord(entry.getKey()+"-->"+entry.getValue()+"-->"+sizeSiteList+"-->"+differentSites.getValue());
				}
		fileWriter.flush();
		fileWriter.close();
		csvFilePrinter.close();

	}

	public static TreeMap<String, Integer> sortMapByValue(HashMap<String, Integer> map) {
		Comparator<String> comparator = new ValueComparator(map);
		// TreeMap is a map sorted by its keys.
		// The comparator is used to sort the TreeMap by keys.
		TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
		result.putAll(map);
		return result;
	}

}

//a comparator that compares Strings
class ValueComparator implements Comparator<String> {

	HashMap<String, Integer> map = new HashMap<String, Integer>();

	public ValueComparator(HashMap<String, Integer> map) {
		this.map.putAll(map);
	}

	@Override
	public int compare(String s1, String s2) {
		if (map.get(s1) >= map.get(s2)) {
			return -1;
		} else {
			return 1;
		}
	}
}
