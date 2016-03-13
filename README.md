# Cassandra part of Metadata Quality Assurance Framework

To create database in Cassandra:

    CREATE KEYSPACE europeana WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};
    USE europeana;
    CREATE TABLE edm (id text, content text, PRIMARY KEY (id));

To check wether everything has been created:

    DESCRIBE keyspaces;
    DESCRIBE tables;
