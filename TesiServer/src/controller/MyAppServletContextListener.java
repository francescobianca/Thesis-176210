package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

import persistence.DatabaseManager;

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

	private ArrayList<String> googleSM = new ArrayList<>();

	public UpdateNews() {

	}

	@Override
	public void run() {
		
		while (true) {

			// Invocazione Lambda. Come input le sitemap.
			// Mi prendo con una query i GoogleSM
			googleSM = getGoogleSM();
			
			// Ora devo passare i GoogleSM alla lambda.
			for (String sitemap : googleSM) {
				
				InvokeRequest invokeRequest = new InvokeRequest().withFunctionName("DownloaderSitemap").withPayload("\""+sitemap+"\"");
				BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAJ3KWEZBBFDQQKBBA",
						"waUKerZklqDVjBv+XpUq98HJpjGI2KQ+DGY4EJKP");

				AWSLambda awsLambda = AWSLambdaClientBuilder.standard().withRegion(Regions.EU_WEST_2)
						.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();

				InvokeResult invokeResult = null;

				try {
					invokeResult = awsLambda.invoke(invokeRequest);
				} catch (Exception e) {
					System.out.println(e);
				}

				System.out.println(invokeResult.getStatusCode());
				
			}
			
			// Fase di aggiornamento delle news

			try {
				sleep(120000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	private ArrayList<String> getGoogleSM() {

		Connection connection = DatabaseManager.getInstance().getDaoFactory().getDataSource().getConnection();
		ResultSet setGoogleSM = null;

		try {
			String query = " select googlesm from sito";

			PreparedStatement statement = connection.prepareStatement(query);
			setGoogleSM = statement.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ArrayList<String> localGoogleSM = new ArrayList<>();

		try {

			while (setGoogleSM.next()) {
				String s = setGoogleSM.getString("googlesm");
				localGoogleSM.add(s);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return localGoogleSM;
	}

}