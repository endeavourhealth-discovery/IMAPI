package com.endavourhealth.legacy.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.endavourhealth.legacy.models.Concept;
import com.endavourhealth.legacy.models.Property;
import com.endavourhealth.legacy.models.RelatedConcept;

public class Hydrator {
    // CONCEPTS
    public static List<Concept> createConceptList(ResultSet rs) throws SQLException {
        List<Concept> result = new ArrayList<>();
        while (rs.next())
            result.add(createConcept(rs));

        return result;
    }

    public static Concept createConcept(ResultSet rs) throws SQLException {
        return populate(new Concept(), rs);
    }

    public static Concept populate(Concept c, ResultSet rs) throws SQLException {
        return c
            .setIri(DALHelper.getString(rs, "iri"))
            .setName(DALHelper.getString(rs, "name"))
            .setDescription(DALHelper.getString(rs, "description"));
    }

    // RELATED CONCEPTS
    public static List<RelatedConcept> createRelatedConceptList(ResultSet rs) throws SQLException {
        List<RelatedConcept> result = new ArrayList<>();
        while (rs.next())
            result.add(createRelatedConcept(rs));

        return result;
    }

    public static RelatedConcept createRelatedConcept(ResultSet rs) throws SQLException {
        return populate(new RelatedConcept(), rs);
    }

    public static RelatedConcept populate(RelatedConcept c, ResultSet rs) throws SQLException {
        return c
            .setMinCardinality(DALHelper.getInt(rs, "minCardinality"))
            .setMaxCardinality(DALHelper.getInt(rs, "maxCardinality"))
            .setRelationship(new Concept()
                .setIri(rs.getString("r_iri"))
                .setName(rs.getString("r_name"))
                .setDescription(rs.getString("r_description"))
            )
            .setConcept(
                new Concept()
                    .setIri(rs.getString("c_iri"))
                    .setName(rs.getString("c_name"))
                    .setDescription(rs.getString("c_description"))
            );
    }

    // PROPERTIES
    public static List<Property> createPropertyList(ResultSet rs) throws SQLException {
        List<Property> result = new ArrayList<>();
        while (rs.next())
            result.add(createProperty(rs));

        return result;
    }

    public static Property createProperty(ResultSet rs) throws SQLException {
        return populate(new Property(), rs);
    }

    public static Property populate(Property c, ResultSet rs) throws SQLException {
        return c
            .setProperty(new Concept(rs.getString("p_iri"), rs.getString("p_name")))
            .setMinCardinality(rs.getInt("min_cardinality"))
            .setMaxCardinality(rs.getInt("max_cardinality"))
            .setValueType(new Concept(rs.getString("v_iri"), rs.getString("v_name")))
            .setLevel(rs.getInt("level"))
            .setOwner(new Concept(rs.getString("o_iri"), rs.getString("o_name")));
    }
}
