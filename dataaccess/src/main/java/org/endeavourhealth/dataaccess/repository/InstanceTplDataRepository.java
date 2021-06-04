package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.ConnectionPool;
import org.endeavourhealth.dataaccess.entity.TplInsData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.endeavourhealth.dataaccess.helpers.DALHelper.getNullableInt;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class InstanceTplDataRepository {
    public List<TplInsData> findAllBySubjectIri(String iri) throws SQLException {
        List<TplInsData> result = new ArrayList<>();

        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT d.dbid, d.group_number, d.blank_node, p.iri AS p_iri, p.name AS p_name, c.iri AS c_iri, c.name AS c_name, d.literal")
            .add("FROM instance i")
            .add("JOIN tpl_ins_data d ON d.subject = i.dbid")
            .add("JOIN concept p ON p.dbid = d.predicate")
            .add("JOIN concept c ON c.dbid = d.data_type")
            .add("WHERE i.iri = ?")
            .add("ORDER BY group_number, blank_node, p.iri");

        try (Connection conn = ConnectionPool.get()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                stmt.setString(1, iri);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        result.add(new TplInsData()
                            .setDbid(rs.getInt("dbid"))
                            .setGroup(rs.getInt("group_number"))
                            .setBnode(getNullableInt(rs, "blank_node"))
                            .setPredicate(iri(rs.getString("p_iri"), rs.getString("p_name")))
                            .setDataType(iri(rs.getString("c_iri"), rs.getString("c_name")))
                            .setLiteral(rs.getString("literal"))
                        );
                    }
                }
            }
        }

        return result;
    }
}
