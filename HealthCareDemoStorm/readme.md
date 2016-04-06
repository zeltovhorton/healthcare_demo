
# /opt/HealthCareDemoStorm/target/HealthCareDemo-1.0-SNAPSHOT-shaded.jar

#setup kafka
cd /usr/hdp/current/kafka-broker/bin/
./kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic healthcaredemo 


#storm
[root@sandbox ~]# storm jar target/HealthCareDemo-1.0-SNAPSHOT.jar com.hortonworks.se.healthcare.storm.PatientTopology

#storm kill PatientTopology

#HBASE
[root@sandbox ~]$ su hbase
[hbase@sandbox root]$ hbase shell
hbase(main):001:0> create 'patient_events', 'events'    
hbase(main):002:0> create 'patient_danger_events', 'count'    
hbase(main):003:0> list    
hbase(main):004:0> exit 

#producer simulate
java -cp target/HealthCareDemo-1.0-SNAPSHOT-shaded.jar com.hortonworks.se.healthcare.PatientDataProducer sandbox.hortonworks.com:6667 sandbox.hortonworks.com:2181

  