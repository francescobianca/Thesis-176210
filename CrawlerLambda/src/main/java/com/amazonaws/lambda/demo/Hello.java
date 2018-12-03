package com.amazonaws.lambda.demo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

// Lambda Function to crawl sitemap and put them in an AmazonBucket
public class Hello implements RequestHandler<String, String> {

	@Override
	public String handleRequest(String input, Context context) {
		
		context.getLogger().log("Input: " + input);
		
		DowloaderXML downloader = new DowloaderXML();
		
		try {
			downloader.downloadFile(new URL(input));
		} catch (MalformedURLException e) {
			return "Error Malformed";
		} catch (IOException e) {
			return "Error IO";
		}
		
		return "OK";
		
	}

}
