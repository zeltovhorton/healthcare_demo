package com.hortonworks.se.healthcare.test;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import org.json.simple.JSONArray;

import com.hortonworks.se.healthcare.producer.PatientJsonDataProducer;
import com.hortonworks.se.healthcare.utils.ParseUtils;

public class JsonPatientTest {

	public static void main(String[] args) {

		ParseUtils.parseJson(PatientJsonDataProducer.generateSampleJson(true/* hear_rate */, true/* blood_pressure */, true/*respitory*/, true/*oxygen*/));
		System.out.println("----------------------------------");
		ParseUtils.parseJson(PatientJsonDataProducer.generateSampleJson(true/* hear_rate */, false/* blood_pressure */, true/*respitory*/, true/*oxygen*/));
		System.out.println("----------------------------------");
		ParseUtils.parseJson(PatientJsonDataProducer.generateSampleJson(true/* hear_rate */, true/* blood_pressure */, false/*respitory*/, true/*oxygen*/));
		System.out.println("----------------------------------");
		ParseUtils.parseJson(PatientJsonDataProducer.generateSampleJson(true/* hear_rate */, true/* blood_pressure */, false/*respitory*/, false/*oxygen*/));
		System.out.println("----------------------------------");
		ParseUtils.parseJson(PatientJsonDataProducer.generateSampleJson(true/* hear_rate */, true/* blood_pressure */, true/*respitory*/, false/*oxygen*/));
		System.out.println("----------------------------------");

		
		  createJsonResultForSepsis();

	}

	private static void createJsonResultForSepsis() {
		try{
	        	URL url = new URL("http://localhost:8082/contentListener");
	    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    		conn.setDoOutput(true);
	    		conn.setRequestMethod("POST");
	    		conn.setRequestProperty("Content-Type", "application/json");
	            
	            
	            OutputStream os = conn.getOutputStream();
	    		String jsonResult = null;
				os.write(jsonResult.getBytes());
	    		os.flush();
	            
	            if (conn.getResponseCode() != 200) {
	    			throw new RuntimeException("Failed : HTTP error code : "
	    				+ conn.getResponseCode());
	    		}

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
}
