package com.endavourhealth.legacy.dal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.endavourhealth.legacy.models.Concept;
import com.endavourhealth.legacy.models.PagedResultSet;
import com.endavourhealth.legacy.models.Property;
import com.endavourhealth.legacy.models.RelatedConcept;
import com.endavourhealth.legacy.models.SchemeChild;
import com.endavourhealth.legacy.models.SchemeChildren;
import com.endavourhealth.legacy.models.SchemeCount;
import com.endavourhealth.legacy.models.ValueSetMember;
import com.fasterxml.jackson.databind.JsonNode;

public class ViewerJDBCDAL {
    private final String IS_A = "sn:116680003";
    private final String HAS_MEMBER = ":3521000252101";

    public List<JsonNode> getAxioms(String iri, Connection conn) throws SQLException, IOException {
        List<JsonNode> result = new ArrayList<>();

        String sql = "SELECT a.definition FROM concept c JOIN concept_axiom a ON a.concept = c.dbid WHERE c.iri = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, iri);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
//                    result.add(ObjectMapperPool.getInstance().readTree(rs.getString("definition")));
                }
            }
        }
        
        
        return result;
    }

    public PagedResultSet<RelatedConcept> getSources(String iri, List<String> relationships, int limit, int page, Connection conn) throws SQLException {
        String sql = "SELECT SQL_CALC_FOUND_ROWS DISTINCT\n" +
            "o.minCardinality, o.maxCardinality,\n" +
            "p.iri AS r_iri, p.name AS r_name, p.description AS r_description,\n" +
            "c.iri AS c_iri, c.name AS c_name, c.description AS c_description\n" +
            "FROM concept_property_object o\n" +
            "JOIN concept v ON v.dbid = o.object AND v.iri = ?\n" +
            "JOIN concept p ON p.dbid = o.property\n" +
            "JOIN concept c ON c.dbid = o.concept\n";

        if (relationships != null && !relationships.isEmpty())
            sql += "WHERE p.iri IN (" + DALHelper.inListParams(relationships.size()) + ")\n";

        if (limit > 0) {
            sql += "LIMIT ?";
            if (page > 0) {
                sql += ",?";
            }
        }

        PagedResultSet<RelatedConcept> result = new PagedResultSet<RelatedConcept>() 
            .setPage(page)
            .setPageSize(limit);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            stmt.setString(i++, iri);

            if (relationships != null)
                for (String relationship : relationships)
                    stmt.setString(i++, relationship);

            if (limit > 0) {
                if (page == 0) {
                    stmt.setInt(i++, limit);
                } else {
                    stmt.setInt(i++, (page - 1) * limit);
                    stmt.setInt(i++, limit);
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                result.setResult(Hydrator.createRelatedConceptList(rs));
            }
        }

        result.setTotalRecords(DALHelper.getCalculatedRows(conn));

        
        return result;
    }

    public PagedResultSet<RelatedConcept> getTargets(String iri, List<String> relationships, int limit, int page, Connection conn) throws SQLException {
        String sql = "SELECT SQL_CALC_FOUND_ROWS DISTINCT\n" +
            "o.minCardinality, o.maxCardinality,\n" +
            "p.iri AS r_iri, p.name AS r_name, p.description AS r_description,\n" +
            "v.iri AS c_iri, v.name AS c_name, c.description AS c_description\n" +
            "FROM concept_property_object o\n" +
            "JOIN concept c ON c.dbid = o.concept AND c.iri = ?\n" +
            "JOIN concept p ON p.dbid = o.property\n" +
            "JOIN concept v ON v.dbid = o.object\n";

        if (relationships != null && !relationships.isEmpty())
            sql += "WHERE p.iri IN (" + DALHelper.inListParams(relationships.size()) + ")\n";

        if (limit > 0) {
            sql += "LIMIT ?";
            if (page > 0) {
                sql += ",?";
            }
        }

        PagedResultSet<RelatedConcept> result = new PagedResultSet<RelatedConcept>()
            .setPage(page)
            .setPageSize(limit);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            stmt.setString(i++, iri);

            if (relationships != null)
                for (String relationship : relationships)
                    stmt.setString(i++, relationship);

            if (limit > 0) {
                if (page == 0) {
                    stmt.setInt(i++, limit);
                } else {
                    stmt.setInt(i++, (page - 1) * limit);
                    stmt.setInt(i++, limit);
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                result.setResult(Hydrator.createRelatedConceptList(rs));
            }
        }

        result.setTotalRecords(DALHelper.getCalculatedRows(conn));

        
        return result;
    }

    public Concept getConcept(String iri, Connection conn) throws SQLException {

        String sql = "SELECT iri, name, description FROM concept WHERE iri = ?";
//        Connection conn = driverManagerDataSource.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, iri);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next())
                    return null;
                else
                    return Hydrator.createConcept(rs);
            }
        }
    }

    public List<Concept> search(String term, String root, List<String> relationships, Connection conn) throws SQLException {
        String sql = "SELECT c.iri, c.name, c.description\n" +
            "FROM concept c\n" +
            "JOIN concept_tct tct ON tct.source = c.dbid AND tct.level > 0\n" +
            "JOIN concept p ON p.dbid = tct.property\n" +
            "JOIN concept t ON t.dbid = tct.target\n" +
            "WHERE c.name LIKE ?\n" +
            "AND t.iri = ?\n";

        if (relationships != null && !relationships.isEmpty())
            sql += "AND p.iri IN (" + DALHelper.inListParams(relationships.size()) + ")\n";

        sql += "ORDER BY LENGTH(c.name)\n" +
            "LIMIT 10";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            stmt.setString(i++, '%' + term + '%');
            stmt.setString(i++, root);

            if (relationships != null)
                for(String relationship: relationships)
                    stmt.setString(i++, relationship);

            try (ResultSet rs = stmt.executeQuery()) {
            	
                return Hydrator.createConceptList(rs);
            }
        }
    }

    public List<RelatedConcept> getTree(String iri, String root, List<String> relationships, Connection conn) throws SQLException {
        List<RelatedConcept> result = new ArrayList<>();

        if (iri.equals(root))
            return result;

        String sql = "SELECT null AS minCardinality, null AS maxCardinality,\n" +
            "p.iri AS r_iri, p.name AS r_name, p.description AS r_description,\n" +
            "t.iri AS c_iri, t.name AS c_name, c.description AS c_description\n" +
            "FROM concept c\n" +
            "JOIN concept_tct tct ON tct.source = c.dbid AND tct.level >= 0\n" +
            "JOIN concept p ON p.dbid = tct.property\n" +
            "JOIN concept t ON t.dbid = tct.target\n" +
            "WHERE c.iri = ?\n";

        if (relationships != null && !relationships.isEmpty())
            sql += "AND p.iri IN (" + DALHelper.inListParams(relationships.size()) + ")\n";

        sql += "GROUP BY tct.level\n" +
            "ORDER BY tct.level";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            stmt.setString(i++, iri);

            if (relationships != null)
                for(String relationship: relationships)
                    stmt.setString(i++, relationship);

            try(ResultSet rs = stmt.executeQuery()) {
                boolean rootFound = false;
                while (rs.next() && !rootFound) {
                    result.add(Hydrator.createRelatedConcept(rs));
                    rootFound = rs.getString("c_iri").equals(root);
                }
            }
        }

        
        return result;
    }

    // **************************************** DATA MODEL ****************************************
/*    public List<RelatedConcept> getDefinition(String iri) throws SQLException {
        String sql = "SELECT DISTINCT o.minCardinality, o.maxCardinality,\n" +
            "p.iri AS r_iri, p.name AS r_name, p.description AS r_description,\n" +
            "v.iri AS c_iri, v.name AS c_name, c.description AS c_description\n" +
            "FROM concept_property_object o\n" +
            "JOIN concept c ON c.dbid = o.concept AND c.iri = ?\n" +
            "JOIN concept p ON p.dbid = o.property\n" +
            "JOIN concept v ON v.dbid = o.object\n" +
            "WHERE NOT EXISTS (\n" +
            "\tSELECT 1 \n" +
            "    FROM concept_tct tct \n" +
            "    JOIN concept tt ON tt.iri IN ('sn:3021000252107', 'sn:3581000252102')\n" +
            "    WHERE tct.source = p.dbid AND tct.target = tt.dbid\n" +
            ");\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, iri);

            try(ResultSet rs = stmt.executeQuery()) {
                return Hydrator.createRelatedConceptList(rs);
            }
        }
    }*/

    public List<Property> getProperties(String iri, boolean inherited, Connection conn) throws SQLException, IOException {
        String sql = "SELECT p.iri AS p_iri, p.name AS p_name,\n" +
            "cpo.minCardinality AS min_cardinality,\n" +
            "cpo.maxCardinality AS max_cardinality,\n" +
            "v.iri as v_iri, v.name AS v_name,\n" +
            "-1 as level, c.iri as o_iri, c.name AS o_name\n" +
            "FROM concept c\n" +
            "JOIN concept_property_object cpo ON cpo.concept = c.dbid\n" +
            "JOIN concept p ON p.dbid = cpo.property\n" +
            "JOIN concept v ON v.dbid = cpo.object\n" +
            "JOIN concept_tct tct ON tct.source = cpo.property\n" +
            "JOIN concept t ON t.dbid = tct.target\n" +
            "WHERE c.iri = ?\n" +
            "AND t.iri IN ('owl:topObjectProperty', 'owl:topDataProperty')\n";

        if (inherited)
            sql += "UNION\n" +
                "SELECT p.iri AS p_iri, p.name AS p_name,\n" +
                "cpo.minCardinality AS min_cardinality,\n" +
                "cpo.maxCardinality AS max_cardinality,\n" +
                "v.iri as v_iri, v.name AS v_name,\n" +
                "i.level as level, h.iri as o_iri, h.name AS o_name\n" +
                "FROM concept c\n" +
                "JOIN concept_tct i ON i.source = c.dbid\n" +
                "JOIN concept_property_object cpo ON cpo.concept = i.target\n" +
                "JOIN concept h ON h.dbid = cpo.concept\n" +
                "JOIN concept p ON p.dbid = cpo.property\n" +
                "JOIN concept v ON v.dbid = cpo.object\n" +
                "JOIN concept_tct tct ON tct.source = cpo.property\n" +
                "JOIN concept t ON t.dbid = tct.target\n" +
                "WHERE c.iri = ?\n" +
                "AND t.iri IN ('owl:topObjectProperty', 'owl:topDataProperty')\n";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, iri);

            if (inherited)
                stmt.setString(2, iri);

            try(ResultSet rs = stmt.executeQuery()) {
            	
                return Hydrator.createPropertyList(rs);
            }
        }
    }

    // **************************************** VALUE SETS ****************************************

    public List<ValueSetMember> getValueSetMembers(String iri, Connection conn) throws SQLException, IOException {
        List<ValueSetMember> result = new ArrayList<>();

        String sql = "SELECT m.iri, m.name, m.code, IFNULL(operator.name,'or')  as operator\n" +
        		"FROM concept v\n" +
        		"JOIN concept p ON p.iri = ?\n" +
        		"JOIN concept_property_object cpo ON cpo.concept = v.dbid AND cpo.property = p.dbid\n" +
        		"JOIN concept m ON m.dbid = cpo.object\n" +
        		"LEFT OUTER JOIN concept operator ON cpo.operator = operator.dbid\n" +
        		"WHERE v.iri = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, HAS_MEMBER);
            stmt.setString(2, iri);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(new ValueSetMember()
                        .setIri(rs.getString("iri"))
                        .setName(rs.getString("name"))
                        .setCode(rs.getString("code"))
                        .setOperatorName(rs.getString("operator"))
                    );
                }
            }
        }
        
        return result;
    }

    public List<SchemeCount> getChildCountByScheme(String iri, Connection conn) throws SQLException {
        List<SchemeCount> result = new ArrayList<>();

        String sql = "SELECT scm.iri, scm.name AS scheme, COUNT(DISTINCT(s.dbid)) AS cnt\n" +
            "FROM concept c\n" +
            "JOIN concept p ON p.iri = ?\n" +
            "JOIN concept_tct t ON t.target = c.dbid AND t.property = p.dbid AND t.level > 0\n" +
            "JOIN concept s ON s.dbid = t.source\n" +
            "LEFT JOIN concept scm on scm.dbid = s.scheme\n" +
            "WHERE c.iri = ?\n" +
            "GROUP BY scm.dbid";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DALHelper.setString(stmt, 1, IS_A);
            DALHelper.setString(stmt, 2, iri);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(new SchemeCount()
                        .setIri(rs.getString("iri"))
                        .setName(rs.getString("scheme"))
                        .setCount(rs.getInt("cnt"))
                    );
                }
            }
        }

        
        return result;
    }

    public List<SchemeChildren> getChildren(String iri, String scheme, Connection conn) throws SQLException {
        List<SchemeChildren> result = new ArrayList<>();

        String sql = "SELECT DISTINCT scm.iri, scm.name AS scheme, s.iri AS childIri, s.name AS childName, s.code AS childCode\n" +
            "FROM concept c\n" +
            "JOIN concept p ON p.iri = ?\n" +
            "JOIN concept_tct t ON t.target = c.dbid AND t.property = p.dbid AND t.level > 0\n" +
            "JOIN concept s ON s.dbid = t.source\n" +
            "LEFT JOIN concept scm on scm.dbid = s.scheme\n" +
            "WHERE c.iri = ?\n";

        if (scheme != null && !scheme.isEmpty())
            sql += "AND scm.iri = ?\n";

        sql += "ORDER BY scm.name, s.name\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DALHelper.setString(stmt, 1, IS_A);
            DALHelper.setString(stmt, 2, iri);

            if (scheme != null && !scheme.isEmpty())
                DALHelper.setString(stmt, 3, scheme);

            try (ResultSet rs = stmt.executeQuery()) {
                SchemeChildren sc = new SchemeChildren()
                    .setIri(null)
                    .setName("No scheme");
                result.add(sc);

                while (rs.next()) {
                    if (sc.getIri() != null && !sc.getIri().equals(rs.getString("iri"))) {
                        sc = new SchemeChildren()
                            .setIri(rs.getString("iri"))
                            .setName(rs.getString("scheme"));
                        result.add(sc);
                    }
                    sc.addChild(new SchemeChild()
                        .setIri(rs.getString("childIri"))
                        .setName(rs.getString("childName"))
                        .setCode(rs.getString("childCode"))
                    );
                }
            }
        }

        
        return result;
    }
}
