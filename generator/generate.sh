#!/usr/bin/env bash

mkdir -p /tmp/vitals
mkdir -p ./generated
chmod goa+x /tmp/vitals/
chmod a+w -R /tmp/vitals
rm -f ./generated/*.json /tmp/vitals/*.txt

names=$(cat patients.csv  | tail -n +2 | cut -d, -f3)
for n in $names; do

    start=$(date -d "-10 minutes" +"%Y-%m-%dT%H:%M:%SZ")
    end=$(date -d "+10 minutes" +"%Y-%m-%dT%H:%M:%SZ")

    perl -pe "s/(start-date-time: ).*/start-date-time: $start/g" -i application.yaml
    perl -pe "s/(end-date-time: ).*/end-date-time: $end/g" -i application.yaml

    perl -pe "s/filename: .*/filename: \.\/generated\/$n.json/g" -i application.yaml
    perl -pe "s/user-id: .*/user-id: $n/g" -i application.yaml
    /usr/lib/jvm/jre-1.8.0-openjdk.x86_64/bin/java -Dlogging.config=logback.xml  -jar data-generator*.jar
done

for m in blood-pressure respiratory-rate heart-rate oxygen-saturation; do
    grep -h $m ./generated/*.json > /tmp/vitals/$m.json
done