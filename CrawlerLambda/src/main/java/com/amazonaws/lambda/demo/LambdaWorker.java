package com.amazonaws.lambda.demo;

import java.util.ArrayList;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

public class LambdaWorker extends Thread {

	private int startIndex, endIndex;
	private ArrayList<String> sm;

	public LambdaWorker(int startIndex, int endIndex, ArrayList<String> sm) {
		
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		
		this.sm = sm;
		
	}

	// Ogni thread si fa un pezzo della lista
	@Override
	public void run() {
		
		System.out.println("Sono il thread: "+getName()+" e sto partendo...");

		for (int i = startIndex; i < endIndex; i++) {

			InvokeRequest invokeRequest = new InvokeRequest().withFunctionName("MyFunction").withPayload(sm.get(i));

			BasicAWSCredentials awsCreds = new BasicAWSCredentials("***AccessKey***",
					"***SecretKey***");

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

	}

}
