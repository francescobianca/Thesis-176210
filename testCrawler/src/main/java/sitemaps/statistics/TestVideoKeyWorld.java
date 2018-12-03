package sitemaps.statistics;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import crawlercommons.sitemaps.ImageAttributes;
import crawlercommons.sitemaps.SiteMap;
import crawlercommons.sitemaps.SiteMapURL;
import crawlercommons.sitemaps.UnknownFormatException;
import crawlercommons.sitemaps.VideoAttributes;

public class TestVideoKeyWorld {

	public static void main(String[] args) throws UnknownFormatException, IOException {

		ClassificatoreSitemap cs = new ClassificatoreSitemap("https://www.yogajournal.com/");
		ArrayList<String> sitemap = (ArrayList<String>) cs.getNoIndexSitemaps();

		// Mi trova le sitemap dei video ma mi da quasi sempre errore di parserizzazione
		// per accedere alle informazioni

		for (String s : sitemap)
			if (cs.isVideoSitemap(s)) {
				System.out.println(s);
				SiteMap sm = (SiteMap) cs.getSiteMapType(new URL(s));

				Collection<SiteMapURL> links = sm.getSiteMapUrls();

				for (SiteMapURL smu : links) {

					ImageAttributes[] ImageAttributes = smu.getImages();
					VideoAttributes[] VideoAttributes = smu.getVideos();

					if (ImageAttributes != null)
						System.out.println(ImageAttributes[ImageAttributes.length - 1].getTitle() + " --> "
								+ ImageAttributes[ImageAttributes.length - 1].getLoc());

					if (VideoAttributes != null)
						System.out.println(VideoAttributes[VideoAttributes.length - 1].getTitle() + " --> "
								+ VideoAttributes[VideoAttributes.length - 1].getPlayerLoc());

				}

			}

	}

}
