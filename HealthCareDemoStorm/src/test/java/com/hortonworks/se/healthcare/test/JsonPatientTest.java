package com.hortonworks.se.healthcare.test;

import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.hortonworks.se.healthcare.storm.PatientScheme;
import com.hortonworks.se.healthcare.utils.ParseUtils;

public class JsonPatientTest {

	public static void main(String[] args) {

		ParseUtils.parseJson(generateSampleJson(true/* hear_rate */, true/* blood_pressure */, true/*respitory*/, true/*oxygen*/));
		System.out.println("----------------------------------");
		ParseUtils.parseJson(generateSampleJson(true/* hear_rate */, false/* blood_pressure */, true/*respitory*/, true/*oxygen*/));
		System.out.println("----------------------------------");
		ParseUtils.parseJson(generateSampleJson(true/* hear_rate */, true/* blood_pressure */, false/*respitory*/, true/*oxygen*/));
		System.out.println("----------------------------------");
		ParseUtils.parseJson(generateSampleJson(true/* hear_rate */, true/* blood_pressure */, false/*respitory*/, false/*oxygen*/));
		System.out.println("----------------------------------");
		ParseUtils.parseJson(generateSampleJson(true/* hear_rate */, true/* blood_pressure */, true/*respitory*/, false/*oxygen*/));
		System.out.println("----------------------------------");


	}

	private static String generateSampleJson(boolean useHeartRate,
			boolean useBloodPressure, boolean useRespiratory, boolean useOxygen) {
		String patientId = "patientId";
		Double heart_rate = null;
		Double systolic_blood_pressure = null;
		Double diastolic_blood_pressure = null;
		Double respiratory_rate = null;
		Double oxygen_saturation = null;

		Random r = new Random();
		double heartRangeMin = 20.0;
		double heartRangeMax = 150.0;
		double randomValue = heartRangeMin + (heartRangeMax - heartRangeMin)
				* r.nextDouble();

		JSONObject obj = new JSONObject();
		obj.put(PatientScheme.FIELD_PATIENT_ID, "patiend" + r.nextInt());

		Timestamp effective_time_frame = new Timestamp(new Date().getTime());
		obj.put(PatientScheme.FIELD_EVENT_TIME, effective_time_frame.toString());
		if (useHeartRate) {
			obj.put(PatientScheme.FIELD_HEART_RATE, randomValue);
		}
		if (useBloodPressure) {
			randomValue = heartRangeMin + (heartRangeMax - heartRangeMin)
					* r.nextDouble();
			obj.put(PatientScheme.FIELD_SYSTOLIC_BLOOD_PRESSURE, randomValue);

			randomValue = heartRangeMin + (heartRangeMax - heartRangeMin)
					* r.nextDouble();
			obj.put(PatientScheme.FIELD_DIASTOLIC_BLOOD_PRESSURE, randomValue);
		}
		if(useRespiratory){
			randomValue = heartRangeMin + (heartRangeMax - heartRangeMin)
					* r.nextDouble();
			obj.put(PatientScheme.FIELD_RESPIRATORY_RATE, randomValue);
			
		}
		if (useOxygen){			
			randomValue = heartRangeMin + (heartRangeMax - heartRangeMin)
					* r.nextDouble();
			obj.put(PatientScheme.FIELD_OXYGEN_SATURATION, randomValue);
		}

		String jsonString = obj.toJSONString();
		System.out.println(jsonString);
		return jsonString;
	}
}
