# Cassandra part of Metadata Quality Assurance Framework

[Apache Cassandra](http://cassandra.apache.org/) is a NoSQL database fits for big data, and works nicely with Hadoop and Spark.

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
    DESCRIBE tables;
