package sitemaps.statistics;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import crawlercommons.sitemaps.SiteMap;
import crawlercommons.sitemaps.SiteMapURL;
import crawlercommons.sitemaps.UnknownFormatException;

public class TestProductsKeyWord {
	
	public static void main(String[] args) throws UnknownFormatException, IOException {

		ClassificatoreSitemap cs = new ClassificatoreSitemap("http://www.smartneighbor.com/");
		ArrayList<String> sitemap = (ArrayList<String>) cs.getNoIndexSitemaps();

		for (String s : sitemap)
			if (cs.isProductSitemap(s)) {
				System.out.println(s);

				SiteMap sm = (SiteMap) cs.getSiteMapType(new URL(s));

				Collection<SiteMapURL> links = sm.getSiteMapUrls();
				for (SiteMapURL smu : links) {
					URL productURLsitemap = smu.getUrl();
					String filename = smu.getUrl().getFile();
					String[] parts = filename.split("/");
					String productName = null;
					if (parts.length>0)
						productName = parts[parts.length - 1]; // potrebbero esserci variazioni nello schema
					System.out.println("Prodotto: " + productName + " --> Link al prodotto: " + productURLsitemap.toString());
				}

			}

	}

}
