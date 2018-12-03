package sitemaps.statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class TestChangeOfGoogleSM {

	public static void main(String[] args) {
		
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;
		
		FileWriter w;
		BufferedWriter b;

		try {
			url = new URL("https://tg24.sky.it/sitemap-https.gnews.xml");
			is = url.openStream(); // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));
			
			w = new FileWriter("downloadGoogleNewsSM.txt");
			b = new BufferedWriter(w);

			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				b.write(line+"\n");
			}
			
			b.flush();
			
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
				// nothing to see here
			}
		}
		
		try {
			Thread.sleep(900000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Fase di confronto
		FileReader f;
		BufferedReader reader;
		
		try {
			f = new FileReader("downloadGoogleNewsSM.txt");
			reader = new BufferedReader(f);
			
			url = new URL("https://tg24.sky.it/sitemap-https.gnews.xml");
			is = url.openStream(); // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));
			
			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				if (!line.equals(reader.readLine()))
					System.out.println("Change");
			}
			
			
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
				// nothing to see here
			}
		}
		
	}

}
