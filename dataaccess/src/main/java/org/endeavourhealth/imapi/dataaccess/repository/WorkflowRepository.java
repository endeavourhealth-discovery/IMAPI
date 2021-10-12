package org.endeavourhealth.imapi.dataaccess.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.workflow.Task;
import org.endeavourhealth.imapi.statemachine.StateMachineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class WorkflowRepository extends BaseRepository{
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowRepository.class);

    private ObjectMapper om = new ObjectMapper();

    public List<Task> findAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT w.name as workflow, t.state,t.id, t.name")
                .add("FROM task t")
                .add("JOIN workflow w ON w.dbid = t.workflow")
                .add("ORDER BY w.name, t.state,t.id, t.name");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        tasks.add(new Task()
                                .setName(rs.getString("name"))
                                .setState(rs.getString("state"))
                                .setId(rs.getString("id"))
                                .setWorkflow(rs.getString("workflow")));
                    }
                }
            }
        }
        return tasks;
    }

    public List<StateMachineConfig> getWorkflows() throws SQLException {
        List<StateMachineConfig> workflows = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n").add("SELECT w.config FROM workflow w");
        try (Connection conn = ConnectionPool.get()){
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())){
                try (ResultSet rs = statement.executeQuery()){
                    while(rs.next()){
                        if(rs.getString("config") ==null || rs.getString("config").isEmpty()){
                            LOG.error("Config is missing");
                            return null;
                        }
                        StateMachineConfig workflow = om.readValue(rs.getString("config"), StateMachineConfig.class);
                        workflows.add(workflow);

                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

            }

        }
        return workflows;

    }



}
