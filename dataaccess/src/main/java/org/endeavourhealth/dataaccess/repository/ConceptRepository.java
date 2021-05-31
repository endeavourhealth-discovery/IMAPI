package org.endeavourhealth.dataaccess.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class ConceptRepository extends BaseRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ConceptRepository.class);

    private ObjectMapper om = new ObjectMapper();

    public TTConcept getConceptByIri(String iri) throws SQLException, JsonProcessingException {
        TTConcept concept = new TTConcept();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT c.json")
                .add("FROM concept c")
                .add("WHERE c.iri = ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, iri);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        if (rs.getString("json") == null || rs.getString("json").isEmpty()) {
		                    LOG.error("Concept is missing definition {}", iri);
		                    return null;
                        }
                        concept = om.readValue(rs.getString("json"), TTConcept.class);
                    }
                }
            }
        }
        return concept;
    }

    public TTIriRef getConceptReferenceByIri(String iri) throws SQLException {
        TTIriRef ttIriRef = new TTIriRef();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT c.iri, c.name")
                .add("FROM concept c")
                .add("WHERE c.iri = ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, iri);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        ttIriRef.setIri(rs.getString("iri")).setName(rs.getString("name"))  ;
                    }
                }
            }
        }
        return ttIriRef;
    }

    public ConceptSummary getConceptSummaryByIri(String iri) throws SQLException {
        ConceptSummary conceptSummary = new ConceptSummary();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT c.iri, c.name, c.code, s.iri AS schemeIri, s.name AS schemeName")
                .add("FROM concept c")
                .add("JOIN concept s ON s.iri = c.scheme")
                .add("WHERE c.iri = ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, iri);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        conceptSummary
                                .setIri(rs.getString("iri"))
                                .setName(rs.getString("name"))
                                .setCode(rs.getString("code"))
                                .setScheme(iri(rs.getString("schemeIri"),rs.getString("schemeName")));
                    }
                }
            }
        }
        return conceptSummary;
    }

    public List<TTIriRef> findAllByIriIn(Set<String> iris) throws SQLException {
        List<TTIriRef> concepts = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT c.iri, c.name")
                .add("FROM concept c")
                .add("WHERE c.iri IN " + inList(iris.size()));
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                int i=0;
                for(String iri : iris) {
                    statement.setString(++i, iri);
                }
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        concepts.add(iri(rs.getString("iri"),rs.getString("name")));
                    }
                }
            }
        }
        return concepts;
    }

    public String findByDbid(Integer dbid) throws SQLException {
        String code = "";
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT c.code")
                .add("FROM concept c")
                .add("WHERE c.iri = ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setInt(1, dbid);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        code = rs.getString("code") ;
                    }
                }
            }
        }
        return code;
    }

    public String isCoreCodeSchemeIncludedInVSet(String code,  String scheme,  String vSet) throws SQLException {
        String iri = "";
        StringJoiner sql = new StringJoiner("\n")
                .add("select s.iri ")
                .add("from concept c ")
                .add("join tct t on t.descendant = c.dbid ")
                .add("join concept a on a.dbid = t.ancestor ")
                .add("join concept tt on tt.dbid = t.type and tt.iri = ?")
                .add("join tpl l on l.object = a.dbid ")
                .add("join concept p on p.dbid = l.predicate and p.iri = ? ")
                .add("join concept s on s.dbid = l.subject and s.iri = ? ")
                .add("where c.code = ? and c.scheme = ? ");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, IM.IS_A.getIri());
                statement.setString(2, IM.HAS_MEMBER.getIri());
                statement.setString(3, vSet);
                statement.setString(4, code);
                statement.setString(5, scheme);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        iri = rs.getString("iri") ;
                    }
                }
            }
        }
        return iri;
    }

    public String isCoreCodeSchemeExcludedInVSet(String code,  String scheme,  String vSet) throws SQLException {
        String iri = "";
        StringJoiner sql = new StringJoiner("\n")
                .add("select s.iri ")
                .add("from concept c ")
                .add("join tct t on t.descendant = c.dbid ")
                .add("join concept a on a.dbid = t.ancestor ")
                .add("join concept tt on tt.dbid = t.type and tt.iri = ? ")
                .add("join tpl l on l.object = a.dbid ")
                .add("join concept p on p.dbid = l.predicate and p.iri = ? ")
                .add("join concept s on s.dbid = l.subject and s.iri = ? ")
                .add("where c.code = ? and c.scheme = ? ");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, IM.IS_A.getIri());
                statement.setString(2, IM.NOT_MEMBER.getIri());
                statement.setString(3, vSet);
                statement.setString(4, code);
                statement.setString(5, scheme);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        iri = rs.getString("iri") ;
                    }
                }
            }
        }
        return iri;
    }

    public String isLegacyCodeSchemeIncludedInVSet(String code,  String scheme,  String vSet) throws SQLException {
        String iri = "";
        StringJoiner sql = new StringJoiner("\n")
                .add("select s.iri ")
                .add("from concept c ")
                .add( "join tpl tl on tl.subject = c.dbid ")
                .add("join concept cl on cl.dbid = tl.object ")
                .add("join concept clp on clp.dbid = tl.predicate and clp.iri = ? ")
                .add("join tct t on t.descendant = c.dbid ")
                .add("join concept a on a.dbid = t.ancestor ")
                .add("join concept tt on tt.dbid = t.type and tt.iri = ? ")
                .add("join tpl l on l.object = a.dbid ")
                .add("join concept p on p.dbid = l.predicate and p.iri = ? ")
                .add("join concept s on s.dbid = l.subject and s.iri = ? ")
                .add("where c.code = ? and c.scheme = ? ");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, IM.MATCHED_TO.getIri());
                statement.setString(2, IM.IS_A.getIri());
                statement.setString(3, IM.HAS_MEMBER.getIri());
                statement.setString(4, vSet);
                statement.setString(5, code);
                statement.setString(6, scheme);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        iri = rs.getString("iri") ;
                    }
                }
            }
        }
        return iri;
    }

    public String isLegacyCodeSchemeExcludedInVSet(String code,  String scheme,  String vSet) throws SQLException {
        String iri = "";
        StringJoiner sql = new StringJoiner("\n")
                .add("select s.iri ")
                .add("from concept c ")
                .add( "join tpl tl on tl.subject = c.dbid ")
                .add("join concept cl on cl.dbid = tl.object ")
                .add("join concept clp on clp.dbid = tl.predicate and clp.iri = ? ")
                .add("join tct t on t.descendant = c.dbid ")
                .add("join concept a on a.dbid = t.ancestor ")
                .add("join concept tt on tt.dbid = t.type and tt.iri = ? ")
                .add("join tpl l on l.object = a.dbid ")
                .add("join concept p on p.dbid = l.predicate and p.iri = ? ")
                .add("join concept s on s.dbid = l.subject and s.iri = ? ")
                .add("where c.code = ? and c.scheme = ? ");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, IM.MATCHED_TO.getIri());
                statement.setString(2, IM.IS_A.getIri());
                statement.setString(3, IM.NOT_MEMBER.getIri());
                statement.setString(4, vSet);
                statement.setString(5, code);
                statement.setString(6, scheme);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        iri = rs.getString("iri") ;
                    }
                }
            }
        }
        return iri;
    }

}
