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
import com.hortonworks.se.healthcare.storm.SepsisRulesProcessorBolt;
import com.hortonworks.se.healthcare.utils.PatientUtils;

public class PushResultsToNifiTest {

	public static void main(String[] args) {

		//String generateJson = generateJson();
		//System.out.println(generateJson);
		
		createJsonResultForSepsis();

	}

	private static void createJsonResultForSepsis() {
		try{
	        	URL url = new URL("http://172.24.2.178:8885/resultListener");
	        	//URL url = new URL("http://172.24.2.178:8983/solr/healthcare/update/json");
	    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    		conn.setDoOutput(true);
	    		conn.setRequestMethod("POST");
	    		conn.setRequestProperty("Content-Type", "application/json");
	            
	            
	            OutputStream os = conn.getOutputStream();

	            String patientId = "alex";
	            
				String jsonString = generateJson(patientId);
	    		System.out.println(jsonString);
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

	private static String generateJson(String patientId ) {
		Random r = new Random();

		JSONObject obj = new JSONObject();
		
		obj.put(PatientScheme.FIELD_PATIENT_ID, patientId);
		
		Timestamp effective_time_frame = new Timestamp(new Date().getTime());
		obj.put(PatientScheme.FIELD_EVENT_TIME, effective_time_frame.toString());

		obj.put("score_s", SepsisRulesProcessorBolt.NORMAL);
		obj.put("id", patientId );
		obj.put("record_type_s", "patient_stat" );
		obj.put(PatientScheme.FIELD_PATIENT_ID, patientId );
			
		double heartRangeMin = 20.0;
		double heartRangeMax = 150.0;
		double randomValue = heartRangeMin + (heartRangeMax - heartRangeMin)
				* r.nextDouble();
		
		obj.put(PatientScheme.FIELD_HEART_RATE, randomValue);
			randomValue = heartRangeMin + (heartRangeMax - heartRangeMin)
					* r.nextDouble();
			obj.put(PatientScheme.FIELD_SYSTOLIC_BLOOD_PRESSURE, randomValue);
	
			randomValue = heartRangeMin + (heartRangeMax - heartRangeMin)
					* r.nextDouble();
			obj.put(PatientScheme.FIELD_DIASTOLIC_BLOOD_PRESSURE, randomValue);
			randomValue = heartRangeMin + (heartRangeMax - heartRangeMin)
					* r.nextDouble();
			obj.put(PatientScheme.FIELD_RESPIRATORY_RATE, randomValue);
			
			randomValue = heartRangeMin + (heartRangeMax - heartRangeMin)
					* r.nextDouble();
			obj.put(PatientScheme.FIELD_OXYGEN_SATURATION, randomValue);

		
		String jsonString = obj.toJSONString();
		return jsonString;
	}
}
