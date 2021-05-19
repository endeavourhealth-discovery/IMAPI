package org.endeavourhealth.dataaccess.repository;


import org.endeavourhealth.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.report.SimpleCount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimpleCountRepository extends BaseRepository {
    public List<SimpleCount> getConceptTypeReport() throws SQLException {
        List<SimpleCount> result = new ArrayList<>();
        try (Connection conn = ConnectionPool.get()) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT t.dbid, t.iri as iri, t.name as label, count(1) as `count`\n" +
                "FROM concept_type ct \n" +
                "JOIN concept t on t.iri = ct.type \n" +
                "GROUP BY t.dbid , t.iri , t.name \n" +
                "ORDER BY `count` DESC  ")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next())
                        result.add(new SimpleCount(rs.getString("iri"), rs.getString("label"), rs.getInt("count")));
                }
            }
        }
        return result;
    }

    public List<SimpleCount> getConceptSchemeReport() throws SQLException {
        List<SimpleCount> result = new ArrayList<>();
        try (Connection conn = ConnectionPool.get()) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT s.dbid, c.scheme as iri, s.name as label, count(1) as `count` " +
                "FROM concept c " +
                "JOIN concept s on s.iri = c.scheme " +
                "WHERE c.scheme IS NOT NULL " +
                "GROUP BY s.dbid,c.scheme, s.name " +
                "ORDER BY `count` DESC")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next())
                        result.add(new SimpleCount(rs.getString("iri"), rs.getString("label"), rs.getInt("count")));
                }
            }
        }
        return result;
    }

    public List<SimpleCount> getConceptStatusReport() throws SQLException {
        List<SimpleCount> result = new ArrayList<>();
        try (Connection conn = ConnectionPool.get()) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT s.dbid, s.iri, s.name as label, count(1) as `count` " +
                "FROM concept c " +
                "JOIN concept s on s.iri = c.status " +
                "GROUP BY s.dbid, s.iri, s.name " +
                "ORDER BY `count` DESC")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next())
                        result.add(new SimpleCount(rs.getString("iri"), rs.getString("label"), rs.getInt("count")));
                }
            }
        }
        return result;
    }
}
