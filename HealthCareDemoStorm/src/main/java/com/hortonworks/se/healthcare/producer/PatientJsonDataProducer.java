package com.hortonworks.se.healthcare.producer;

/**
 * PatientProducer class simulates the real time patient health device data event generation.
 *
 */
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.hortonworks.se.healthcare.storm.PatientScheme;

public class PatientJsonDataProducer {

	private static final Logger LOG = Logger
			.getLogger(PatientJsonDataProducer.class);

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException, URISyntaxException {
		if (args.length != 2) {

			System.out
					.println("Usage: PatientProducer <broker list> <zookeeper>");
			System.exit(-1);
		}

		LOG.debug("Using broker list:" + args[0] + ", zk conn:" + args[1]);

		// long events = Long.parseLong(args[0]);
		Random rnd = new Random();

		Properties props = new Properties();
		props.put("metadata.broker.list", args[0]);
		props.put("zk.connect", args[1]);
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("request.required.acks", "1");

		String TOPIC = "healthcaredemo";
		ProducerConfig config = new ProducerConfig(props);

		Producer<String, String> producer = new Producer<String, String>(config);

		
        Random random = new Random();
        int count=10;
		for(int i=0; i<count;i++){
            //random.nextBoolean()
    		try {
    			String json = PatientJsonDataProducer.generateSampleJson(random.nextBoolean()/* hear_rate */, random.nextBoolean()/* blood_pressure */, random.nextBoolean()/*respitory*/, random.nextBoolean()/*oxygen*/);
    			KeyedMessage<String, String> data = new KeyedMessage<String, String>(
    					TOPIC, json);
    			LOG.info(json );
    			producer.send(data);
    			Thread.sleep(1000);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        	
        }

		producer.close();
	}

	public static String generateSampleJson(boolean useHeartRate,
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
