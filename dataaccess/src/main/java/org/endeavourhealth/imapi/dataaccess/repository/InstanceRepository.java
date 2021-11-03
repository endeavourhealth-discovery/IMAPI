package org.endeavourhealth.imapi.dataaccess.repository;

import org.endeavourhealth.imapi.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.dataaccess.helpers.DALHelper;
import org.endeavourhealth.imapi.model.report.SimpleCount;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.RDF;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class InstanceRepository extends BaseRepository{

    public List<Tpl> getTriplesRecursive(String iri, Set<String> predicates) throws SQLException {
        List<Tpl> result = new ArrayList<>();

        StringJoiner sql = new StringJoiner("\n")
                .add("WITH RECURSIVE triples AS (")
                .add("\tSELECT tpl.dbid, tpl.subject, tpl.blank_node AS parent, tpl.predicate, tpl.object, tpl.literal,tpl.functional, tpl.instance")
                .add("\tFROM inst_tpl tpl")
                .add("\tJOIN inst_entity e ON tpl.subject=e.dbid")
                .add("\tJOIN entity p ON p.dbid = tpl.predicate")
                .add("\tWHERE e.iri = ? ");
        if (predicates != null && !predicates.isEmpty())
            sql.add("\tAND p.iri IN " + inList(predicates.size())) ;
        sql.add("\tAND tpl.blank_node IS NULL")
                .add("UNION ALL")
                .add("\tSELECT t2.dbid, t2.subject, t2.blank_node AS parent, t2.predicate, t2.object, t2.literal ," +
                        "t2.functional, t2.instance")
                .add("\tFROM triples t")
                .add("\tJOIN inst_tpl t2 ON t2.subject = t.subject AND t2.blank_node= t.dbid")
                .add("\tWHERE t2.dbid <> t.dbid")
                .add(")")
                .add("SELECT t.dbid, t.parent, p.iri AS predicateIri, p.name AS predicate, " +
                        "case when ISNULL(i.iri) then o.iri else i.iri end AS objectIri, " +
                        "case when ISNULL(i.iri) then o.name else i.name end AS object, " +
                        "t.literal, t.functional")
                .add("FROM triples t")
                .add("JOIN entity p ON t.predicate = p.dbid")
                .add("LEFT JOIN entity o ON t.object = o.dbid")
                .add("LEFT JOIN inst_entity i ON t.instance = i.dbid;");

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

    public List<TTIriRef> search(String request, Set<String> types) throws SQLException {
        List<TTIriRef> result = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT i.iri, i.name, o.name as type")
                .add("FROM inst_tpl t")
                .add("JOIN entity p ON p.iri = ?")
                .add("JOIN inst_tpl f on f.subject = t.subject and f.predicate = p.dbid")
                .add("JOIN entity o on o.dbid = f.object")
                .add("JOIN inst_entity i on i.dbid = t.subject")
                .add("WHERE t.literal like ? ");
        if (types != null && types.size()!=0)
            sql.add("AND o.iri IN " + inList(types.size()));
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, RDF.TYPE.getIri());
                statement.setString(2, request + "%");
                int i = 3;
                if (types!=null && types.size()!=0){
                    for (String type : types){
                        statement.setString(i++, type);
                    }
                }
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        result.add(new TTIriRef(rs.getString("iri"),rs.getString("name"),rs.getString("type")));
                    }
                }
            }
        }
        return result;

    }

    public List<SimpleCount> getTypesCount() throws SQLException {
        List<SimpleCount> result = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("select t.iri,t.name, COUNT(*) as count")
                .add("from entity p")
                .add("join inst_tpl i on p.dbid = i.predicate")
                .add("join entity t on t.dbid = i.object")
                .add("where p.iri = ? ")
                .add("GROUP BY i.object");
        try(Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())){
                statement.setString(1, RDF.TYPE.getIri());
                try (ResultSet rs = statement.executeQuery()){
                    while (rs.next()) {
                        result.add(new SimpleCount(rs.getString("iri"), rs.getString("name"), rs.getInt("count")));
                    }
                }
            }
        }
        return result;
    }

}
