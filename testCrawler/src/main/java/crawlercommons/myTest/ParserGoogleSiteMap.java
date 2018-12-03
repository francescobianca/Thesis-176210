package crawlercommons.myTest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import crawlercommons.sitemaps.AbstractSiteMap;
import crawlercommons.sitemaps.SiteMap;
import crawlercommons.sitemaps.SiteMapIndex;
import crawlercommons.sitemaps.SiteMapParser;
import crawlercommons.sitemaps.SiteMapURL;
import crawlercommons.sitemaps.UnknownFormatException;

public class ParserGoogleSiteMap {

	private static SiteMapParser parser = new SiteMapParser();

	public static void main(String[] args) throws UnknownFormatException, IOException {

		try {

			// Link di un SiteMapIndex per fare il test:
			// URL url = new URL("http://www.bbc.com/sitemap.xml");

			// Link di una Sitemap
			// URL url = new URL("https://www.bbc.com/sport/sitemap.xml");
			// URL url = new URL("https://tg24.sky.it/sitemap-https.speciali.xml");

			// Link google news Sitemap
			//URL url = new URL("https://tg24.sky.it/sitemap-https.gnews.xml");
			URL url = new URL("https://www.ilmessaggero.it/?sez=XML&p=MapNews");

			parse(url);

		} catch (MalformedURLException e) {
			// Url sottomesso non è corretto.
			e.printStackTrace();
		}

	}

	/** Recursive sitemap parsing method */
	private static void parse(URL url) throws UnknownFormatException, IOException {

		AbstractSiteMap sm = parser.parseSiteMap(url);

		if (sm.isIndex()) {

			// Sto lavorando con un SiteMapIndex.

			SiteMapIndex siteMapIndex = (SiteMapIndex) sm;
			// System.out.println(siteMapIndex.toString());

			Collection<AbstractSiteMap> links = siteMapIndex.getSitemaps();

			for (AbstractSiteMap asm : links) {
				parse(asm.getUrl());
			}

		} else {

			// Sto lavorando con una sitemap semplice.

			SiteMap siteMap = (SiteMap) sm;
			// System.out.println(siteMap.toString());

			Collection<SiteMapURL> links = siteMap.getSiteMapUrls();

			for (SiteMapURL smu : links) {
				
				// Parametri di google.

				System.out.println(smu.getNews().getName());
				System.out.println(smu.getNews().getLanguage());
				System.out.println(smu.getNews().getTitle());
				System.out.println(smu.getNews().getPublicationDate());
				
				for (int i=0; i<smu.getNews().getKeywords().length; i++) {
					if (i==smu.getNews().getKeywords().length-1)
						System.out.println(smu.getNews().getKeywords()[i]);
					else
						System.out.print(smu.getNews().getKeywords()[i]+",");
				}
				
				for (int i=0; i<smu.getImages().length;i++)
					System.out.println("Image loc: "+smu.getImages()[i].getLoc());
				
				
				System.out.println("---------------------------------");
			}

		}

	}

}
