package org.endeavourhealth.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IMPFilerCSV extends IMPFiler {
    private static Logger LOG = LoggerFactory.getLogger(IMPFilerCSV.class);
    private FileWriter dataCsv;
    private FileWriter rltnCsv;
    public IMPFilerCSV(String folder) {
        try {
            dataCsv = new FileWriter(folder + "data.csv");
            rltnCsv = new FileWriter(folder + "rltn.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void writeData(String id, String type, ObjectNode root)  {
        try {
            String json = om.writeValueAsString(root);
            LOG.trace("JSON: {}", json);
            dataCsv.write(id + "\t" + json + System.lineSeparator());
            dataCsv.flush();
        } catch (JsonProcessingException e) {
            LOG.error("Could not serialize {}", id);
            throw new RuntimeException(e);
        } catch (IOException e) {
            LOG.error("Could not write data {}", id);
            throw new RuntimeException(e);
        }
    }

    void writeRelationship(String id, String rp, String target) {
        try {
            rltnCsv.write(id + "\t" + rp + "\t" + target + System.lineSeparator());
            rltnCsv.flush();
        } catch (IOException e) {
            LOG.error("Could not write relationship {}", id);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        dataCsv.close();
        rltnCsv.close();
    }
}
