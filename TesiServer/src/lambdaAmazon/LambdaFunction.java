package lambdaAmazon;

import java.net.MalformedURLException;
import java.net.URL;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaFunction implements RequestHandler<String, String> {

	@Override
	public String handleRequest(String input, Context context) {
		
		context.getLogger().log("Input: " + input);
		
		DownloaderSitemap downloader = new DownloaderSitemap();
		
		try {
			downloader.downloadFile(new URL(input));
		} catch (MalformedURLException e) {
			return "Error Malformed";
		}
		
		return "OK";
		
	}

}