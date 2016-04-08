package com.hortonworks.se.healthcare.storm;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import java.util.UUID;
import org.apache.storm.hbase.bolt.HBaseBolt;
import org.apache.storm.hbase.bolt.mapper.SimpleHBaseMapper;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

public class PatientTopologyArtem extends BaseEventTopology {

    private static final String KAFKA_SPOUT_ID = "kafkaSpout";
    private static final String LOG_BOLT_ID = "logPatientEventBolt";
    private static final String HBASE_BOLT_ID = "hbaseBolt";

    public PatientTopologyArtem(String configFileLocation) throws Exception {
        super(configFileLocation);
    }

    // TODO make sure Kafka reads from specific point/offset
    // Make sure to do conf.set hbase-conf-key like Alex showed you
    
    
    
    
    private SpoutConfig constructKafkaSpoutConf() {
        BrokerHosts hosts = new ZkHosts(topologyConfig.getProperty("kafka.zookeeper.host.port"));
        String topic = topologyConfig.getProperty("kafka.topic");
        String zkRoot = topologyConfig.getProperty("kafka.zkRoot");
        String consumerGroupId = "StormSpout";

        SpoutConfig spoutConfig = new SpoutConfig(hosts, topic, zkRoot, consumerGroupId);

        spoutConfig.scheme = new SchemeAsMultiScheme(new PatientScheme());

        return spoutConfig;
    }

    public void configureKafkaSpout(TopologyBuilder builder) {
        KafkaSpout kafkaSpout = new KafkaSpout(constructKafkaSpoutConf());
        int spoutCount = Integer.valueOf(topologyConfig.getProperty("spout.thread.count"));
        builder.setSpout(KAFKA_SPOUT_ID, kafkaSpout);
    }

    public void configureLogEventBolt(TopologyBuilder builder) {
        SepsisRulesProcessorBolt logBolt = new SepsisRulesProcessorBolt();
        builder.setBolt(LOG_BOLT_ID, logBolt).globalGrouping(KAFKA_SPOUT_ID);
    }

    public void configureHBaseBolt(TopologyBuilder builder) {
        SimpleHBaseMapper mapper = new SimpleHBaseMapper()
                .withRowKeyField("patient_id_s")
                .withColumnFields(new Fields("diastolic_blood_pressure_f"))
                .withColumnFields(new Fields("oxygen_saturation_f"))
                .withColumnFields(new Fields("heart_rate_f"))
                .withColumnFields(new Fields("respiratory_rate_f"))
                .withColumnFields(new Fields("systolic_blood_pressure_f"))
                .withColumnFields(new Fields("effective_time_frame_tdt"))
                // "patient_id_s,heart_rate_f,respiratory_rate_f,oxygen_saturation_f,systolic_blood_pressure_f,diastolic_blood_pressure_f"
                // .withCounterFields(new Fields("count"))
                .withColumnFamily("events");

        HBaseBolt hbaseBolt = new HBaseBolt("HBaseBolt", mapper);
        builder.setBolt(HBASE_BOLT_ID, hbaseBolt, 2).shuffleGrouping(KAFKA_SPOUT_ID);
    }

    private void buildAndSubmit() throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        configureKafkaSpout(builder);
        //configureLogEventBolt(builder);
        configureHBaseBolt(builder);

        Config conf = new Config();
        conf.setDebug(true);

        StormSubmitter.submitTopology("healthcare-event-processor",
                conf, builder.createTopology());
    }

    public static void main(String[] args) throws Exception {
        String configFileLocation = "/opt/HealthCareDemo/healthcare_event_topology.properties";
        if (args[0] != null & args[0].length() > 0) {
            configFileLocation = args[0];
        }
        PatientTopologyArtem healthcareTopology
                = new PatientTopologyArtem(configFileLocation);
        healthcareTopology.buildAndSubmit();
    }

}
