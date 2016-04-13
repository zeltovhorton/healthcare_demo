##### requires maven
```
sh install_mvn.sh
exit
```
##### log back in and check maven works
```
mvn -v
```

####setup kafka
Make sure its started
```
cd /usr/hdp/current/kafka-broker/bin/
./kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic healthcaredemo 
```
####cleanup kafka q. First enable property in ambari->kafka->configs : delete.topic.enable=true
```
cd /usr/hdp/current/kafka-broker/bin/
./kafka-topics.sh --zookeeper localhost:2181 --delete --topic healthcaredemo
```
####check the kafka q
```
/usr/hdp/current/kafka-broker/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic healthcaredemo --from-beginning
```

####storm
```
mkdir -p /opt/HealthCareDemo/
cp {GIT_DIRECTORY}/HealthCareDemoStorm/src/main/resources/healthcare_event_topology.properties /opt/HealthCareDemo/
storm jar target/HealthCareDemo-1.0-SNAPSHOT.jar com.hortonworks.se.healthcare.storm.PatientTopology /opt/HealthCareDemo/healthcare_event_topology.properties
```
####Kill Healthcare Storm Topology
```
storm kill healthcare-event-processor
```

####HBASE
```
[root@sandbox ~]$ su hbase
[hbase@sandbox root]$ hbase shell
hbase(main):001:0> create 'patient_events', 'events'    
hbase(main):002:0> create 'patient_danger_events', 'count'    
hbase(main):003:0> list    
hbase(main):004:0> exit 
```
####producer simulate
```
[root@sandbox HealthCareDemoStorm]# java -cp target/HealthCareDemo-1.0-SNAPSHOT.jar com.hortonworks.se.healthcare.producer.PatientDataProducer sandbox.hortonworks.com:6667 sandbox.hortonworks.com:2181
```
##### hbase bolt requires core-site.xml, hdfs-site.xml and hbase-site.xml on storm's classpath or in src/main/resources 

### solr (hdp search)
```
cd
./start_solr.sh
sh /opt/lucidworks/solr/bin/solr create -c healthcare -d /opt/lucidworks-hdpsearch/solr/server/solr/configsets/data_driven_schema_configs/conf -n healthcare
```

#### solr (fresh install)
```
wget http://apache.mirror1.spango.com/lucene/solr/5.2.1/solr-5.2.1.tg

root@sandbox ~]# sh /root/solr-5.2.1/bin/solr start

Started Solr server on port 8983 (pid=21554). Happy searching!

[root@sandbox ~]# sh /root/solr-5.2.1/bin/solr create -c healthcare -d /root/solr-5.2.1/server/solr/configsets/data_driven_schema_configs/conf -n healthcare

Setup new core instance directory:
/root/solr-5.2.1/server/solr/healthcare

Creating new core 'healthcare' using command:
http://localhost:8983/solr/admin/cores?action=CREATE&name=healthcare&instanceDir=healthcare
```
####BANANA
You don't need to do this if you're using HDP Search
unzip banana-release.zip
mv banana-release banana
cp -R banana /root/solr-5.2.1/server/solr-webapp/

#### TO DELETE SOLR INDEX
```
curl "http://localhost:8983/solr/admin/cores?action=UNLOAD&core=healthcare&deleteIndex=true"
```
  
