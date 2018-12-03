package sitemaps.statistics;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import crawlercommons.sitemaps.SiteMap;
import crawlercommons.sitemaps.SiteMapURL;
import crawlercommons.sitemaps.UnknownFormatException;

public class TestDownloadKeyWord {
	
	public static void main(String[] args) throws UnknownFormatException, IOException {

		ClassificatoreSitemap cs = new ClassificatoreSitemap("https://www.komando.com/");
		ArrayList<String> sitemap = (ArrayList<String>) cs.getNoIndexSitemaps();

		for (String s : sitemap)
			if (cs.isDownloadSitemap(s)) {
				System.out.println(s);
				
				SiteMap sm = (SiteMap) cs.getSiteMapType(new URL(s));

				Collection<SiteMapURL> links = sm.getSiteMapUrls();
				for (SiteMapURL smu : links) {
					System.out.println("Link al download: "+smu.getUrl());
				}
				
			}
	
	}

}
