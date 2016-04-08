package com.hortonworks.se.healthcare.test;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.TupleImpl;

import com.hortonworks.se.healthcare.storm.PatientHBaseBolt;


public class HBaseBoltTest {

	public static void main(String[] args) {

		PatientHBaseBolt hBaseBolt = new PatientHBaseBolt(null);
		hBaseBolt.prepare(null, null, null);
		long patientData = hBaseBolt.getPatientData("patient1");
		
	}


}
