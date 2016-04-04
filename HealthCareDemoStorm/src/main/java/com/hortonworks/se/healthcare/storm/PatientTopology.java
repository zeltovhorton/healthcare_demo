package com.hortonworks.se.healthcare.storm;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

public class PatientTopology extends BaseEventTopology 
{
    private static final String KAFKA_SPOUT_ID = "kafkaSpout"; 
    private static final String LOG_BOLT_ID = "logPatientEventBolt";
            
    public PatientTopology(String configFileLocation) throws Exception 
    {
        super(configFileLocation);
    }

    private SpoutConfig constructKafkaSpoutConf() 
    {
        BrokerHosts hosts = new ZkHosts(topologyConfig.getProperty("kafka.zookeeper.host.port"));
        String topic = topologyConfig.getProperty("kafka.topic");
        String zkRoot = topologyConfig.getProperty("kafka.zkRoot");
        String consumerGroupId = "StormSpout";

        SpoutConfig spoutConfig = new SpoutConfig(hosts, topic, zkRoot, consumerGroupId);

        spoutConfig.scheme = new SchemeAsMultiScheme(new PatientScheme());

        return spoutConfig;
    }
    
    
    
    public void configureKafkaSpout(TopologyBuilder builder) 
    {
        KafkaSpout kafkaSpout = new KafkaSpout(constructKafkaSpoutConf());
        int spoutCount = Integer.valueOf(topologyConfig.getProperty("spout.thread.count"));
        builder.setSpout(KAFKA_SPOUT_ID, kafkaSpout);
    }
    
    public void configureLogEventBolt(TopologyBuilder builder)
    {
        LogEventsBolt logBolt = new LogEventsBolt();
        builder.setBolt(LOG_BOLT_ID, logBolt).globalGrouping(KAFKA_SPOUT_ID);
    }
    
    private void buildAndSubmit() throws Exception
    {
        TopologyBuilder builder = new TopologyBuilder();
        configureKafkaSpout(builder);
        configureLogEventBolt(builder);
        
        Config conf = new Config();
        conf.setDebug(true);
        
        StormSubmitter.submitTopology("healthcare-event-processor", 
                                    conf, builder.createTopology());
    }

    public static void main(String[] str) throws Exception
    {
        String configFileLocation = "healthcare_event_topology.properties";
        PatientTopology healthcareTopology 
                = new PatientTopology(configFileLocation);
        healthcareTopology.buildAndSubmit();
    }

}
