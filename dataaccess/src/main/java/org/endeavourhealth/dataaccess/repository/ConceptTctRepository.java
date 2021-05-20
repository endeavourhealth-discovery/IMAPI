package org.endeavourhealth.dataaccess.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;


public class ConceptTctRepository extends BaseRepository{
    private ObjectMapper om = new ObjectMapper();

	public List<TTIriRef> findByDescendant_Iri_AndAncestor_IriIn(String iri, List<String> candidates) throws SQLException {
        List<TTIriRef> ancestors = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT a.iri AS anc_iri ,a.name AS anc_name")
                .add("FROM concept c")
                .add("JOIN tct tct ON tct.descendant = c.dbid")
                .add("LEFT JOIN concept a ON a.dbid = tct.ancestor")
                .add("WHERE c.iri = ?")
                .add("AND a.iri IN " + inList(candidates.size()));
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, iri);
                int i=2;
                for(String candidate : candidates) {
                    statement.setString(i++, candidate);
                }
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        ancestors.add(iri(rs.getString("anc_iri"), rs.getString("anc_name")));
                    }
                }
            }
        }
        return ancestors;
    };
    public List<TTConcept> findByDescendant_Iri_AndType_Iri_OrderByLevel(String iri, String type) throws SQLException, JsonProcessingException {
        List<TTConcept> ancestors = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT a.json AS anc_json")
                .add("FROM concept c")
                .add("JOIN tct tct ON tct.descendant = c.dbid")
                .add("LEFT JOIN concept a ON a.dbid = tct.ancestor")
                .add("LEFT JOIN concept t ON t.dbid = tct.type")
                .add("WHERE c.iri = ?")
                .add("AND t.iri = ?")
                .add("ORDER BY tct.level");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, iri);
                statement.setString(2,type);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        TTConcept concept = om.readValue(rs.getString("anc_json"), TTConcept.class);
                        ancestors.add(concept);
                    }
                }
            }
        }
        return ancestors;
    }

    public List<TTIriRef> findByDescendant_Iri_AndType_Iri_AndAncestor_IriIn(String iri, String type, List<String> candidates) throws SQLException {
        List<TTIriRef> ancestors = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT a.iri AS anc_iri ,a.name AS anc_name")
                .add("FROM concept c")
                .add("JOIN tct tct ON tct.descendant = c.dbid")
                .add("LEFT JOIN concept a ON a.dbid = tct.ancestor")
                .add("LEFT JOIN concept t ON t.dbid = tct.type")
                .add("WHERE c.iri = ?")
                .add("AND t.iri = ?")
                .add("AND a.iri IN " + inList(candidates.size()));
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, iri);
                statement.setString(2,type);
                int i=3;
                for(String candidate : candidates) {
                    statement.setString(i++, candidate);
                }
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        ancestors.add(iri(rs.getString("anc_iri"), rs.getString("anc_name")));
                    }
                }
            }
        }
        return ancestors;
    }


}
