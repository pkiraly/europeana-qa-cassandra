CREATE KEYSPACE europeana WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};
USE europeana;
CREATE TABLE edm (id text, content text, PRIMARY KEY (id));

DESCRIBE keyspaces;
DESCRIBE tables;