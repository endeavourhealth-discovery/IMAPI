package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EntityTripleRepository extends BaseRepository{

    public List<TTIriRef> getActiveSubjectByObjectExcludeByPredicate(String objectIri, String predicateIri) throws SQLException {
        List<TTIriRef> usages = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT DISTINCT s.iri, s.name")
                .add("FROM tpl tpl")
                .add("JOIN entity o ON o.dbid = tpl.object ")
                .add("JOIN entity p ON p.dbid = tpl.predicate ")
                .add("JOIN entity s ON s.dbid = tpl.subject ")
                .add("WHERE o.iri = ?")
                .add("AND p.iri <> ?")
                .add("AND s.status <> ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, objectIri);
                statement.setString(2, predicateIri);
                statement.setString(3, IM.INACTIVE.getIri());
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        usages.add(iri(rs.getString("iri"),rs.getString("name")));
                    }
                }
            }
        }
        return usages;
    }

    public Set<ValueSetMember> getObjectBySubjectAndPredicate(String iri, String predicate) throws SQLException {
        Set<ValueSetMember> members = new HashSet<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT o.iri, o.name, o.code, sc.iri AS schemeIri, sc.name AS schemeName")
                .add("FROM tpl tpl")
                .add("JOIN entity s ON s.dbid = tpl.subject ")
                .add("JOIN entity p ON p.dbid = tpl.predicate ")
                .add("JOIN entity o ON o.dbid = tpl.object ")
                .add("JOIN entity sc ON sc.iri = o.scheme ")
                .add("WHERE s.iri = ?")
                .add("AND p.iri = ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, iri);
                statement.setString(2, predicate);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        members.add( new ValueSetMember()
                                .setEntity(iri(rs.getString("iri"),rs.getString("name")))
                                .setCode(rs.getString("code"))
                                .setScheme(iri(rs.getString("schemeIri"),rs.getString("schemeName"))));
                    }
                }
            }
        }
        return members;
    }

    public Set<TTIriRef> getMemberIriRefsBySubject_Iri_AndPredicate_Iri(String iri, String predicate) throws SQLException {
        Set<TTIriRef> memberIriRefs = new HashSet<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT o.iri, o.name")
                .add("FROM tpl tpl")
                .add("JOIN entity s ON s.dbid = tpl.subject ")
                .add("JOIN entity p ON p.dbid = tpl.predicate ")
                .add("JOIN entity o ON o.dbid = tpl.object ")
                .add("WHERE s.iri = ?")
                .add("AND p.iri = ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, iri);
                statement.setString(2, predicate);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        memberIriRefs.add(iri(rs.getString("iri"),rs.getString("name")));
                    }
                }
            }
        }
        return memberIriRefs;
    }

    public List<TTIriRef> getCoreMappedFromLegacyBySubject_Iri_AndPredicate_Iri(String iri, String predicate) throws SQLException {
        List<TTIriRef> coreMappedFromLegacy = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT o.iri, o.name")
                .add("FROM tpl tpl")
                .add("JOIN entity s ON s.dbid = tpl.subject ")
                .add("JOIN entity p ON p.dbid = tpl.predicate ")
                .add("JOIN entity o ON o.dbid = tpl.object ")
                .add("WHERE s.iri = ?")
                .add("AND p.iri = ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, iri);
                statement.setString(2, predicate);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        coreMappedFromLegacy.add(iri(rs.getString("iri"),rs.getString("name")));
                    }
                }
            }
        }
        return coreMappedFromLegacy;
    }

    public List<TTIriRef> findAllByObject_Iri_AndPredicate_Iri(String iri, String predicate) throws SQLException {
        List<TTIriRef> legacyMappedToCore = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT s.iri, s.name")
                .add("FROM tpl tpl")
                .add("JOIN entity o ON o.dbid = tpl.object ")
                .add("JOIN entity p ON p.dbid = tpl.predicate ")
                .add("JOIN entity s ON s.dbid = tpl.subject ")
                .add("WHERE o.iri = ?")
                .add("AND p.iri = ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, iri);
                statement.setString(2, predicate);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        legacyMappedToCore.add(iri(rs.getString("iri"),rs.getString("name")));
                    }
                }
            }
        }
        return legacyMappedToCore;
    }

    public List<TTIriRef> findImmediateParentsByIri(String iri, Integer rowNumber, Integer pageSize, boolean includeInactive) throws SQLException {
        List<TTIriRef> parents = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT o.iri, o.name, o.status")
                .add("FROM entity c ")
                .add("JOIN tpl t ON t.subject = c.dbid ")
                .add("JOIN entity p ON p.dbid = t.predicate AND p.iri IN(?, ?) ")
                .add("JOIN entity o ON o.dbid = t.object ")
                .add("WHERE c.iri = ?");
        if(!includeInactive)
            sql.add("AND o.status <> ?").add("AND o.iri <> ?");
        sql.add("ORDER BY o.name ");
        if(rowNumber!=null && pageSize!=null)
            sql.add("LIMIT ? , ? ");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                int i = 0;
                statement.setString(++i, IM.IS_A.getIri());
                statement.setString(++i, IM.IS_CONTAINED_IN.getIri());
                statement.setString(++i, iri);
                if(!includeInactive) {
                    statement.setString(++i, IM.INACTIVE.getIri());
                    statement.setString(++i, OWL.THING.getIri());
                }
                if(rowNumber!=null && pageSize!=null) {
                    statement.setInt(++i, rowNumber);
                    statement.setInt(++i, pageSize);
                }
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        parents.add(iri(rs.getString("iri"),rs.getString("name")));
                    }
                }
            }
        }
        return parents;
    }

    public List<TTIriRef> findImmediateChildrenByIri(String iri, Integer rowNumber, Integer pageSize, boolean includeInactive) throws SQLException {
        List<TTIriRef> children = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT s.iri, s.name")
                .add("FROM entity c ")
                .add("JOIN tpl t ON t.object = c.dbid ")
                .add("JOIN entity p ON p.dbid = t.predicate AND p.iri IN(?, ?, ?) ")
                .add("JOIN entity s ON s.dbid = t.subject ")
                .add("WHERE c.iri = ?");
        if(!includeInactive)
            sql.add("AND s.status <> ?");
        sql.add("ORDER BY s.name ");
        if(rowNumber!=null && pageSize!=null)
            sql.add("LIMIT ? , ? ");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                int i = 0;
                statement.setString(++i, IM.IS_A.getIri());
                statement.setString(++i, IM.IS_CONTAINED_IN.getIri());
                statement.setString(++i, IM.IS_CHILD_OF.getIri());
                statement.setString(++i, iri);
                if(!includeInactive)
                    statement.setString(++i, IM.INACTIVE.getIri());
                if(rowNumber!=null && pageSize!=null) {
                    statement.setInt(++i, rowNumber);
                    statement.setInt(++i, pageSize);
                }
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        children.add(iri(rs.getString("iri"),rs.getString("name")));
                    }
                }
            }
        }
        return children;
    }

}
