package com.hortonworks.se.healthcare.storm;

import antlr.Utils;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hortonworks.se.healthcare.dto.Patient;
import com.hortonworks.se.healthcare.utils.PatientUtils;


public class SepsisRulesProcessorBolt extends BaseRichBolt
{
    private static final String ALL_NULLS = "ALL_NULLS";
	private static final String BAD_DATA = "BAD_DATA";
    private static final String CRITICAL = "CRITICAL";
    private static final String WARN = "WARN";
    private static final String NORMAL = "NORMAL";
    
	private static final Logger LOG = Logger.getLogger(SepsisRulesProcessorBolt.class);
    
    public void declareOutputFields(OutputFieldsDeclarer ofd) 
    {
       //none prints to the Logger.
    }

    public void prepare(Map map, TopologyContext tc, OutputCollector oc) 
    {
       //no output.
    }

    public void execute(Tuple tuple) 
    {
    	Patient patient = new Patient();
    	patient.patientId = tuple.getStringByField(PatientScheme.FIELD_PATIENT_ID);
    	System.out.println("patientID=" + patient.patientId);
    	patient.heartRate = tuple.getDoubleByField(PatientScheme.FIELD_HEART_RATE);
    	System.out.println("heartRate=" + patient.heartRate);

//    	Object eventTime = tuple.getValueByField(PatientScheme.FIELD_EVENT_TIME);
//    	System.out.println("eventTime=" + eventTime);

//		patient.eventTime = new Timestamp(new Date().getTime()).toString();
    	
    	patient.diastolicBloodPressure = tuple.getDoubleByField(PatientScheme.FIELD_DIASTOLIC_BLOOD_PRESSURE);
    	System.out.println("diastolicBloodPressure=" + patient.diastolicBloodPressure);
    	
    	patient.systolicBloodPressure = tuple.getDoubleByField(PatientScheme.FIELD_SYSTOLIC_BLOOD_PRESSURE);
    	System.out.println("systolicBloodPressure=" + patient.systolicBloodPressure);

    	patient.respitatoryRate = tuple.getDoubleByField(PatientScheme.FIELD_RESPIRATORY_RATE);
    	System.out.println("respitatoryRate=" + patient.respitatoryRate);
    	
    	patient.oxygenRate = tuple.getDoubleByField(PatientScheme.FIELD_OXYGEN_SATURATION);
    	System.out.println("oxygenRate=" + patient.oxygenRate);

    	String determineDyspnearesult = determineDyspnearesult(patient);
    	System.out.println("determineDyspnearesult" + determineDyspnearesult);
    	if (determineDyspnearesult!=ALL_NULLS){
        	PatientUtils.createJsonResultForSepsis(patient, determineDyspnearesult);
    	}
    	
//      LOG.info(tuple.getStringByField(PatientScheme.FIELD_PATIENT_ID)  + "," + 
//              tuple.getDoubleByField(PatientScheme.FIELD_HEART_RATE)    + "," +
//              tuple.getValueByField(PatientScheme.FIELD_EVENT_TIME)  + "," +
//              tuple.getDoubleByField(PatientScheme.FIELD_DIASTOLIC_BLOOD_PRESSURE)  + "," +
//              tuple.getDoubleByField(PatientScheme.FIELD_RESPIRATORY_RATE)    + "," +
//              tuple.getDoubleByField(PatientScheme.FIELD_OXYGEN_SATURATION)    + "," +
//              tuple.getDoubleByField(PatientScheme.FIELD_SYSTOLIC_BLOOD_PRESSURE));
      
    }

	public String determineDyspnearesult(Patient patient) {
		String result = BAD_DATA;
		if (patient.heartRate ==-1 && patient.respitatoryRate==-1  && patient.systolicBloodPressure==-1  && patient.oxygenRate ==-1){
			result = ALL_NULLS;			
		}
		else if (patient.heartRate <=49 || patient.respitatoryRate<=10 || patient.systolicBloodPressure<=89 || patient.oxygenRate <=89){
			result = CRITICAL;
		}
		else if ((patient.heartRate >49 && patient.heartRate <=59) || (patient.respitatoryRate >10 && patient.respitatoryRate<=15) || ( patient.systolicBloodPressure>89 && patient.systolicBloodPressure<=99 )||( patient.oxygenRate> 89 && patient.oxygenRate <=94)){
			result = WARN;			
		}
		else if ((patient.heartRate >59 && patient.heartRate <=100) || (patient.respitatoryRate >15 && patient.respitatoryRate<=20) || ( patient.systolicBloodPressure>99 && patient.systolicBloodPressure<=140 )||( patient.oxygenRate> 95  && patient.oxygenRate <=100)){
			result = NORMAL;			
		}
		else if ((patient.heartRate >100 && patient.heartRate <=119) || (patient.respitatoryRate > 20 && patient.respitatoryRate<=29) || ( patient.systolicBloodPressure>140 && patient.systolicBloodPressure<=180 )){
			result = WARN;			
		}
		else if ((patient.heartRate >120) || (patient.respitatoryRate >30 ) || ( patient.systolicBloodPressure >181 )){
			result = CRITICAL;			
		}
		return result;
	}
    
}
