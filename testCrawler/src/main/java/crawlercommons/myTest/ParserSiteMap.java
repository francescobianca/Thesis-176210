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

public class ParserSiteMap {

	private static SiteMapParser parser = new SiteMapParser();

	public static void main(String[] args) throws UnknownFormatException, IOException {

		try {

			// Link di un SiteMapIndex per fare il test:
			// URL url = new URL("http://www.bbc.com/sitemap.xml");

			// Link di una Sitemap
			URL url = new URL("https://www.bbc.com/sport/sitemap.xml");


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
			//System.out.println(siteMapIndex.toString());

			Collection<AbstractSiteMap> links = siteMapIndex.getSitemaps();

			for (AbstractSiteMap asm : links) {
				parse(asm.getUrl());
			}

		} else {

			// Sto lavorando con una sitemap semplice.

			SiteMap siteMap = (SiteMap) sm;
			//System.out.println(siteMap.toString());

			Collection<SiteMapURL> links = siteMap.getSiteMapUrls();

			for (SiteMapURL smu : links) {
				
				System.out.println(smu.toString()); //Tutte le specifiche per ogni url. Ci posso accedere singolarmente.
				
			}

		}

	}

}
