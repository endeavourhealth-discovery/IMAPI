package org.endeavourhealth.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IMPFilerCSV extends IMPFiler {
    private static Logger LOG = LoggerFactory.getLogger(IMPFilerCSV.class);
    private Map<String, FileWriter> dataCsv = new HashMap<>();
    private FileWriter rltnCsv;
    private String folder;
    public IMPFilerCSV(String folder) {
        this.folder = folder;
        try {
            rltnCsv = new FileWriter(folder + "rltn.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void writeData(String id, String type, ObjectNode root)  {
        try {
            String json = om.writeValueAsString(root);
            LOG.trace("JSON: {}", json);
            FileWriter fw = getWriter(type);
            fw.write(id + "\t" + json + System.lineSeparator());
            fw.flush();
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

    private FileWriter getWriter(String type) throws IOException {
        FileWriter result = dataCsv.get(type);
        if (result == null) {
            result = new FileWriter(folder + type + ".csv");
            dataCsv.put(type, result);
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        for (FileWriter v : dataCsv.values()) {
            v.close();
        }
        rltnCsv.close();
    }
}
