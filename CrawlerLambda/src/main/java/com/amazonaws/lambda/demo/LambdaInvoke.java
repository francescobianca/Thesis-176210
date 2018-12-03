package com.amazonaws.lambda.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class LambdaInvoke {

	public static void main(String[] args) {

		ArrayList<String> sm = new ArrayList<>();

		try {
			sm = readSm("Lista.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//System.out.println(sm.size());
		
		int fetta = sm.size()/100;
		System.out.println(fetta);
		
		LambdaWorker[] worker = new LambdaWorker[100];
		for (int i=0; i<100; i++) {
			worker[i] = new LambdaWorker(i*4610, (i*4610)+4610 , sm);
			worker[i].start();
		}
		
	}
	
	public static ArrayList<String> readSm(String inputFile) throws IOException {

		FileReader f = new FileReader(inputFile);
		BufferedReader b = new BufferedReader(f);

		ArrayList<String> sm = new ArrayList<>();

		String s;
		s = b.readLine();
		while (s != null) {
			sm.add(s);
			s = b.readLine();
		}
		
		b.close();

		return sm;
		
	}
	
}