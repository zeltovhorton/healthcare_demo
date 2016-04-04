package com.hortonworks.se.healthcare.storm;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class PatientScheme implements Scheme 
{


    public static final String FIELD_PATIENT_ID  = "patientId";
    public static final String FIELD_HEART_RATE   = "heart_rate";
    public static final String FIELD_EVENT_TIME = "eventTime";
    public static final String FIELD_SYSTOLIC_BLOOD_PRESSURE  = "systolic_blood_pressure";
    public static final String FIELD_DIASTOLIC_BLOOD_PRESSURE  = "diastolic_blood_pressure";
    public static final String FIELD_RESPIRATORY_RATE   = "respiratory_rate";        
    public static final String FIELD_OXYGEN_SATURATION = "oxygen_saturation";
        
        
	private static final long serialVersionUID = -2990121166902741545L;

	private static final Logger LOG = Logger.getLogger(PatientScheme.class);
	
	//Is the deserializer provided to the kafka spout to deserialize kafka byte message stream to Values objects.
	@Override
	public List<Object> deserialize(byte[] bytes)
        {
		try 
                {
			String patientEvent = new String(bytes, "UTF-8");
			String[] pieces = patientEvent.split("\\|");
		
			
			Timestamp eventTime = Timestamp.valueOf(pieces[0]);
			String patientId = pieces[1];
			String heart_rate = pieces[2];
			String systolic_blood_pressure = pieces[3];
			String diastolic_blood_pressure= pieces[4];
			String respiratory_rate  = pieces[5];
			String oxygen_saturation  = pieces[6];
			return new Values(cleanup(heart_rate), cleanup(patientId), 
                                    eventTime, cleanup(systolic_blood_pressure), cleanup(diastolic_blood_pressure), cleanup(respiratory_rate), cleanup(oxygen_saturation));
			
		} 
                catch (UnsupportedEncodingException e) 
                {
                    LOG.error(e);
                    throw new RuntimeException(e);
		}
		
	}
//	String patientId = "patientId";
//	Float heart_rate = null;
//	Float systolic_blood_pressure = null;
//	Float diastolic_blood_pressure = null;
//	Float respiratory_rate = null;
//	Float oxygen_saturation = null;    
	@Override
	public Fields getOutputFields()
        {
            return new Fields(FIELD_PATIENT_ID,
                              FIELD_HEART_RATE, 
                             FIELD_EVENT_TIME, 
                             FIELD_DIASTOLIC_BLOOD_PRESSURE, 
                             FIELD_SYSTOLIC_BLOOD_PRESSURE, 
                             FIELD_RESPIRATORY_RATE);
		
	}
        
        private String cleanup(String str)
        {
            if (str != null)
            {
                return str.trim().replace("\n", "").replace("\t", "");
            } 
            else
            {
                return str;
            }
            
        }
}