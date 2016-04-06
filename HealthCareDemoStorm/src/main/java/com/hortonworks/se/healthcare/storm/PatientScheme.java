package com.hortonworks.se.healthcare.storm;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import com.hortonworks.se.healthcare.dto.Patient;
import com.hortonworks.se.healthcare.utils.ParseUtils;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class PatientScheme implements Scheme {

	public static final String FIELD_PATIENT_ID = "patient_id_s";
	public static final String FIELD_HEART_RATE = "heart_rate_f";
	public static final String FIELD_EVENT_TIME = "effective_time_frame_tdt";
	public static final String FIELD_SYSTOLIC_BLOOD_PRESSURE = "systolic_blood_pressure_f";
	public static final String FIELD_DIASTOLIC_BLOOD_PRESSURE = "diastolic_blood_pressure_f";
	public static final String FIELD_RESPIRATORY_RATE = "respiratory_rate_f";
	public static final String FIELD_OXYGEN_SATURATION = "oxygen_saturation_f";

	String patientId = "patientId";
	Double heart_rate = null;
	Double systolic_blood_pressure = null;
	Double diastolic_blood_pressure = null;
	Double respiratory_rate = null;
	Double oxygen_saturation = null;

	private static final long serialVersionUID = -2990121166902741545L;

	private static final Logger LOG = Logger.getLogger(PatientScheme.class);

	// Is the deserializer provided to the kafka spout to deserialize kafka byte
	// message stream to Values objects.
	@Override
	public List<Object> deserialize(byte[] bytes) {
		Patient patient = null;
		try {
			String json = new String(bytes, "UTF-8");
			patient = ParseUtils.parseJson(json);
		} catch (Exception e) {
			LOG.error(e);
		}

		return new Values(patient.patientId,patient.eventTime, patient.heartRate, patient.systolicBloodPressure,
				patient.diastolicBloodPressure, patient.respitatoryRate, patient.oxygenRate);

		// String[] pieces = patientEvent.split("\\|");
		//
		//
		// Timestamp eventTime = Timestamp.valueOf(pieces[0]);
		// String patientId = pieces[1];
		// String heart_rate = pieces[2];
		// String systolic_blood_pressure = pieces[3];
		// String diastolic_blood_pressure= pieces[4];
		// String respiratory_rate = pieces[5];
		// String oxygen_saturation = pieces[6];
		// return new Values(cleanup(heart_rate), cleanup(patientId),
		// eventTime, cleanup(systolic_blood_pressure),
		// cleanup(diastolic_blood_pressure), cleanup(respiratory_rate),
		// cleanup(oxygen_saturation));

	}

	@Override
	public Fields getOutputFields() {
		return new Fields(FIELD_PATIENT_ID,  FIELD_EVENT_TIME,FIELD_HEART_RATE,
				 FIELD_SYSTOLIC_BLOOD_PRESSURE,FIELD_DIASTOLIC_BLOOD_PRESSURE,
				FIELD_RESPIRATORY_RATE, FIELD_OXYGEN_SATURATION);

	}


}