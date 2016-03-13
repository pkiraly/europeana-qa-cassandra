package com.nsdr.cassandra;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class Writer {

    private static final Logger log = Logger.getLogger(Writer.class.getCanonicalName());

    public static void main(String[] args) {
        if (args.length == 0 || StringUtils.isBlank(args[0])) {
            log.severe("Give a Json file name");
            System.exit(1);
        }
        String fileName = args[0];
        File inFile = new File(fileName);
        if (!inFile.exists() || !inFile.canRead()) {
            log.severe(String.format("File is not existing or not readable: %s", fileName));
            System.exit(1);
        }

        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        Cluster cluster;
        Session session;

        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = cluster.connect("europeana");

        if (args.length >= 2 && "--truncate".equals(args[1])) {
          // clear database
          log.info("Clear table 'edm'");
          session.execute("TRUNCATE edm");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
            String line;
            PreparedStatement statement = session.prepare(
                    "INSERT INTO edm (id, content) VALUES (?,?);");
            BoundStatement boundStatement = new BoundStatement(statement);

            int i = 0;
            while ((line = br.readLine()) != null) {
                Map<String, Object> json = mapper.readValue(line,
                        new TypeReference<HashMap<String, Object>>() {
                        });
                String id = (String) json.get("identifier");
                session.execute(boundStatement.bind(id, line));
                if (++i % 10000 == 0)
                    log.info(String.format("Indexed %d", i));
            }
            log.info(String.format("Finished. Indexed %d", i));
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }

        session.close();
        cluster.close();
    }
}
