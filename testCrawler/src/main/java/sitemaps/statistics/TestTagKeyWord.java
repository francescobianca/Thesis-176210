package sitemaps.statistics;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import crawlercommons.sitemaps.SiteMap;
import crawlercommons.sitemaps.SiteMapURL;
import crawlercommons.sitemaps.UnknownFormatException;

public class TestTagKeyWord {

	public static void main(String[] args) throws UnknownFormatException, IOException {

		ClassificatoreSitemap cs = new ClassificatoreSitemap("https://martech.zone/");
		ArrayList<String> sitemap = (ArrayList<String>) cs.getNoIndexSitemaps();

		for (String s : sitemap)
			if (cs.isTagSitemap(s)) {
				System.out.println(s);
				SiteMap sm = (SiteMap) cs.getSiteMapType(new URL(s));

				Collection<SiteMapURL> links = sm.getSiteMapUrls();
				for (SiteMapURL smu : links) {
					URL tagURLsitemap = smu.getUrl();
					String filename = smu.getUrl().getFile();
					String[] parts = filename.split("/");
					String tagName = parts[parts.length-1]; // potrebbero esserci variazioni nello schema
					System.out.println("Tag: "+tagName+" --> Articoli legati al tag: "+tagURLsitemap.toString());
				}
			}

	}

}
