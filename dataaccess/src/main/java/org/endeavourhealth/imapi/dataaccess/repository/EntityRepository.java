package org.endeavourhealth.imapi.dataaccess.repository;

import org.endeavourhealth.imapi.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.search.EntitySummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@SuppressWarnings("java:S1192") // Disable "Literals as const" rule for SQL
public class EntityRepository extends BaseRepository {
    public TTIriRef getEntityReferenceByIri(String iri) throws SQLException {
        TTIriRef ttIriRef = new TTIriRef();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT c.iri, c.name")
                .add("FROM entity c")
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

    public EntitySummary getEntitySummaryByIri(String iri) throws SQLException {
        EntitySummary entitySummary = new EntitySummary();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT c.iri, c.name, c.code, n.iri AS schemeIri, n.name AS schemeName")
                .add("FROM entity c")
                .add("LEFT JOIN namespace n ON n.iri = c.scheme")
                .add("WHERE c.iri = ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, iri);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        entitySummary
                                .setIri(rs.getString("iri"))
                                .setName(rs.getString("name"))
                                .setCode(rs.getString("code"))
                                .setScheme(iri(rs.getString("schemeIri"),rs.getString("schemeName")));
                    }
                }
            }
        }
        return entitySummary;
    }

    public List<TTIriRef> findAllByIriIn(Set<String> iris) throws SQLException {
        List<TTIriRef> entities = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT c.iri, c.name")
                .add("FROM `entity` c")
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
                        entities.add(iri(rs.getString("iri"),rs.getString("name")));
                    }
                }
            }
        }
        return entities;
    }

    public String findByDbid(Integer dbid) throws SQLException {
        String code = "";
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT c.code")
                .add("FROM entity c")
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
                .add("from entity c ")
                .add("join tct t on t.descendant = c.dbid ")
                .add("join entity a on a.dbid = t.ancestor ")
                .add("join entity tt on tt.dbid = t.type and tt.iri = ?")
                .add("join tpl l on l.object = a.dbid ")
                .add("join entity p on p.dbid = l.predicate and p.iri = ? ")
                .add("join entity s on s.dbid = l.subject and s.iri = ? ")
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
                .add("from entity c ")
                .add("join tct t on t.descendant = c.dbid ")
                .add("join entity a on a.dbid = t.ancestor ")
                .add("join entity tt on tt.dbid = t.type and tt.iri = ? ")
                .add("join tpl l on l.object = a.dbid ")
                .add("join entity p on p.dbid = l.predicate and p.iri = ? ")
                .add("join entity s on s.dbid = l.subject and s.iri = ? ")
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
                .add("from entity c ")
                .add( "join tpl tl on tl.subject = c.dbid ")
                .add("join entity cl on cl.dbid = tl.object ")
                .add("join entity clp on clp.dbid = tl.predicate and clp.iri = ? ")
                .add("join tct t on t.descendant = c.dbid ")
                .add("join entity a on a.dbid = t.ancestor ")
                .add("join entity tt on tt.dbid = t.type and tt.iri = ? ")
                .add("join tpl l on l.object = a.dbid ")
                .add("join entity p on p.dbid = l.predicate and p.iri = ? ")
                .add("join entity s on s.dbid = l.subject and s.iri = ? ")
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
                .add("from entity c ")
                .add( "join tpl tl on tl.subject = c.dbid ")
                .add("join entity cl on cl.dbid = tl.object ")
                .add("join entity clp on clp.dbid = tl.predicate and clp.iri = ? ")
                .add("join tct t on t.descendant = c.dbid ")
                .add("join entity a on a.dbid = t.ancestor ")
                .add("join entity tt on tt.dbid = t.type and tt.iri = ? ")
                .add("join tpl l on l.object = a.dbid ")
                .add("join entity p on p.dbid = l.predicate and p.iri = ? ")
                .add("join entity s on s.dbid = l.subject and s.iri = ? ")
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

	public Set<String> findAllPredicateIris() throws SQLException {
//		SELECT iri FROM entity WHERE dbid IN (SELECT DISTINCT predicate FROM tpl);
		Set<String> predicateIris = new HashSet<>();
		StringJoiner sql = new StringJoiner("\n")
				.add("SELECT iri")
				.add("FROM `entity`")
				.add("WHERE dbid IN ")
				.add("(SELECT DISTINCT predicate FROM tpl)");
		try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                    	predicateIris.add(rs.getString("iri"));
                    }
                }
            }
        }
		return predicateIris;
	}

}
