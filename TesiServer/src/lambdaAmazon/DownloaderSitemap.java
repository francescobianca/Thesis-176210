package lambdaAmazon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class DownloaderSitemap {

	private BasicAWSCredentials credenziali;
	private AmazonS3 s3;

	public DownloaderSitemap() {

	}

	public void downloadFile(URL file,String hostName) {
		
		System.out.println(file.toString());

		//String filename = file.toString();
		//filename = filename.replace('/', '_');

		InputStream fis;
		try {
			fis = file.openStream();
			FileOutputStream fos = new FileOutputStream("/tmp/" + hostName);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			// close resources
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File localFile = new File("/tmp/" + hostName);
		
		credenziali = new BasicAWSCredentials("AKIAJPBWMHBQ6Z3LOUBQ", "vpR48uncYawe7fZ9DFTly1Zhugdqk71vKHsGXf2l");
		s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credenziali)).build();
		
		// Nel bucket vado ad inserire le google sitemap
		PutObjectRequest put = new PutObjectRequest("news-app-bucket", hostName, localFile);
		s3.putObject(put);

	}

}