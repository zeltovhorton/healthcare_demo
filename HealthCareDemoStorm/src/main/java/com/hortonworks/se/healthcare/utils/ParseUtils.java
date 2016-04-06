package com.hortonworks.se.healthcare.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.hortonworks.se.healthcare.dto.Patient;
import com.hortonworks.se.healthcare.storm.PatientScheme;

public class ParseUtils {

	public static Patient parseJson(String jsonString) {
		JSONParser parser = new JSONParser();
		Patient patient = new Patient();
		try {
	
			Object obj = parser.parse(jsonString);
	
			JSONObject jsonObject = (JSONObject) obj;
	
			patient.patientId = cleanup((String) jsonObject
					.get(PatientScheme.FIELD_PATIENT_ID));
	
			Object objEventTime = jsonObject
					.get(PatientScheme.FIELD_EVENT_TIME);
			if (objEventTime != null) {
				patient.eventTime = (String) objEventTime;
			}
			Object objHeartRate = jsonObject
					.get(PatientScheme.FIELD_HEART_RATE);
			if (objHeartRate != null) {
				patient.heartRate = (Double) objHeartRate;
			}
			Object objRespitatoryRate = jsonObject
					.get(PatientScheme.FIELD_RESPIRATORY_RATE);
			if (objRespitatoryRate != null) {
				patient.respitatoryRate = (Double) objRespitatoryRate;
			}
	
			Object objOxygenRate = jsonObject
					.get(PatientScheme.FIELD_OXYGEN_SATURATION);
			if (objOxygenRate != null) {
				patient.oxygenRate = (Double) objOxygenRate;
			}
			Object objDiastolicBloodPressure = jsonObject
					.get(PatientScheme.FIELD_DIASTOLIC_BLOOD_PRESSURE);
			if (objDiastolicBloodPressure != null) {
				patient.diastolicBloodPressure = (Double) objDiastolicBloodPressure;
			}
			Object objSystolicBloodPressure = jsonObject
					.get(PatientScheme.FIELD_SYSTOLIC_BLOOD_PRESSURE);
			if (objSystolicBloodPressure != null) {
				patient.systolicBloodPressure = (Double) objSystolicBloodPressure;
			}
	
			// // loop array
			// JSONArray msg = (JSONArray) jsonObject.get("messages");
			// Iterator<String> iterator = msg.iterator();
			// while (iterator.hasNext()) {
			// System.out.println(iterator.next());
			// }
			System.out.println(patient);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return patient;
	}
	private static String cleanup(String str) {
		if (str != null) {
			return str.trim().replace("\n", "").replace("\t", "");
		} else {
			return str;
		}

	}

}
