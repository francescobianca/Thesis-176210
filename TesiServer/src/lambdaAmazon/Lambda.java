package lambdaAmazon;

import java.net.MalformedURLException;
import java.net.URL;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import model.Sito;

public class Lambda implements RequestHandler<Sito, String> {

	@Override
	public String handleRequest(Sito input, Context context) {
		context.getLogger().log("Input: " + input);

		DownloaderSitemap downloader = new DownloaderSitemap();

		try {
			downloader.downloadFile(new URL(input.getGoogleSitemap()),input.getHost());
		} catch (MalformedURLException e) {
			return "Error Malformed";
		}

		return "OK";

	}

}
