package crawlercommons.myTest;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import crawlercommons.robots.BaseRobotRules;
import crawlercommons.robots.SimpleRobotRules;
import crawlercommons.robots.SimpleRobotRules.RobotRulesMode;
import crawlercommons.robots.SimpleRobotRulesParser;

public class ParserRobotTXT {

	public static void main(String[] args) throws ClientProtocolException, IOException {

		HttpClient httpclient = HttpClientBuilder.create().build();
		
		// USER_AGENT - name(s) of crawler, to be used when processing file contents
		String USER_AGENT = "WhateverBot";
		String url = "http://www.sportmediaset.mediaset.it";
		URL urlObj = new URL(url);
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

				for (int i = 0; i < rules.getSitemaps().size(); i++)
					System.out.println(rules.getSitemaps().get(i)); // Link delle siteMap

			}
			robotsTxtRules.put(hostId, rules);
		}
		boolean urlAllowed = rules.isAllowed(url);
	}

}
