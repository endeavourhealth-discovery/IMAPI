package org.endeavourhealth.imapi.dataaccess.repository;

import org.endeavourhealth.imapi.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.dataaccess.helpers.DALHelper;
import org.endeavourhealth.imapi.model.Namespace;
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

@SuppressWarnings("java:S1192") // Disable "Literals as const" rule for SQL
public class EntityTripleRepository extends BaseRepository{

    public List<Tpl> getTriplesRecursive(String iri, Set<String> predicates) throws SQLException {
        List<Tpl> result = new ArrayList<>();

        StringJoiner sql = new StringJoiner("\n")
            .add("WITH RECURSIVE triples AS (")
            .add("\tSELECT tpl.dbid, tpl.subject, tpl.blank_node AS parent, tpl.predicate, tpl.object, tpl.literal, tpl.functional")
            .add("\tFROM tpl")
            .add("\tJOIN entity e ON tpl.subject=e.dbid")
            .add("\tJOIN entity p ON p.dbid = tpl.predicate")
            .add("\tWHERE e.iri = ? ");
        if (predicates != null && !predicates.isEmpty())
            sql.add("\tAND p.iri IN " + inList(predicates.size())) ;
        sql.add("\tAND tpl.blank_node IS NULL")
            .add("UNION ALL")
            .add("\tSELECT t2.dbid, t2.subject, t2.blank_node AS parent, t2.predicate, t2.object, t2.literal, t2.functional")
            .add("\tFROM triples t")
            .add("\tJOIN tpl t2 ON t2.blank_node= t.dbid")
            .add("\tWHERE t2.dbid <> t.dbid")
            .add(")")
            .add("SELECT t.dbid, t.parent, p.iri AS predicateIri, p.name AS predicate, o.iri AS objectIri, o.name AS object, t.literal, t.functional")
            .add("FROM triples t")
            .add("JOIN entity p ON t.predicate = p.dbid")
            .add("LEFT JOIN entity o ON t.object = o.dbid;");

        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                int i = 0;
                statement.setString(++i, iri);
                if (predicates != null && !predicates.isEmpty()) {
                    for (String predicate : predicates)
                        statement.setString(++i, predicate);
                }
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        TTIriRef pred = iri(rs.getString("predicateIri"), rs.getString("predicate"));
                        String objIri = DALHelper.getNullableString(rs, "objectIri");
                        TTIriRef object = objIri == null ? null : iri(rs.getString("objectIri"), rs.getString("object"));
                        String literal = rs.getString("literal");
                        result.add(new Tpl()
                            .setDbid(rs.getInt("dbid"))
                            .setParent(DALHelper.getNullableInt(rs, "parent"))
                            .setPredicate(pred)
                            .setObject(object)
                            .setLiteral(literal)
                            .setFunctional(rs.getBoolean("functional"))
                        );
                    }
                }
            }
        }

        return result;
    }

    public List<TTIriRef> getActiveSubjectByObjectExcludeByPredicate(String objectIri, Integer rowNumber, Integer pageSize, String predicateIri) throws SQLException {
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
        if(rowNumber!=null && pageSize!=null)
            sql.add("LIMIT ? , ? ");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, objectIri);
                statement.setString(2, predicateIri);
                statement.setString(3, IM.INACTIVE.getIri());
                if(rowNumber!=null && pageSize!=null) {
                    statement.setInt(4, rowNumber);
                    statement.setInt(5, pageSize);
                }
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
                .add("LEFT JOIN entity sc ON sc.iri = o.scheme ")
                .add("WHERE s.iri = ?")
                .add("AND p.iri = ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, iri);
                statement.setString(2, predicate);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        ValueSetMember valueSetMember = new ValueSetMember()
                                .setEntity(iri(rs.getString("iri"),rs.getString("name")))
                                .setCode(rs.getString("code"));
                        if(rs.getString("schemeIri")!=null && rs.getString("schemeName")!=null)
                            valueSetMember.setScheme(iri(rs.getString("schemeIri"),rs.getString("schemeName")));
                        members.add(valueSetMember);
                    }
                }
            }
        }
        return members;
    }

    public Set<TTIriRef> getObjectIriRefsBySubjectAndPredicate(String iri, String predicate) throws SQLException {
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

    public List<TTIriRef> findImmediateParentsByIri(String iri, Integer rowNumber, Integer pageSize, boolean includeInactive) throws SQLException {
        List<TTIriRef> parents = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT o.iri, o.name, o.status")
                .add("FROM entity c ")
                .add("JOIN tpl t ON t.subject = c.dbid ")
                .add("JOIN entity p ON p.dbid = t.predicate AND p.iri IN(?, ?,?) ")
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
                statement.setString(++i, IM.IS_CHILD_OF.getIri());
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

    public List<Namespace> findNamespaces() throws SQLException {
        List<Namespace> namespaces = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT n.iri, n.prefix, n.name")
                .add("FROM namespace n ")
                .add("ORDER BY n.name ");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        if (rs.getString("name") != null) {
                            Namespace namespace = new Namespace(rs.getString("iri"), rs.getString("prefix"), rs.getString("name"));
                            namespaces.add(namespace);
                        }
                    }
                }
            }
        }
        return namespaces;
    }

}
