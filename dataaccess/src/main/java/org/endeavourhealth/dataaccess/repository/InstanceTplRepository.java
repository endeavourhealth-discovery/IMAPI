package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.ConnectionPool;
import org.endeavourhealth.dataaccess.entity.TplInsObject;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.endeavourhealth.dataaccess.helpers.DALHelper.getNullableInt;
import static org.endeavourhealth.dataaccess.helpers.DALHelper.getNullableString;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class InstanceTplRepository {
	public List<TplInsObject> findAllBySubjectIri(String iri) throws SQLException {
        List<TplInsObject> result = new ArrayList<>();

        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT o.dbid, o.group_number, o.blank_node, p.iri AS p_iri, p.name AS p_name, c.iri AS c_iri, c.name AS c_name")
            .add("FROM instance i")
            .add("JOIN tpl_ins_object o ON o.subject = i.dbid")
            .add("JOIN entity p ON p.dbid = o.predicate")
            .add("LEFT JOIN entity c ON c.dbid = o.object")    // LEFT join for BNodes
            .add("WHERE i.iri = ?");

        try (Connection conn = ConnectionPool.get()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                stmt.setString(1, iri);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        TTIriRef object = (getNullableString(rs, "c_iri") == null) ? null : iri(rs.getString("c_iri"), rs.getString("c_name"));
                        result.add(new TplInsObject()
                            .setDbid(rs.getInt("dbid"))
                            .setGroup(rs.getInt("group_number"))
                            .setBnode(getNullableInt(rs, "blank_node"))
                            .setPredicate(iri(rs.getString("p_iri"), rs.getString("p_name")))
                            .setObject(object)
                        );
                    }
                }
            }
        }

        return result;
    }
}
