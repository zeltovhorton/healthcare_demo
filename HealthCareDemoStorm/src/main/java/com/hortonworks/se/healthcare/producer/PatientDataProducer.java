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


public class PatientDataProducer {

    private static final Logger LOG = Logger.getLogger(PatientDataProducer.class);

    public static void main(String[] args) 
            throws ParserConfigurationException, SAXException, IOException, URISyntaxException 
    {
        if (args.length != 2) 
        {
            
            System.out.println("Usage: PatientProducer <broker list> <zookeeper>");
            System.exit(-1);
        }
        
        LOG.debug("Using broker list:" + args[0] +", zk conn:" + args[1]);
        
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
        

        	Timestamp effective_time_frame = new Timestamp(new Date().getTime());
			String patientId = "patientId";
			Float heart_rate = null;
			Float systolic_blood_pressure = null;
			Float diastolic_blood_pressure = null;
			Float respiratory_rate = null;
			Float oxygen_saturation = null;
			String event = effective_time_frame + "|"
                    + patientId + "|" + heart_rate 
                    
                    + "|" + oxygen_saturation
                    + "|" + respiratory_rate
                    + "|" + systolic_blood_pressure
                    + "|" + diastolic_blood_pressure;
			
        	try {
                KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, event);
                LOG.info("Sending Messge PatientId#: " + patientId + ", msg:" + event);
                producer.send(data);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        	producer.close();
    }


}
