##### Artem Notes
##### tested with Nifi 0.6.0

##### prepare directories
```
mkdir -p /tmp/vitals/bloodpressure_flat
cp ../generator/output.json /tmp/vitals
```

##### Kafka
##### on sandbox expects brokers list as "sandbox.hortonworks.com:6667" and topic "healthcaredemo" and client name "nifi"
##### confirm messages are being pumped
```
/usr/hdp/current/kafka-broker/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic healthcaredemo --from-beginning
```

