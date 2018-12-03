package testAmazonS3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * author: Mark Watson
 */
public class ReadS3Bucket {
	
	static public void process(AmazonS3 s3, String bucketName, String prefix, int max) throws IOException {

		String key = "crawl-data/CC-MAIN-2018-43/robotstxt.paths.gz";

		ObjectListing list = s3.listObjects(bucketName, prefix);

		S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
		
		byte[] buffer = new byte[1024];
	    int n;
	    FileOutputStream fileOuputStream = new FileOutputStream("temp.gz");
	    BufferedInputStream bufferedInputStream = new BufferedInputStream( new GZIPInputStream(object.getObjectContent()));

	    GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOuputStream);
	    while ((n = bufferedInputStream.read(buffer)) != -1) {
	        gzipOutputStream.write(buffer);
	    }
	    gzipOutputStream.flush();
	    gzipOutputStream.close();

	}

	static public void main(String[] args) throws IOException {
		AmazonS3Client s3 = new AmazonS3Client();
		process(s3, "commoncrawl", "crawl-data/CC-MAIN-2018-43", 20);
	}

}