package sitemaps.statistics;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import crawlercommons.sitemaps.ImageAttributes;
import crawlercommons.sitemaps.SiteMap;
import crawlercommons.sitemaps.SiteMapURL;
import crawlercommons.sitemaps.UnknownFormatException;

public class TestGalleryKeyWord {

	public static void main(String[] args) throws UnknownFormatException, IOException {

		ClassificatoreSitemap cs = new ClassificatoreSitemap("https://www.housebeautiful.com/");
		ArrayList<String> sitemap = (ArrayList<String>) cs.getNoIndexSitemaps();

		for (String s : sitemap)
			if (cs.isImageSitemap(s)) {
				System.out.println(s);

				SiteMap sm = (SiteMap) cs.getSiteMapType(new URL(s));

				Collection<SiteMapURL> links = sm.getSiteMapUrls();

				for (SiteMapURL smu : links) {
					ImageAttributes[] ImageAttributes = smu.getImages();
					if (ImageAttributes != null)
						System.out.println("Titolo immagine: " + ImageAttributes[ImageAttributes.length - 1].getTitle()
								+ " --> Loc: " + ImageAttributes[ImageAttributes.length - 1].getLoc());
					else 
						System.out.println("Loc: "+smu.getUrl());
				}

			}

	}

}
