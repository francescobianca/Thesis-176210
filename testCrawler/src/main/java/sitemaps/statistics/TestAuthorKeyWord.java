package sitemaps.statistics;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import crawlercommons.sitemaps.SiteMap;
import crawlercommons.sitemaps.SiteMapURL;
import crawlercommons.sitemaps.UnknownFormatException;

public class TestAuthorKeyWord {

	public static void main(String[] args) throws UnknownFormatException, IOException {

		ClassificatoreSitemap cs = new ClassificatoreSitemap("https://24sunnews.com/");
		ArrayList<String> sitemap = (ArrayList<String>) cs.getNoIndexSitemaps();

		for (String s : sitemap)
			if (cs.isAuthorSitemap(s)) {
				System.out.println(s);
				SiteMap sm = (SiteMap) cs.getSiteMapType(new URL(s));

				Collection<SiteMapURL> links = sm.getSiteMapUrls();
				for (SiteMapURL smu : links) {
					URL authorURLsitemap = smu.getUrl();
					String filename = smu.getUrl().getFile();
					String[] parts = filename.split("/");
					String authorName = parts[parts.length-1];
					System.out.println("Autore: "+authorName+" --> Link ai suoi articoli: "+authorURLsitemap.toString());
				}
			}

	}

}
