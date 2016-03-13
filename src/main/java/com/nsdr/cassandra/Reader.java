package com.nsdr.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class Reader {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        Cluster cluster;
        Session session;

        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = cluster.connect("europeana");

        ResultSet results = session.execute("SELECT * FROM edm");
        for (Row row : results) {
            System.out.format("%s\n", row.getString("id"));
            try {
                Map<String, Object> json = mapper.readValue(row.getString("content"),
                        new TypeReference<HashMap<String, Object>>() {
                        });
                System.out.format("\t%s\n", (String) json.get("qIdentifier"));
            } catch (IOException ex) {
                Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        session.close();
        cluster.close();
    }
}
