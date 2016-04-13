package com.hortonworks.se.healthcare.test;

import java.io.FileNotFoundException;
import java.util.Iterator;

import org.json.simple.JSONArray;

import com.hortonworks.se.healthcare.dto.Patient;
import com.hortonworks.se.healthcare.producer.PatientJsonDataProducer;
import com.hortonworks.se.healthcare.storm.SepsisRulesProcessorBolt;
import com.hortonworks.se.healthcare.utils.PatientUtils;

public class SepsisLogicTest {

	public static void main(String[] args) {

		SepsisRulesProcessorBolt rulesProcessorBolt = new SepsisRulesProcessorBolt();

		Patient patient = PatientUtils.parseJson(PatientJsonDataProducer.generateSampleJson(
				true/* hear_rate */, true/* blood_pressure */,
				true/* respitory */, true/* oxygen */));
		patient = new Patient();
		patient.patientId="test";
		patient.heartRate=30.0;
		patient.respitatoryRate = 5.0;
		patient.systolicBloodPressure = 80.0;
		patient.oxygenRate = 80.0;
		System.out.println("result=" + rulesProcessorBolt.determineDyspnearesult(patient));
		
		System.out.println("----------------------------------");

		patient.patientId="test";
		patient.heartRate=50.0;
		patient.respitatoryRate = 14.0;
		patient.systolicBloodPressure = 80.0;
		patient.oxygenRate = 80.0;
		System.out.println("result=" + rulesProcessorBolt.determineDyspnearesult(patient));

		System.out.println("----------------------------------");

		patient.patientId="test";
		patient.heartRate=50.0;
		patient.respitatoryRate = 14.0;
		patient.systolicBloodPressure = 90.0;
		patient.oxygenRate = 90.0;
		System.out.println("result=" + rulesProcessorBolt.determineDyspnearesult(patient));

		System.out.println("----------------------------------");

		patient.patientId="test";
		patient.heartRate=-1.0;
		patient.respitatoryRate = 14.0;
		patient.systolicBloodPressure = 90.0;
		patient.oxygenRate = 90.0;
		System.out.println("result=" + rulesProcessorBolt.determineDyspnearesult(patient));
		System.out.println("----------------------------------");

		patient.patientId="test";
		patient.heartRate=30.0;
		patient.respitatoryRate = 5.0;
		patient.systolicBloodPressure = 80.0;
		patient.oxygenRate = 80.0;
		System.out.println("result=" + rulesProcessorBolt.determineDyspnearesult(patient));
		
		System.out.println("----------------------------------");
		patient.patientId="test";
		patient.heartRate=-1.0;
		patient.respitatoryRate = -1.0;
		patient.systolicBloodPressure = 80.0;
		patient.oxygenRate = 80.0;
		System.out.println("result=" + rulesProcessorBolt.determineDyspnearesult(patient));
		
		System.out.println("----------------------------------");

		
	}
}
