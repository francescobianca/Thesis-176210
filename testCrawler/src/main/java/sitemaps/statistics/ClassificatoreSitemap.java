package sitemaps.statistics;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import crawlercommons.robots.BaseRobotRules;
import crawlercommons.robots.SimpleRobotRules;
import crawlercommons.robots.SimpleRobotRulesParser;
import crawlercommons.robots.SimpleRobotRules.RobotRulesMode;
import crawlercommons.sitemaps.AbstractSiteMap;
import crawlercommons.sitemaps.SiteMapIndex;
import crawlercommons.sitemaps.SiteMapParser;
import crawlercommons.sitemaps.UnknownFormatException;

/*
 * Given a url try to understand if in the site there is a google sitemap
 */
public class ClassificatoreSitemap {

	private String site_url;
	
	public ClassificatoreSitemap() {

	}

	/*
	 * @param site_url
	 */
	public ClassificatoreSitemap(String site_url) {
		this.site_url = site_url;
	}

	/*
	 * @return the site_url
	 */
	public String getSite_url() {
		return site_url;
	}

	/*
	 * @param site_url to set
	 */
	public void setSite_url(String site_url) {
		this.site_url = site_url;
	}

	/*
	 * @return sitemaps or sitemapIndex url of the site_url
	 */
	public List<String> getSitemaps() throws ClientProtocolException, IOException {

		// Creando così un http client non ho più il problema dei cookie
		HttpClient httpclient = HttpClientBuilder.create()
				.setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build()).build();

		// USER_AGENT - name(s) of crawler, to be used when processing file contents
		String USER_AGENT = "TestBot";

		URL urlObj = new URL(site_url);

		// Utility per comporre l'hostID
		String hostId = urlObj.getProtocol() + "://" + urlObj.getHost()
				+ (urlObj.getPort() > -1 ? ":" + urlObj.getPort() : "");

		Map<String, BaseRobotRules> robotsTxtRules = new HashMap<String, BaseRobotRules>();
		BaseRobotRules rules = robotsTxtRules.get(hostId);

		if (rules == null) {
			HttpGet httpget = new HttpGet(hostId + "/robots.txt");
			HttpContext context = new BasicHttpContext();
			HttpResponse response = httpclient.execute(httpget, context);

			if (response.getStatusLine() != null && response.getStatusLine().getStatusCode() == 404) {

				// Entriamo mel caso in cui ci sia un errore.

				rules = new SimpleRobotRules(RobotRulesMode.ALLOW_ALL);
				// consume entity to deallocate connection
				EntityUtils.consumeQuietly(response.getEntity());
			} else {

				BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());
				SimpleRobotRulesParser robotParser = new SimpleRobotRulesParser();
				rules = robotParser.parseContent(hostId, IOUtils.toByteArray(entity.getContent()), "text/plain",
						USER_AGENT);

				return rules.getSitemaps();

			}
			robotsTxtRules.put(hostId, rules);
		}

		return null;

	}

	/*
	 * @return ALL sitemaps url (if there is a sitemap index it is parserized)
	 */
	public List<String> getNoIndexSitemaps() throws UnknownFormatException, IOException {

		List<String> sitemaps = getSitemaps();
		List<String> finalSitemap = new ArrayList<String>();

		if (sitemaps != null) // Il metodo getSitemaps ritorna qualcosa non null.

			for (String url : sitemaps) {
				/*
				 * Se non è un'estensione .xml non ci posso lavorare altrimenti la escludo. Al
				 * momento non ho stabilito cosa fare con altri formati perchè mi danno
				 * eccezioni ed errori.
				 */

				// Posso controllare l'estensione anche con FilenameUtils.getExtension("")
				// Per ora ometto questo controllo tanto eventuali errori vengono catturati.
				// if (!isGzExtension(url)) {

				URL sitemapURL = new URL(url);

				AbstractSiteMap sm = getSiteMapType(sitemapURL);

				if (sm != null)
					if (sm.isIndex()) {
						// Sto lavorando con un SiteMapIndex.

						SiteMapIndex siteMapIndex = (SiteMapIndex) sm;
						Collection<AbstractSiteMap> links = siteMapIndex.getSitemaps();

						for (AbstractSiteMap asm : links) {
							finalSitemap.add(asm.getUrl().toString());
						}

					} else {
						finalSitemap.add(url);
					}
				// } chiude l'if del controllo estensione.
			}

		return finalSitemap;

	}

	/*
	 * @return the possible google sitemap --> Metodo da rivedere.
	 */
	public List<String> googleSitemaps() throws UnknownFormatException, IOException {

		List<String> links = getNoIndexSitemaps();
		List<String> googleSiteMap = new ArrayList<String>();

		for (String url : links) {

			// Qui dentro devo applicare delle regole che mi permettono di trovare sitemap
			// google.
			url = url.toLowerCase();
			String regex = ".*news.*"; // Per ora provo con un semplice pattern
			if (url.matches(regex))
				googleSiteMap.add(url);

		}

		return googleSiteMap;
	}

	/*
	 * @return true if @param url is a googleSitemap.
	 */
	public boolean isGoogleSitemaps(String url) {
		
		URL modURL = null;
		
		try {
			modURL = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		url = modURL.getFile(); //Mi serve il filename in modo da evitare match con siti tipo breaking-news ecc.
		url = url.toLowerCase();

		String regex1 = ".*mapnews.*"; // Per ora provo con un semplice pattern
		String regex2 = ".*gnews.*";
		String regex3 = ".*_news.*";
		String regex4 = ".*-news.*";
		String regex5 = ".*sitemap\\/news.*";
		String regex6 = ".*sitemaps\\/news.*";
		String regex7 = ".*news-.*";
		String regex8 = ".*googlenews.*";

		ArrayList<String> regex = new ArrayList<String>();
		regex.add(regex1);
		regex.add(regex2);
		regex.add(regex3);
		regex.add(regex4);
		regex.add(regex5);
		regex.add(regex6);
		regex.add(regex7);
		regex.add(regex8);

		for (String s : regex)
			if (url.matches(s))
				return true;

		return false;
	}

	/*
	 * @return true if @param url is a sitemap of a possible list of author.
	 */
	public boolean isAuthorSitemap(String url) {

		url = url.toLowerCase();

		String regex = ".*author.*";

		if (url.matches(regex))
			return true;

		return false;
	}

	/*
	 * @return true if @param url is a sitemap of a possible list of tag.
	 */
	public boolean isTagSitemap(String url) {

		url = url.toLowerCase();

		String regex = ".*tag.*";

		if (url.matches(regex))
			return true;

		return false;
	}

	/*
	 * @return true if @param url is a sitemap of a possible list of video.
	 */
	public boolean isVideoSitemap(String url) {

		url = url.toLowerCase();

		String regex = ".*video.*";

		if (url.matches(regex))
			return true;

		return false;
	}

	/*
	 * @return true if @param url is a sitemap of a possible list of image.
	 */
	public boolean isImageSitemap(String url) {

		url = url.toLowerCase();

		String regex1 = ".*image.*";
		String regex2 = ".*picture.*";
		String regex3 = ".*gallery.*";
		String regex4 = ".*galleries.*";

		ArrayList<String> regex = new ArrayList<String>();

		regex.add(regex1);
		regex.add(regex2);
		regex.add(regex3);
		regex.add(regex4);

		for (String s : regex)
			if (url.matches(s))
				return true;

		return false;
	}

	/*
	 * @return true if @param url is a sitemap of a possible list of section.
	 */
	public boolean isSectionSitemap(String url) {

		url = url.toLowerCase();

		String regex1 = ".*section.*";
		String regex2 = ".*category.*";
		String regex3 = ".*categories.*";

		ArrayList<String> regex = new ArrayList<String>();

		regex.add(regex1);
		regex.add(regex2);
		regex.add(regex3);

		for (String s : regex)
			if (url.matches(s) && !url.matches(".*google.*")) // Modo per evitarmi archivi google che non mi interessano.
				return true;

		return false;

	}

	/*
	 * @return true if @param url is a sitemap of a possible list of download.
	 */
	public boolean isDownloadSitemap(String url) {

		url = url.toLowerCase();

		String regex = ".*download.*";

		if (url.matches(regex))
			return true;

		return false;
	
	}
	
	/*
	 * @return true if @param url is a sitemap of a possible list of post.
	 */
	public boolean isPostSitemap(String url) {

		url = url.toLowerCase();

		String regex = ".*post.*";

		if (url.matches(regex))
			return true;

		return false;
		
	}
	
	/*
	 * @return true if @param url is a sitemap of a possible list of products.
	 */
	public boolean isProductSitemap(String url) {

		url = url.toLowerCase();

		String regex1 = ".*product.*";
		String regex2 = ".*shop.*";

		ArrayList<String> regex = new ArrayList<String>();

		regex.add(regex1);
		regex.add(regex2);

		for (String s : regex)
			if (url.matches(s))
				return true;

		return false;

	}

	/*
	 * @return true if the @param url is a file with .gz extension
	 */
	@SuppressWarnings("unused")
	private boolean isGzExtension(String url) {

		String fileArray[] = url.split("\\.");
		String extension = fileArray[fileArray.length - 1];

		if (extension.equals("gz"))
			return true;

		return false;

	}

	/*
	 * @return true if the @param url is an xml file.
	 */
	@SuppressWarnings("unused")
	private boolean isXMLExtension(String url) {

		String fileArray[] = url.split("\\.");
		String extension = fileArray[fileArray.length - 1];

		if (extension.equals("xml"))
			return true;

		return false;

	}

	/*
	 * @return the AbstractSiteMap of the @param url
	 */
	public AbstractSiteMap getSiteMapType(URL url) throws UnknownFormatException, IOException {

		SiteMapParser parser = new SiteMapParser();

		AbstractSiteMap sm = null;

		sm = parser.parseSiteMap(url);

		return sm;

	}

}
