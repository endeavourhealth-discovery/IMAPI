package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionPool;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.workflow.Task;
import org.endeavourhealth.imapi.statemachine.StateMachineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class WorkflowRepositoryImpl implements WorkflowRepository {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowRepositoryImpl.class);

    private final ObjectMapper om = new ObjectMapper();

    @Override
    public List<Task> findAllTasks() throws DALException {
        List<Task> tasks = new ArrayList<>();
        String sql = new StringJoiner("\n")
            .add("SELECT w.name as workflow, t.state,t.id, t.name")
            .add("FROM task t")
            .add("JOIN workflow w ON w.dbid = t.workflow")
            .add("ORDER BY w.name, t.state,t.id, t.name")
            .toString();
        try (Connection conn = ConnectionPool.get();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    tasks.add(new Task()
                        .setName(rs.getString("name"))
                        .setState(rs.getString("state"))
                        .setId(rs.getString("id"))
                        .setWorkflow(rs.getString("workflow")));
                }
            }
        } catch (SQLException e) {
            throw new DALException("Failed to get tasks");
        }
        return tasks;
    }

    @Override
    public List<StateMachineConfig> getWorkflows() throws DALException {
        String config = "config";
        List<StateMachineConfig> workflows = new ArrayList<>();

        try (Connection conn = ConnectionPool.get();
             PreparedStatement statement = conn.prepareStatement("SELECT w.config FROM workflow w");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                if (rs.getString(config) == null || rs.getString(config).isEmpty()) {
                    LOG.error("Config is missing");
                    return Collections.emptyList();
                }
                StateMachineConfig workflow = om.readValue(rs.getString(config), StateMachineConfig.class);
                workflows.add(workflow);

            }
        } catch (SQLException e) {
            throw new DALException("Failed to get workflows", e);
        } catch (JsonProcessingException e) {
            throw new DALException("Failed to read workflow config", e);
        }
        return workflows;

    }



}
