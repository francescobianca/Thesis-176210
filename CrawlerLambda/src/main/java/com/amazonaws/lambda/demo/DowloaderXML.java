package com.amazonaws.lambda.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;


public class DowloaderXML {

	private BasicAWSCredentials credenziali;
	private AmazonS3Client s3;

	public DowloaderXML() {

	}

	public void downloadFile(URL file) {

		String filename = file.getHost()+file.getFile();
		filename = filename.replace('/', '_');

		InputStream fis;
		try {
			fis = file.openStream();
			FileOutputStream fos = new FileOutputStream("/tmp/"+filename);
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
		
		File localFile = new File("/tmp/"+filename);
		
		credenziali = new BasicAWSCredentials("***AccessKey***", "***SecretKey***");
		s3 = new AmazonS3Client(credenziali);
		
		PutObjectRequest put = new PutObjectRequest("***bucket***", filename, localFile);
		s3.putObject(put);

	}

}