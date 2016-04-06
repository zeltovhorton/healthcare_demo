package com.hortonworks.se.healthcare.test;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.hortonworks.se.healthcare.producer.PatientJsonDataProducer;
import com.hortonworks.se.healthcare.storm.PatientScheme;
import com.hortonworks.se.healthcare.utils.ParseUtils;

public class PushResultsToNifiTest {

	public static void main(String[] args) {

		String generateJson = generateJson();
		System.out.println(generateJson);
		//createJsonResultForSepsis();

	}

	private static void createJsonResultForSepsis() {
		try{
	        	URL url = new URL("http://sandbox:8885/resultListener");
	    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    		conn.setDoOutput(true);
	    		conn.setRequestMethod("POST");
	    		conn.setRequestProperty("Content-Type", "application/json");
	            
	            
	            OutputStream os = conn.getOutputStream();

	            String jsonString = generateJson();
	    		
				os.write(jsonString.getBytes());
	    		os.flush();
	            
	            if (conn.getResponseCode() != 200) {
	    			throw new RuntimeException("Failed : HTTP error code : "
	    				+ conn.getResponseCode());
	    		}

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}

	private static String generateJson() {
		Random r = new Random();

		JSONObject obj = new JSONObject();
		obj.put(PatientScheme.FIELD_PATIENT_ID, "patiend" + r.nextInt());
		
		Timestamp effective_time_frame = new Timestamp(new Date().getTime());
		obj.put(PatientScheme.FIELD_EVENT_TIME, effective_time_frame.toString());

		obj.put("score", 2);

		String jsonString = obj.toJSONString();
		return jsonString;
	}
}
