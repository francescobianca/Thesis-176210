package sitemaps.statistics;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import crawlercommons.sitemaps.SiteMap;
import crawlercommons.sitemaps.SiteMapURL;
import crawlercommons.sitemaps.UnknownFormatException;

public class TestSectionKeyWord {

	public static void main(String[] args) throws UnknownFormatException, IOException {

		ClassificatoreSitemap cs = new ClassificatoreSitemap("https://24sunnews.com/");
		ArrayList<String> sitemap = (ArrayList<String>) cs.getNoIndexSitemaps();

		for (String s : sitemap)
			if (cs.isSectionSitemap(s)) {
				System.out.println(s);

				SiteMap sm = (SiteMap) cs.getSiteMapType(new URL(s));

				Collection<SiteMapURL> links = sm.getSiteMapUrls();
				for (SiteMapURL smu : links) {
					URL tagSectionsitemap = smu.getUrl();
					String filename = smu.getUrl().getFile();
					String[] parts = filename.split("/");
					String sectionName = null;
					if (parts.length > 0)
						sectionName = parts[parts.length - 1]; // potrebbero esserci variazioni nello schema
					System.out.println("Section: " + sectionName + " --> Articoli legati alla sezione: "
							+ tagSectionsitemap.toString());
				}

			}

	}

}
