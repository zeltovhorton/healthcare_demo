Copy all files into the same directory, install Java 8 and then execute

##### install Java 8
```
sh jdk8.sh
```

##### make sure you edit application.yaml to configure output to consolr or to file

##### run latest generator code
```
/usr/lib/jvm/jre-1.8.0-openjdk.x86_64/bin/java -Dlogging.config=logback.xml  -jar data-generator*.jar
```
