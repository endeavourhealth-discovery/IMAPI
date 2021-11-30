package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionPool;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.dataaccess.helpers.JDBCHelper;
import org.endeavourhealth.imapi.model.EntitySummary;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.endeavourhealth.imapi.dataaccess.helpers.JDBCHelper.executeAndAddTriples;
import static org.endeavourhealth.imapi.dataaccess.helpers.JDBCHelper.inListParams;

@SuppressWarnings("java:S1192") // Disable "Literals as const" rule for SQL
public class EntityTripleRepositoryImpl implements EntityTripleRepository {

    @Override
    public TTBundle getEntityPredicates(String iri, Set<String> predicates, int limit) throws DALException {
        List<Tpl> triples = getTriplesRecursive(iri, predicates, limit);

        return Tpl.toBundle(iri, triples);
    }

    @Override
    public List<Tpl> getTriplesRecursive(String iri, Set<String> predicates, int limit) throws DALException {
        List<Tpl> result = new ArrayList<>();

        StringJoiner sql = new StringJoiner("\n")
            .add("WITH RECURSIVE triples AS (")
            .add("\tSELECT tpl.dbid, tpl.subject, tpl.blank_node AS parent, tpl.predicate, tpl.object, tpl.literal, tpl.functional")
            .add("\tFROM tpl")
            .add("\tJOIN entity e ON tpl.subject=e.dbid")
            .add("\tJOIN entity p ON p.dbid = tpl.predicate")
            .add("\tWHERE e.iri = ? ");
        if (predicates != null && !predicates.isEmpty())
            sql.add("\tAND p.iri IN " + inListParams(predicates.size()));
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
            .add("LEFT JOIN entity o ON t.object = o.dbid");
        if (limit > 0)
            sql.add("LIMIT ?");

        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                int i = 0;
                statement.setString(++i, iri);
                if (predicates != null && !predicates.isEmpty()) {
                    for (String predicate : predicates)
                        statement.setString(++i, predicate);
                }
                if (limit > 0)
                    statement.setInt(++i, limit);
                executeAndAddTriples(result, statement);
            }
        } catch (SQLException e) {
            throw new DALException("Error retrieving triples", e);
        }

        return result;
    }

    @Override
    public List<Tpl> getTriplesRecursiveByExclusions(String iri, Set<String> exclusionPredicates, int limit) throws DALException {
        List<Tpl> result = new ArrayList<>();

        StringJoiner sql = new StringJoiner("\n")
                .add("WITH RECURSIVE triples AS (")
                .add("\tSELECT tpl.dbid, tpl.subject, tpl.blank_node AS parent, tpl.predicate, tpl.object, tpl.literal, tpl.functional")
                .add("\tFROM tpl")
                .add("\tJOIN entity e ON tpl.subject=e.dbid")
                .add("\tJOIN entity p ON p.dbid = tpl.predicate")
                .add("\tWHERE e.iri = ? ");
        if (exclusionPredicates != null && !exclusionPredicates.isEmpty())
            sql.add("\tAND p.iri NOT IN " + inListParams(exclusionPredicates.size()));
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
                .add("LEFT JOIN entity o ON t.object = o.dbid");
        if (limit > 0)
            sql.add("LIMIT ?");

        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                int i = 0;
                statement.setString(++i, iri);
                if (exclusionPredicates != null && !exclusionPredicates.isEmpty()) {
                    for (String predicate : exclusionPredicates)
                        statement.setString(++i, predicate);
                }
                if (limit > 0)
                    statement.setInt(++i, limit);
                executeAndAddTriples(result, statement);
            }
        } catch (SQLException e) {
            throw new DALException("Error retrieving triples", e);
        }

        return result;
    }


    @Override
    public List<TTIriRef> getActiveSubjectByObjectExcludeByPredicate(String objectIri, Integer rowNumber, Integer pageSize, String predicateIri) throws DALException {
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
        if (rowNumber != null && pageSize != null)
            sql.add("LIMIT ? , ? ");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, objectIri);
                statement.setString(2, predicateIri);
                statement.setString(3, IM.INACTIVE.getIri());
                if (rowNumber != null && pageSize != null) {
                    statement.setInt(4, rowNumber);
                    statement.setInt(5, pageSize);
                }
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        usages.add(TTIriRef.iri(rs.getString("iri"), rs.getString("name")));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DALException("Failed getActiveSubjectByObjectExcludeByPredicate()", e);
        }
        return usages;
    }

    @Override
    public Integer getCountOfActiveSubjectByObjectExcludeByPredicate(String objectIri, String predicateIri) throws DALException {
        int usages = 0;
        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT DISTINCT s.iri, COUNT(1) as rowcount")
            .add("FROM tpl tpl")
            .add("JOIN entity o ON o.dbid = tpl.object ")
            .add("JOIN entity p ON p.dbid = tpl.predicate ")
            .add("JOIN entity s ON s.dbid = tpl.subject ")
            .add("WHERE o.iri = ?")
            .add("AND p.iri <> ?")
            .add("AND s.status <> ?")
            .add("GROUP BY s.iri");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, objectIri);
                statement.setString(2, predicateIri);
                statement.setString(3, IM.INACTIVE.getIri());
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        usages += rs.getInt("rowcount");
                    }
                }
            }
        } catch (Exception e) {
            throw new DALException("Failed getCountOfActiveSubjectByObjectExcludeByPredicate()", e);
        }
        return usages;
    }

    @Override
    public Set<ValueSetMember> getSubjectByObjectAndPredicateAsValueSetMembers(String iri, String predicate) throws DALException {
        Set<ValueSetMember> subsets = new HashSet<>();
        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT s.iri, s.name, s.code, s.iri AS schemeIri, n.name AS schemeName")
            .add("FROM tpl tpl")
            .add("JOIN entity o ON o.dbid = tpl.object")
            .add("JOIN entity p ON p.dbid = tpl.predicate")
            .add("JOIN entity s ON s.dbid = tpl.subject")
            .add("LEFT JOIN namespace n ON n.iri = s.scheme")
            .add("WHERE o.iri = ?")
            .add("AND p.iri = ?");
        try (Connection conn = ConnectionPool.get();
             PreparedStatement statement = conn.prepareStatement(sql.toString())) {
            executeAndAddMembers(iri, predicate, subsets, statement);
        } catch (SQLException e) {
            throw new DALException("Failed getSubjectByObjectAndPredicateAsValueSetMembers()", e);
        }
        return subsets;
    }

    @Override
    public Set<TTIriRef> getObjectIriRefsBySubjectAndPredicate(String iri, String predicate) throws DALException {
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
                        memberIriRefs.add(TTIriRef.iri(rs.getString("iri"), rs.getString("name")));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DALException("Failed getObjectIriRefsBySubjectAndPredicate()", e);
        }
        return memberIriRefs;
    }

    @Override
    public List<TTIriRef> findImmediateParentsByIri(String iri, Integer rowNumber, Integer pageSize, boolean includeInactive) throws DALException {
        List<TTIriRef> parents = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT o.iri, o.name, o.status")
                .add("FROM entity c ")
                .add("JOIN tpl t ON t.subject = c.dbid ")
                .add("JOIN entity p ON p.dbid = t.predicate AND p.iri IN(?, ?,?) ")
                .add("JOIN entity o ON o.dbid = t.object ")
                .add("WHERE c.iri = ?");
        if (!includeInactive)
            sql.add("AND o.status <> ?").add("AND o.iri <> ?");
        sql.add("ORDER BY o.name ");
        if (rowNumber != null && pageSize != null)
            sql.add("LIMIT ? , ? ");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                int i = 0;
                statement.setString(++i, RDFS.SUBCLASSOF.getIri());
                statement.setString(++i, IM.IS_CONTAINED_IN.getIri());
                statement.setString(++i, IM.IS_CHILD_OF.getIri());
                statement.setString(++i, iri);
                if (!includeInactive) {
                    statement.setString(++i, IM.INACTIVE.getIri());
                    statement.setString(++i, OWL.THING.getIri());
                }
                if (rowNumber != null && pageSize != null) {
                    statement.setInt(++i, rowNumber);
                    statement.setInt(++i, pageSize);
                }
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        parents.add(TTIriRef.iri(rs.getString("iri"), rs.getString("name")));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DALException("Failed to find parents", e);
        }
        return parents;
    }

    @Override
    public List<TTIriRef> findImmediateChildrenByIri(String iri,List<String> schemeIris, Integer rowNumber, Integer pageSize, boolean includeInactive) throws DALException {
        List<TTIriRef> children = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT s.iri, s.name")
                .add("FROM entity c ")
                .add("JOIN tpl t ON t.object = c.dbid ")
                .add("JOIN entity p ON p.dbid = t.predicate AND p.iri IN(?, ?, ?) ")
                .add("JOIN entity s ON s.dbid = t.subject ")
                .add("WHERE c.iri = ?");
        if(schemeIris != null && !schemeIris.isEmpty()){
            sql.add("AND s.scheme IN " + inListParams(schemeIris.size()));
        }
        if (!includeInactive)
            sql.add("AND s.status <> ?");
        sql.add("ORDER BY s.name, s.iri ");
        if (rowNumber != null && pageSize != null)
            sql.add("LIMIT ? , ? ");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                int i = 0;
                statement.setString(++i, RDFS.SUBCLASSOF.getIri());
                statement.setString(++i, IM.IS_CONTAINED_IN.getIri());
                statement.setString(++i, IM.IS_CHILD_OF.getIri());
                statement.setString(++i, iri);
                if(schemeIris != null && !schemeIris.isEmpty()){
                    for(String scheme : schemeIris){
                        statement.setString(++i,scheme);
                    }
                }
                if (!includeInactive)
                    statement.setString(++i, IM.INACTIVE.getIri());
                if (rowNumber != null && pageSize != null) {
                    statement.setInt(++i, rowNumber);
                    statement.setInt(++i, pageSize);
                }
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        children.add(TTIriRef.iri(rs.getString("iri"), rs.getString("name")));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DALException("Failed to find immediate children", e);
        }
        return children;
    }

    @Override
    public boolean hasChildren(String iri, boolean includeInactive) throws DALException {
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT 1")
                .add("FROM entity c ")
                .add("JOIN tpl t ON t.object = c.dbid ")
                .add("JOIN entity p ON p.dbid = t.predicate AND p.iri IN(?, ?, ?) ")
                .add("JOIN entity s ON s.dbid = t.subject ")
                .add("WHERE c.iri = ?");
        if (!includeInactive)
            sql.add("AND s.status <> ?");
        sql.add("LIMIT 1 ");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                int i = 0;
                statement.setString(++i, RDFS.SUBCLASSOF.getIri());
                statement.setString(++i, IM.IS_CONTAINED_IN.getIri());
                statement.setString(++i, IM.IS_CHILD_OF.getIri());
                statement.setString(++i, iri);
                if (!includeInactive)
                    statement.setString(++i, IM.INACTIVE.getIri());
                try (ResultSet rs = statement.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception e) {
            throw new DALException("Failed determine if concept has children", e);
        }
    }

    @Override
    public List<Namespace> findNamespaces() throws DALException {
        List<Namespace> namespaces = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT n.iri, n.prefix, n.name")
                .add("FROM namespace n ")
                .add("ORDER BY n.name ");
        try (Connection conn = ConnectionPool.get();
             PreparedStatement statement = conn.prepareStatement(sql.toString())) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    if (rs.getString("name") != null) {
                        Namespace namespace = new Namespace(rs.getString("iri"), rs.getString("prefix"), rs.getString("name"));
                        namespaces.add(namespace);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DALException("Failed to retrieve namespaces", e);
        }
        return namespaces;
    }

    public Collection<SimpleMap> getSubjectFromObjectPredicate(String iri, TTIriRef predicate) throws DALException {
        HashMap<String, SimpleMap> simpleMaps = new HashMap<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT e.iri, e.name, e.code, e.scheme ")
                .add("FROM tpl t ")
                .add("JOIN entity e ON e.dbid=t.subject")
                .add("WHERE object=(SELECT dbid FROM entity WHERE iri = ?) ")
                .add("AND predicate=(SELECT dbid FROM entity WHERE iri = ?) ");

        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                int i = 0;
                statement.setString(++i, iri);
                statement.setString(++i, predicate.getIri());

                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        simpleMaps.put(rs.getString("iri"), new SimpleMap(rs.getString("iri"), rs.getString("name"), rs.getString("code"), rs.getString("scheme")));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DALException("Failed to fetch subjects by object and predicate", e);
        }

        return simpleMaps.values();
    }

    @Override
    public Set<EntitySummary> getSubclassesAndReplacements(String iri) throws DALException {
        Set<EntitySummary> result = new HashSet<>();

        String sql = new StringJoiner(System.lineSeparator())
            .add("SELECT c.iri, c.name, c.code, s.iri AS schemeIri, s.name AS schemeName")
            .add("FROM entity c")
            .add("JOIN tct t ON t.descendant = c.dbid ")
            .add("JOIN entity p ON p.dbid = t.type AND p.iri = ?")
            .add("JOIN entity a ON a.dbid = t.ancestor")
            .add("LEFT JOIN namespace s ON s.iri = c.scheme")
            .add("WHERE a.iri = ?")
            .add("UNION")
            .add("SELECT r.iri, r.name, r.code, s.iri AS schemeIri, s.name AS schemeName")
            .add("FROM entity c")
            .add("JOIN tct t ON t.descendant = c.dbid ")
            .add("JOIN entity p ON p.dbid = t.type AND p.iri = ?")
            .add("JOIN entity a ON a.dbid = t.ancestor")
            .add("JOIN tpl ON tpl.object = c.dbid")
            .add("JOIN entity tp ON tp.dbid = tpl.predicate AND tp.iri = ?")
            .add("JOIN entity r ON r.dbid = tpl.subject")
            .add("LEFT JOIN namespace s ON s.iri = r.scheme")
            .add("WHERE a.iri = ?")
            .toString();

        try (Connection conn = ConnectionPool.get();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, RDFS.SUBCLASSOF.getIri());
            statement.setString(2, iri);
            statement.setString(3, RDFS.SUBCLASSOF.getIri());
            statement.setString(4, SNOMED.REPLACED_BY.getIri());
            statement.setString(5, iri);

            executeAndAddSummaries(result, statement);
        } catch (SQLException e) {
            throw new DALException("Failed getDescendantSummariesInclusive()", e);
        }

        return result;
    }

    @Override
    public Collection<EntitySummary> getSubjectAndDescendantSummariesByPredicateObjectRelType(String predicate, String object) throws DALException {
        List<TTIriRef> types = Arrays.asList(RDFS.SUBCLASSOF, SNOMED.REPLACED_BY);

        Set<EntitySummary> result = new HashSet<>();
        String sql = new StringJoiner("\n")
            .add("SELECT DISTINCT sd.iri, sd.name, sd.code, n.iri AS schemeIri, n.name AS schemeName")
            .add("FROM entity s")
            .add("JOIN tpl t ON t.subject = s.dbid")
            .add("JOIN tct pt ON pt.descendant = t.predicate")
            .add("JOIN entity p ON p.dbid = pt.ancestor AND p.iri = ?")
            .add("JOIN tct ot ON ot.descendant = t.object")
            .add("JOIN entity o ON o.dbid = ot.ancestor AND o.iri = ?")
            .add("JOIN tct st ON st.ancestor = s.dbid")
            .add("JOIN entity stt ON stt.dbid = st.type AND stt.iri IN " + inListParams(types.size()))
            .add("JOIN entity sd ON sd.dbid = st.descendant")
            .add("LEFT JOIN namespace n ON n.iri = sd.scheme")
            .toString();

        try (Connection conn = ConnectionPool.get();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            int i = 0;
            statement.setString(++i, predicate);
            statement.setString(++i, object);
            for (TTIriRef type : types) {
                statement.setString(++i, type.getIri());
            }

            executeAndAddSummaries(result, statement);
        } catch (SQLException e) {
            throw new DALException("Failed getSubjectAndDescendantSummariesByPredicateObjectRelType()", e);
        }
        return result;
    }

    @Override
    public Set<EntitySummary> getLegacyConceptSummaries(Set<EntitySummary> iris) throws DALException {
        int batchsize = 100;
        int remainder = iris.size() % batchsize;

        String baseSql = new StringJoiner(System.lineSeparator())
            .add("SELECT o.iri, o.name, o.code, s.iri AS schemeIri, s.name AS schemeName")
            .add("FROM entity e")
            .add("JOIN tpl t ON t.subject = e.dbid")
            .add("JOIN entity p ON p.dbid = t.predicate AND p.iri = ?")
            .add("JOIN entity o ON o.dbid = t.object")
            .add("LEFT JOIN namespace s ON s.iri = o.scheme")
            .toString();


        String batchSql = baseSql + " WHERE e.iri IN " + inListParams(batchsize);
        String remainSql = baseSql + ((remainder > 1) ? " WHERE e.iri IN " + inListParams(remainder) : " WHERE e.iri = ?");

        Set<EntitySummary> result = new HashSet<>();

        try (Connection conn = ConnectionPool.get();
             PreparedStatement batchStmt = conn.prepareStatement(batchSql);
             PreparedStatement remainStmt = conn.prepareStatement(remainSql)) {

            batchStmt.setString(1, IM.MATCHED_TO.getIri());
            remainStmt.setString(1, IM.MATCHED_TO.getIri());

            PreparedStatement statement = (iris.size() >= batchsize)
                ? batchStmt
                : remainStmt;

            int i = 0;
            for (EntitySummary iri : iris) {
                int b = i++ % batchsize;    // Index within batch

                statement.setString(2 + b, iri.getIri());

                if (b + 1 == batchsize || i > iris.size()) {
                    // End of a batch or whole list, so execute
                    executeAndAddSummaries(result, statement);

                    if (i + batchsize > iris.size())
                        statement = remainStmt;         // Out of batches into remainder
                }
            }

            return result;
        } catch (SQLException e) {
            throw new DALException("Failed getLegacyConceptSummaries()", e);
        }
    }

    private void executeAndAddSummaries(Set<EntitySummary> result, PreparedStatement statement) throws SQLException {
        try (ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                EntitySummary smry = new EntitySummary()
                    .setIri(rs.getString("iri"))
                    .setName(rs.getString("name"))
                    .setCode(rs.getString("code"));

                if (JDBCHelper.getNullableString(rs, "schemeIri") != null) {
                    smry.setScheme(TTIriRef.iri(rs.getString("schemeIri"), rs.getString("schemeName")));
                }

                result.add(smry);
            }
        }
    }

    private void executeAndAddMembers(String iri, String predicate, Set<ValueSetMember> members, PreparedStatement statement) throws SQLException {
        statement.setString(1, iri);
        statement.setString(2, predicate);
        try (ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                ValueSetMember valueSetMember = new ValueSetMember()
                    .setEntity(TTIriRef.iri(rs.getString("iri"), rs.getString("name")))
                    .setCode(rs.getString("code"));
                if (rs.getString("schemeIri") != null && rs.getString("schemeName") != null)
                    valueSetMember.setScheme(TTIriRef.iri(rs.getString("schemeIri"), rs.getString("schemeName")));
                members.add(valueSetMember);
            }
        }
    }
}
