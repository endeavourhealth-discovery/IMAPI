package org.endeavourhealth.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.logic.codegen.IMDMBase;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;

public class IMPFilerDB extends IMPFiler {
    private static Logger LOG = LoggerFactory.getLogger(IMPFilerDB.class);
    private Connection conn;
    private PreparedStatement usData;
    private PreparedStatement usRltn;
    public IMPFilerDB() {
        conn = null;
        String url = System.getenv("JDBC_URL") != null ? System.getenv("JDBC_URL") : "jdbc:postgresql://localhost:5433/yggdrasill";
        String user = System.getenv("JDBC_USER") != null ? System.getenv("JDBC_USER") : "postgres";
        String pass = System.getenv("JDBC_PASS") != null ? System.getenv("JDBC_PASS") : "postgres";

        try {
            conn = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        prepareUpserts();
    }

    IMPFilerDB(Connection conn) {
        this.conn = conn;
        prepareUpserts();
    }

    private void prepareUpserts() {
        try {
            this.usData = conn.prepareStatement("INSERT INTO main (id, json) VALUES (?, ?::json) ON CONFLICT (id) DO UPDATE SET json = ?::json");
            this.usRltn = conn.prepareStatement("INSERT INTO triple (subject, property, target) VALUES (?, ?, ?)");
        } catch (SQLException e) {
            LOG.error("Failed to prepare upsert statements");
            throw new RuntimeException(e);
        }
    }

    void writeData(String id, String type, ObjectNode root)  {
        try {
            String json = om.writeValueAsString(root);
            LOG.trace("JSON: {}", json);
            this.usData.setString(1, id);
            this.usData.setString(2, json);
            this.usData.setString(3, json);
            this.usData.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Error inserting main data for {}", id);
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            LOG.error("Could not serialize {}", id);
            throw new RuntimeException(e);
        }
    }

    void writeRelationship(String id, String rp, String target) {
        try {
            this.usRltn.setString(1, id);
            this.usRltn.setString(2, rp);
            this.usRltn.setString(3, target);
            this.usRltn.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Error inserting relationship");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        usData.close();
        usRltn.close();
        conn.close();
    }
}
