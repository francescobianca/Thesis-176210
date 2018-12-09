package controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import model.News;
import model.Sito;
import persistence.DatabaseManager;
import persistence.PersistenceException;
import persistence.dao.NewsDao;
import persistence.dao.SitoDao;
import utility.ParserGoogleSM;

public class MyAppServletContextListener implements ServletContextListener {

	private Thread thread;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		System.out.println("Inizializzata");

		thread = new UpdateNews();
		thread.start();

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		System.out.println("Distrutta");
	}

}

class UpdateNews extends Thread {

	private List<Sito> siti = new ArrayList<>();
	private SitoDao sDao = DatabaseManager.getInstance().getDaoFactory().getSitoDAO();

	// Amazon Setup
	private BasicAWSCredentials awsCreds;
	private AWSLambda awsLambda;
	private AmazonS3 s3Client;

	public UpdateNews() {

		awsCreds = new BasicAWSCredentials("***AccessKey***", "***SecretKey***");

		awsLambda = AWSLambdaClientBuilder.standard().withRegion(Regions.EU_WEST_2)
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();

		s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();

	}

	@Override
	public void run() {

		while (true) {

			// Invocazione Lambda. Come input le sitemap.
			// Mi prendo con una query i GoogleSM
			siti = sDao.findAll();

			// Ora devo passare i GoogleSM alla lambda.
			for (Sito s : siti) {

				InvokeRequest invokeRequest = new InvokeRequest().withFunctionName("DownloaderSM")
						.withPayload("{ \"host\" : " + " \" " + s.getHost() + " \" , " + " \"googleSitemap\" : "
								+ " \" " + s.getGoogleSitemap() + " \" , " + " \"icon\" : " + " \"\" } ");

				InvokeResult invokeResult = null;

				try {
					invokeResult = awsLambda.invoke(invokeRequest);
				} catch (Exception e) {
					System.out.println(e);
				}

				System.out.println(invokeResult.getStatusCode());

			}

			// Fase di aggiornamento delle news
			ObjectListing listing = s3Client.listObjects("news-app-bucket");
			ArrayList<S3ObjectSummary> summary = (ArrayList<S3ObjectSummary>) listing.getObjectSummaries();

			for (S3ObjectSummary obj : summary) {
				
				S3Object s3object = s3Client.getObject(new GetObjectRequest("news-app-bucket", obj.getKey()));
				System.out.println(s3object.getKey());

				InputStream reader = new BufferedInputStream(s3object.getObjectContent());

				File file = new File("C:\\Users\\Francesco Bianca\\eclipse-workspace\\TestAppTesi\\Sitemap\\"+s3object.getKey()+".xml");
				
				System.out.println(file.getAbsolutePath());
				try {
					
					FileOutputStream fos = new FileOutputStream(file);
					byte[] buffer = new byte[1024];
					int len;
					while ((len = reader.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}
					fos.close();
					
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			
			// AggiornaDB
			refreshDB();
			

			try {
				sleep(120000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	
	public void refreshDB() {
		
		ParserGoogleSM parser = new ParserGoogleSM();
		
		File folder = new File("C:\\Users\\Francesco Bianca\\eclipse-workspace\\TestAppTesi\\Sitemap\\");
		File[] listOfFiles = folder.listFiles();
		
		NewsDao nDao = DatabaseManager.getInstance().getDaoFactory().getNewsDAO();

		for (File file : listOfFiles) {
			// Vado a parserizzare ogni singolo file.
		    if (file.isFile()) {
		        try {
					parser.parseSitemap(file);
					
					String siteName = FilenameUtils.removeExtension(file.getName());
					siteName = siteName.replaceAll("\\s+","");
					SitoDao sDao = DatabaseManager.getInstance().getDaoFactory().getSitoDAO();
					Sito current =sDao.findByPrimaryKey(siteName);
					
					for (int i=0; i<parser.sizeSM(); i++) {
						
						News news = new News();
						if (parser.getLocNews(i) != null) 
							news.setLoc(parser.getLocNews(i));
						if (parser.getFonteNews(i) != null)
							news.setFonte(parser.getFonteNews(i));
						if (parser.getLinguaNews(i) != null)
							news.setLingua(parser.getLinguaNews(i));
						if (parser.getTitoloNews(i) != null) 
							news.setTitolo(parser.getTitoloNews(i));
						if (parser.getDataNews(i) != null)
							news.setData(parser.getDataNews(i));
						if (parser.getKeywords(i) != null)
							news.setKeywords(parser.getKeywords(i));
						if (parser.getLocImmagine(i) != null)
							news.setLocImmagine(parser.getLocImmagine(i));
						
						
						news.setHostSito(current);
					
						// Provo a salvare la news.
						try {
							nDao.save(news);
							// Qua devo far vedere il cambiamento ai client.
							
						} catch (PersistenceException p) {
							// Qui ho delle news Duplicate e non le aggiungo.
						}
						
					}
					
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		        
		    }
		}
		
	}

}