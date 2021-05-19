package org.endeavourhealth.dataaccess.repository;


import org.endeavourhealth.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.report.SimpleCount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class SimpleCountRepository extends BaseRepository {
    public List<SimpleCount> getConceptTypeReport() throws SQLException {
        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT t.dbid, t.iri as iri, t.name as label, count(1) as `count`")
            .add("FROM concept_type ct")
            .add("JOIN concept t on t.iri = ct.type")
            .add("GROUP BY t.dbid , t.iri , t.name")
            .add("ORDER BY `count` DESC");

        return getSimpleCounts(sql.toString());
    }

    public List<SimpleCount> getConceptSchemeReport() throws SQLException {
        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT s.dbid, c.scheme as iri, s.name as label, count(1) as `count`")
            .add("FROM concept c")
            .add("JOIN concept s on s.iri = c.scheme")
            .add("WHERE c.scheme IS NOT NULL")
            .add("GROUP BY s.dbid,c.scheme, s.name")
            .add("ORDER BY `count` DESC");

        return getSimpleCounts(sql.toString());
    }

    public List<SimpleCount> getConceptStatusReport() throws SQLException {
        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT s.dbid, s.iri, s.name as label, count(1) as `count`")
            .add("FROM concept c")
            .add("JOIN concept s on s.iri = c.status")
            .add("GROUP BY s.dbid, s.iri, s.name")
            .add("ORDER BY `count` DESC");

        return getSimpleCounts(sql.toString());
    }

    private List<SimpleCount> getSimpleCounts(String sql) throws SQLException {
        List<SimpleCount> result = new ArrayList<>();
        try (Connection conn = ConnectionPool.get()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next())
                        result.add(new SimpleCount(rs.getString("iri"), rs.getString("label"), rs.getInt("count")));
                }
            }
        }
        return result;
    }
}
