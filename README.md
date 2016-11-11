# README #

## What is this repository for? ##

Fraud detection

[googleDrive](https://drive.google.com/open?id=0B74PNn8ioxsfcnlmU05wWGZ4R1E)

### webcast ###

[googleDrive/webcasts](https://drive.google.com/open?id=0B74PNn8ioxsfY1YtRG1ZNWpEOHc)

## How do I get set up? ##

### Python libraries installation: ###
* install pip3   (apt-get install pyton3-pip or brew install python3-pip)
* pip3 install pymorphy2
* pip3 install numpy
* pip3 install scipy  (In case this won't work use this: http://stackoverflow.com/questions/26575587/cant-install-scipy-through-pip)
* pip3 install gensim
* pip3 install matplotlib

### Word2vec models ###

Download: [googleDrive/w2v_models](https://drive.google.com/open?id=0B74PNn8ioxsfazRGR29hQjR3S1U)
move to: frauddetection/Detection/src/main/resources/

### Build ###

mvn clean package -DskipTests -P cluster

### Run project ###

* start HDFS

  start-dfs.sh

* start zookeeper

  zookeeper-server-start.sh kafka/config/zookeeper.properties

* start kafka

  kafka-server-start.sh kafka/config/server.properties

* create topic

  kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic xml_data

* start storm nimbus

  storm nimbus

* start storm supevisor

  storm supervisor

* start storm ui

  storm ui

* start storm topology (local or production)

  storm jar frauddetection/FraudProject/StormManager/target/ru.spbstu.frauddetection.core.storm_manager-1.0-SNAPSHOT.jar ru.spbstu.frauddetection.core.storm_manager.StormStarter local

* send test data to kafka

kafka-console-producer.sh --broker-list localhost:9092 --topic xml_data < frauddetection/Documentation/XML/Medicine/test.xml
