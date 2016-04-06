Copy all files into the same directory, install Java 8 and then execute

<pre>
/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.77-0.b03.el6_7.x86_64/jre/bin/java  -Dlogging.config=logback.xml  -jar data-generator-1.1.0.jar
</pre>

#### Artem notes
##### install Java 8
```
sh jdk8.sh
```

##### make sure you edit application.yaml to configure output to consolr or to file

##### run latest generator code
```
/usr/lib/jvm/jre-1.8.0-openjdk.x86_64/bin/java -Dlogging.config=logback.xml  -jar data-generator*.jar
```
