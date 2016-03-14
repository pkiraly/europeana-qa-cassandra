# Cassandra part of Metadata Quality Assurance Framework

[Apache Cassandra](http://cassandra.apache.org/) is a NoSQL database fits for big data, and works nicely with Hadoop and Spark. In this project we tested the functionalities with version 2.2.5 of the standard Apache version. For installation in Ubuntu I recommend [this installation guide](https://www.digitalocean.com/community/tutorials/how-to-install-cassandra-and-run-a-single-node-cluster-on-ubuntu-14-04).

## To create database in Cassandra

Open Cassandra's command line interface with

    cqlsh

if Cassandra runs in your system as a service, otherwise 

    /path/to/cassandra/bin/cqlsh

and enter the following commands:

    CREATE KEYSPACE europeana WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};
    USE europeana;
    CREATE TABLE edm (id text, content text, PRIMARY KEY (id));

To check wether everything has been created:

    DESCRIBE keyspaces;

returns

    europeana  system_traces  system_auth  system  system_distributed

    DESCRIBE tables;

returns

    edm

Quit from the console:

    quit

## Compile

    mvn clean install
    
## Run

    java -cp target/europeana-qa-cassandra-1.0-SNAPSHOT-jar-with-dependencies.jar \
      com.nsdr.cassandra.Writer path/to/europeana.json

with `--truncate` option the programme first empty the edm table:

    java -cp target/europeana-qa-cassandra-1.0-SNAPSHOT-jar-with-dependencies.jar \
      com.nsdr.cassandra.Writer path/to/europeana.json --truncate
