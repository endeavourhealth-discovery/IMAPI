package org.endeavourhealth.imapi.logic.service;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.ods.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.imapi.dataaccess.SparqlConverter.getDefaultPrefixes;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.getString;

@Component
public class OdsService {

    public OdsResponse getOrganisationData(String odsCode) {
        if (odsCode == null || odsCode.isEmpty())
            return null;


        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            Organisation org = new Organisation();
            IRI iri = getOrganisation(conn, odsCode, org);

            if (iri == null)
                return null;

            getOrganisationLocation(conn, iri, org);
            getOrganisationRelationships(conn, iri, org);
            getOrganisationRoles(conn, iri, org);


            return new OdsResponse().setOrganisation(org);
        }
    }

    public OdsResponse getRoleData() {
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            return new OdsResponse().setRoles(getAllRoles(conn));
        }
    }

    private IRI getOrganisation(RepositoryConnection conn, String odsCode, Organisation org) {

        String sql = new StringJoiner("\n")
            .add(getDefaultPrefixes())
            .add("select *")
            .add("where {")
            .add("  ?iri rdf:type   im:Organisation ;")
            .add("       im:odsCode ?odsCode ;")
            .add("       rdfs:label ?name;")
            .add("       im:status  ?status;")
            .add("       im:organisationRecordClass ?recClass. ")
            .add("  ?recClass im:code ?recClassCode ")
            .add("}")
            .toString();

        TupleQuery qry = conn.prepareTupleQuery(sql);
        qry.setBinding("odsCode", literal(odsCode));
        try (TupleQueryResult rs = qry.evaluate()) {
            if (rs.hasNext()) {
                BindingSet bs = rs.next();

                org.setOrgId(new OrgId().setExtension(odsCode))
                    .setName(getString(bs, "name"))
                    .setStatus(getStatus(bs))
                    .setOrgRecordClass(getString(bs, "recClassCode"));

                return (IRI) bs.getValue("iri");
            }
        }

        return null;
    }

    private void getOrganisationLocation(RepositoryConnection conn, IRI iri, Organisation org) {
        String sql = new StringJoiner("\n")
            .add(getDefaultPrefixes())
            .add("select *")
            .add("where {")
            .add("  ?iri im:address ?address .")
            .add("  OPTIONAL { ?address im:addressLine1 ?addrln1 }")
            .add("  OPTIONAL { ?address im:addressLine2 ?addrln2 }")
            .add("  OPTIONAL { ?address im:addressLine3 ?addrln3 }")
            .add("  OPTIONAL { ?address im:locality ?town }")
            .add("  OPTIONAL { ?address im:region ?county }")
            .add("  OPTIONAL { ?address im:postCode ?postCode }")
            .add("  OPTIONAL { ?address im:country ?country }")
            .add("  OPTIONAL { ?address im:uprn ?uprn }")
            .add("}")
            .toString();

        TupleQuery qry = conn.prepareTupleQuery(sql);
        qry.setBinding("iri", iri);
        try (TupleQueryResult rs = qry.evaluate()) {
            if (rs.hasNext()) {
                BindingSet bs = rs.next();

                org.setGeoLoc(
                    new OrgGeoLocation()
                        .setLocation(
                            new OrgLocation()
                                .setAddrln1(getString(bs, "addrln1"))
                                .setAddrln2(getString(bs, "addrln2"))
                                .setAddrln3(getString(bs, "addrln3"))
                                .setTown(getString(bs, "town"))
                                .setCounty(getString(bs, "county"))
                                .setPostcode(getString(bs, "postCode"))
                                .setCountry(getString(bs, "country"))
                                .setUprn(getString(bs, "uprn"))
                        )
                );
            }
        }
    }

    private void getOrganisationRelationships(RepositoryConnection conn, IRI iri, Organisation org) {
        String sql = new StringJoiner("\n")
            .add(getDefaultPrefixes())
            .add("select *")
            .add("where {")
            .add("  ?iri im:organisationRelationship ?or.")
            .add("  ?or im:status ?status;")
            .add("      im:Concept ?rt;")
            .add("      im:targetOrganisation ?to .")
            .add("  ?rt im:code ?relCode .")
            .add("  ?to im:odsCode ?target .")
            .add("}")
            .toString();

        TupleQuery qry = conn.prepareTupleQuery(sql);
        qry.setBinding("iri", iri);
        try (TupleQueryResult rs = qry.evaluate()) {
            List<OrgRelationship> rel = new ArrayList<>();
            org.setRels(new OrgRelationships().setRel(rel));

            while (rs.hasNext()) {
                BindingSet bs = rs.next();

                rel.add(new OrgRelationship()
                    .setId(getString(bs, "relCode"))
                    .setStatus(getStatus(bs))
                    .setTarget(new OrgRelTarget().setOrgId(new OrgId().setExtension(getString(bs, "target"))))
                );
            }
        }
    }

    private void getOrganisationRoles(RepositoryConnection conn, IRI iri, Organisation org) {
        String sql = new StringJoiner("\n")
            .add(getDefaultPrefixes())
            .add("select *")
            .add("where {")
            .add("  ?iri im:organisationRole ?or.")
            .add("  ?or im:status ?status;")
            .add("      im:Concept ?rt .")
            .add("  ?rt im:code ?roleCode .")
            .add("}")
            .toString();

        TupleQuery qry = conn.prepareTupleQuery(sql);
        qry.setBinding("iri", iri);
        try (TupleQueryResult rs = qry.evaluate()) {
            List<OrgRole> role = new ArrayList<>();
            org.setRoles(new OrgRoles().setRole(role));

            while (rs.hasNext()) {
                BindingSet bs = rs.next();

                role.add(new OrgRole()
                    .setId(getString(bs, "roleCode"))
                    .setStatus(getStatus(bs))
                );
            }
        }
    }

    private String getStatus(BindingSet bs) {
        return IM.ACTIVE.getIri().equals(bs.getValue("status").stringValue())
            ? "Active"
            : "Inactive";
    }

    private List<OrgRole> getAllRoles(RepositoryConnection conn) {
        String sql = new StringJoiner("\n")
            .add(getDefaultPrefixes())
            .add("select *")
            .add("where {     ")
            .add("  ?role rdfs:subClassOf <https://directory.spineservices.nhs.uk/STU3/CodeSystem/ODSAPI-OrganizationRole-1#ODS_RoleType> ;")
            .add("        rdfs:label ?displayName ;")
            .add("        im:code ?id .")
            .add("}")
            .toString();

        TupleQuery qry = conn.prepareTupleQuery(sql);
        try (TupleQueryResult rs = qry.evaluate()) {
            List<OrgRole> role = new ArrayList<>();

            while (rs.hasNext()) {
                BindingSet bs = rs.next();

                role.add(new OrgRole()
                    .setId(getString(bs, "id"))
                    .setCode(getString(bs, "id").substring(2))
                    .setDisplayName(getString(bs, "displayName").replace(" (Organisation role)", ""))
                );
            }

            return role;
        }
    }
}
