package com.hortonworks.se.healthcare.storm;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class HBaseBolt implements IRichBolt 
{
    private static final long serialVersionUID = 2946379346389650318L;
    private static final Logger LOG = Logger.getLogger(HBaseBolt.class);

    //TABLES
    private static final String EVENTS_TABLE_NAME =  "patient_events";
    private static final String EVENTS_COUNT_TABLE_NAME = "patient_danger_events";

    //CF
    private static final byte[] CF_EVENTS_TABLE = Bytes.toBytes("events");
    private static final byte[] CF_EVENTS_COUNT_TABLE = Bytes.toBytes("count");

    //COL
    private static final byte[] COL_COUNT_VALUE = Bytes.toBytes("value");

    private static final byte[] COL_PATIENT_ID = Bytes.toBytes("p");
    private static final byte[] COL_EVENT_TIME = Bytes.toBytes("tim");
    private static final byte[] COL_HEART_RATE = Bytes.toBytes("h");

    private OutputCollector collector;
    private HConnection connection;
    private HTableInterface eventsCountTable;
    private HTableInterface eventsTable;

    public HBaseBolt(Properties topologyConfig) 
    {

    }

    @Override
    public void prepare(Map stormConf, TopologyContext context,
                    OutputCollector collector) 
    {
        this.collector = collector;
        try 
        {
            this.connection = HConnectionManager.createConnection(constructConfiguration());
            this.eventsCountTable = connection.getTable(EVENTS_COUNT_TABLE_NAME);	
            this.eventsTable = connection.getTable(EVENTS_TABLE_NAME);
        } 
        catch (Exception e) 
        {
            String errMsg = "Error retrievinging connection and access to HBase Tables";
            LOG.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }		
    }

    @Override
    public void execute(Tuple tuple)
    {

        LOG.info("About to insert tuple into HBase...");

        String patientId = tuple.getStringByField(PatientScheme.FIELD_PATIENT_ID);
//        Timestamp eventTime = (Timestamp) tuple.getValueByField(PatientScheme.FIELD_EVENT_TIME);

        long incidentTotalCount = getInfractionCountForpatient(patientId);

        try 
        {

    		Timestamp effective_time_frame = new Timestamp(new Date().getTime());

            Put put = constructRow(EVENTS_TABLE_NAME, patientId, 100.23/*hear_rate*/,effective_time_frame
            		//,  eventTime
            		);
            this.eventsTable.put(put);

        } 
        catch (Exception e) 
        {
            LOG.error("Error inserting event into HBase table["+EVENTS_TABLE_NAME+"]", e);
        }
/*
        if(!eventType.equalsIgnoreCase("normal")) 
        {
            try 
            {
                //long incidentTotalCount = getInfractionCountForpatient(patientId);

                incidentTotalCount = this.eventsCountTable.incrementColumnValue(Bytes.toBytes(patientId), CF_EVENTS_COUNT_TABLE, 
                                                                                                        COL_COUNT_VALUE, 1L);
                LOG.info("Success inserting event into counts table");

            } 
            catch (Exception e)
            {
               LOG.error("Error inserting violation event into HBase table", e);
            }				
        } 
        collector.emit(tuple, new Values(patientId, truckId, eventTime, eventType, longitude, latitude, incidentTotalCount));
        */
        //acknowledge even if there is an error
        collector.ack(tuple);
    }


    public static  Configuration constructConfiguration()
    {
        Configuration config = HBaseConfiguration.create();
        return config;
    }	


    private Put constructRow(String columnFamily, String patientId, Double heartrate,  Timestamp eventTime)
    {

        String rowKey = consructKey(patientId, eventTime);
        Put put = new Put(Bytes.toBytes(rowKey));

        put.add(CF_EVENTS_TABLE, COL_PATIENT_ID, Bytes.toBytes(patientId));
        put.add(CF_EVENTS_TABLE, COL_HEART_RATE, Bytes.toBytes(heartrate));

        long eventTimeValue=  eventTime.getTime();
        put.add(CF_EVENTS_TABLE, COL_EVENT_TIME, Bytes.toBytes(eventTimeValue));

        return put;
    }


    private String consructKey(String patientId, Timestamp ts2)
    {
        long reverseTime = Long.MAX_VALUE - ts2.getTime();
        String rowKey = patientId +"|"+reverseTime;
        return rowKey;
    }	


    @Override
    public void cleanup() 
    {
        try 
        {
                eventsCountTable.close();
                eventsTable.close();
                connection.close();
        } 
        catch (Exception  e) 
        {
                LOG.error("Error closing connections", e);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields(PatientScheme.FIELD_PATIENT_ID,  PatientScheme.FIELD_EVENT_TIME,PatientScheme.FIELD_HEART_RATE,
            		PatientScheme.FIELD_SYSTOLIC_BLOOD_PRESSURE,PatientScheme.FIELD_DIASTOLIC_BLOOD_PRESSURE,
            		PatientScheme.FIELD_RESPIRATORY_RATE, PatientScheme.FIELD_OXYGEN_SATURATION));
    }

    @Override
    public Map<String, Object> getComponentConfiguration()
    {
            return null;
    }

    private long getInfractionCountForpatient(String patientId)
    {

        try 
        {
            byte[] patient = Bytes.toBytes(patientId);
            Get get = new Get(patient);
            Result result = eventsCountTable.get(get);
            long count = 0;
            if(result != null) 
            {
                byte[] countBytes = result.getValue(CF_EVENTS_COUNT_TABLE, COL_COUNT_VALUE);
                if(countBytes != null) 
                {
                    count = Bytes.toLong(countBytes);
                }
            }
            return count;
        } 
        catch (Exception e) 
        {
            LOG.error("Error getting infraction count", e);
            throw new RuntimeException("Error getting infraction count");
        }
    }	
}
