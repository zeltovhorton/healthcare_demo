package com.hortonworks.se.healthcare.utils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.hortonworks.se.healthcare.dto.Patient;
import com.hortonworks.se.healthcare.storm.PatientScheme;

public class PatientUtils {

	public static Patient parseJson(String jsonString) {
		JSONParser parser = new JSONParser();
		Patient patient = new Patient();
		try {

			Object obj = parser.parse(jsonString);

			JSONObject jsonObject = (JSONObject) obj;

			patient.patientId = cleanup((String) jsonObject
					.get(PatientScheme.FIELD_PATIENT_ID));

			// patient.eventTime = new Timestamp(new
			// Date().getTime()).toString();

			Object objHeartRate = jsonObject
					.get(PatientScheme.FIELD_HEART_RATE);
			if (objHeartRate != null ) {
				try
			    {
					patient.heartRate= Double.parseDouble(objHeartRate.toString());
			    }
			    catch(Exception nfe)
			    {
					patient.heartRate = -1.0;
			    }
			} else {
				patient.heartRate = -1.0;
			}
			Object objRespitatoryRate = jsonObject
					.get(PatientScheme.FIELD_RESPIRATORY_RATE);
			if (objRespitatoryRate != null) {
				try
			    {
					patient.respitatoryRate= Double.parseDouble(objRespitatoryRate.toString());
			    }
			    catch(Exception nfe)
			    {
					patient.respitatoryRate = -1.0;
			    }
			} else {
				patient.respitatoryRate = -1.0;
			}

			Object objOxygenRate = jsonObject
					.get(PatientScheme.FIELD_OXYGEN_SATURATION);
			if (objOxygenRate != null) {
				try
			    {
					patient.oxygenRate= Double.parseDouble(objOxygenRate.toString());
			    }
			    catch(Exception nfe)
			    {
					patient.oxygenRate = -1.0;
			    }
			} else {
				patient.oxygenRate = -1.0;
			}
			Object objDiastolicBloodPressure = jsonObject
					.get(PatientScheme.FIELD_DIASTOLIC_BLOOD_PRESSURE);
			if (objDiastolicBloodPressure != null) {
				try
			    {
					patient.diastolicBloodPressure= Double.parseDouble(objDiastolicBloodPressure.toString());
			    }
			    catch(Exception nfe)
			    {
					patient.diastolicBloodPressure = -1.0;
			    }
			} else {
				patient.diastolicBloodPressure = -1.0;
			}
			Object objSystolicBloodPressure = jsonObject
					.get(PatientScheme.FIELD_SYSTOLIC_BLOOD_PRESSURE);
			if (objSystolicBloodPressure != null) {
				try
			    {
					patient.systolicBloodPressure= Double.parseDouble(objSystolicBloodPressure.toString());
			    }
			    catch(Exception nfe)
			    {
					patient.systolicBloodPressure = -1.0;
			    }
			} else {
				patient.systolicBloodPressure = -1.0;
			}

			// // loop array
			// JSONArray msg = (JSONArray) jsonObject.get("messages");
			// Iterator<String> iterator = msg.iterator();
			// while (iterator.hasNext()) {
			// System.out.println(iterator.next());
			// }
			System.out.println("parseJson=" + patient);

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

	public static void createJsonResultForSepsis(Patient patient, String scoreResult) {
		try {
			URL url = new URL("http://sandbox:8885/resultListener");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStream os = conn.getOutputStream();
			
			JSONObject obj = new JSONObject();

			obj.put("id", patient.patientId );
			obj.put("record_type_s", "patient_status" );
			obj.put(PatientScheme.FIELD_PATIENT_ID, patient.patientId );
			obj.put(PatientScheme.FIELD_DIASTOLIC_BLOOD_PRESSURE, patient.diastolicBloodPressure );
			obj.put(PatientScheme.FIELD_SYSTOLIC_BLOOD_PRESSURE, patient.systolicBloodPressure );
			obj.put(PatientScheme.FIELD_HEART_RATE, patient.heartRate );
			obj.put(PatientScheme.FIELD_OXYGEN_SATURATION, patient.oxygenRate );
			obj.put(PatientScheme.FIELD_RESPIRATORY_RATE, patient.respitatoryRate );
			obj.put("score_s", scoreResult);

			Timestamp effective_time_frame = new Timestamp(new Date().getTime());
			obj.put(PatientScheme.FIELD_EVENT_TIME, effective_time_frame.toString());

			String jsonString = obj.toJSONString();
			System.out.println("jsonString=" + jsonString);

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

}
