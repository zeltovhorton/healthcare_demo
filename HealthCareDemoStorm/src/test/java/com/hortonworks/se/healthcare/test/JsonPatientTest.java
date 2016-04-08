package com.hortonworks.se.healthcare.test;

import java.io.FileNotFoundException;
import java.util.Iterator;

import org.json.simple.JSONArray;

import com.hortonworks.se.healthcare.producer.PatientJsonDataProducer;
import com.hortonworks.se.healthcare.utils.PatientUtils;

public class JsonPatientTest {

	public static void main(String[] args) {

		PatientUtils.parseJson(PatientJsonDataProducer.generateSampleJson(
				true/* hear_rate */, true/* blood_pressure */,
				true/* respitory */, true/* oxygen */));
		System.out.println("----------------------------------");
		PatientUtils.parseJson(PatientJsonDataProducer.generateSampleJson(
				true/* hear_rate */, false/* blood_pressure */,
				true/* respitory */, true/* oxygen */));
		System.out.println("----------------------------------");
		PatientUtils.parseJson(PatientJsonDataProducer.generateSampleJson(
				true/* hear_rate */, true/* blood_pressure */,
				false/* respitory */, true/* oxygen */));
		System.out.println("----------------------------------");
		PatientUtils.parseJson(PatientJsonDataProducer.generateSampleJson(
				true/* hear_rate */, true/* blood_pressure */,
				false/* respitory */, false/* oxygen */));
		System.out.println("----------------------------------");
		PatientUtils.parseJson(PatientJsonDataProducer.generateSampleJson(
				true/* hear_rate */, true/* blood_pressure */,
				true/* respitory */, false/* oxygen */));
		System.out.println("----------------------------------");


	}
}
