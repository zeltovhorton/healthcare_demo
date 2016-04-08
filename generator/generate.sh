#!/usr/bin/env bash

names=$(cat patients.csv  | tail -n +2 | cut -d, -f3)
for n in $names; do

    start=$(date -d "-1 days" +"%Y-%m-%dT%H:%M:%SZ")
    end=$(date -d "+1 days" +"%Y-%m-%dT%H:%M:%SZ")

    perl -pe "s/(start-date-time: ).*/start-date-time: $start/g" -i application.yaml
    perl -pe "s/(end-date-time: ).*/end-date-time: $end/g" -i application.yaml

    perl -pe "s/some-user/$n/g" -i application.yaml
    /usr/lib/jvm/jre-1.8.0-openjdk.x86_64/bin/java -Dlogging.config=logback.xml  -jar data-generator*.jar
done