package com.hortonworks.se.healthcare.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import java.util.Map;
import org.apache.log4j.Logger;


public class LogEventsBolt extends BaseRichBolt
{
    private static final Logger LOG = Logger.getLogger(LogEventsBolt.class);
    
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
      LOG.info(tuple.getStringByField(PatientScheme.FIELD_PATIENT_ID)  + "," + 
              tuple.getDoubleByField(PatientScheme.FIELD_HEART_RATE)    + "," +
              tuple.getValueByField(PatientScheme.FIELD_EVENT_TIME)  + "," +
              tuple.getDoubleByField(PatientScheme.FIELD_DIASTOLIC_BLOOD_PRESSURE)  + "," +
              tuple.getDoubleByField(PatientScheme.FIELD_RESPIRATORY_RATE)    + "," +
              tuple.getDoubleByField(PatientScheme.FIELD_OXYGEN_SATURATION)    + "," +
              tuple.getDoubleByField(PatientScheme.FIELD_SYSTOLIC_BLOOD_PRESSURE));
      
    }
    
}
