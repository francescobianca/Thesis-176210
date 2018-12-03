package sitemaps.statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DifferenceSites {

	public static void main(String[] args) throws IOException {
		
		ArrayList<String> allFiles = readFile("file1.txt");
		ArrayList<String> correctUrl = readFile("file2.txt");
		
		for (String i : correctUrl)
			for (String j : allFiles) {
				if (j.equals(i)) {
					allFiles.remove(i);
					break;
				}
			}
		
		FileWriter w = new FileWriter("sitiSenzaSM.txt");
		BufferedWriter b = new BufferedWriter(w);
		
		System.out.println(allFiles.size());
		
		for (String s: allFiles)
			b.write(s+"\n");
		
		b.flush();
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
