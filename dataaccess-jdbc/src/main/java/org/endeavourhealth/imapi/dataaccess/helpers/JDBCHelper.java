package org.endeavourhealth.imapi.dataaccess.helpers;

import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.sql.*;
import java.util.Collections;
import java.util.List;

import static java.sql.Types.*;

public class JDBCHelper {
    private JDBCHelper() {}

    public static void setString(PreparedStatement stmt, int i, String value) {
        try {
            if (value == null)
                stmt.setNull(i, VARCHAR);
            else
                stmt.setString(i, value);
        } catch (SQLException e) {
            throw new DALException("Error setting STRING value", e);
        }
    }

    public static String inListParams(int size) {
        List<String> q = Collections.nCopies(size, "?");
        return "(" + String.join(",", q) + ")";
    }

    // GET methods
    public static Integer getNullableInt(ResultSet rs, String field) throws SQLException {
        int result = rs.getInt(field);
        return rs.wasNull() ? null : result;
    }

    public static String getNullableString(ResultSet rs, String field) throws SQLException {
        String result = rs.getString(field);
        return rs.wasNull() ? null : result;
    }

    public static void executeAndAddTriples(List<Tpl> result, PreparedStatement statement) throws SQLException {
        try (ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                TTIriRef pred = TTIriRef.iri(rs.getString("predicateIri"), rs.getString("predicate"));
                String objIri = JDBCHelper.getNullableString(rs, "objectIri");
                TTIriRef object = objIri == null ? null : TTIriRef.iri(rs.getString("objectIri"), rs.getString("object"));
                String literal = rs.getString("literal");
                result.add(new Tpl()
                    .setDbid(rs.getInt("dbid"))
                    .setParent(JDBCHelper.getNullableInt(rs, "parent"))
                    .setPredicate(pred)
                    .setObject(object)
                    .setLiteral(literal)
                    .setFunctional(rs.getBoolean("functional"))
                );
            }
        }
    }
}
