package org.endeavourhealth.imapi.filer.rdf4j;

import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.ValidatingValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.filer.TTEntityFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.TTFilerFactory;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.*;

public class TTEntityFilerRdf4j implements TTEntityFiler {
    private static final Logger LOG = LoggerFactory.getLogger(TTEntityFilerRdf4j.class);

    private RepositoryConnection conn;
    private final Map<String, String> prefixMap;
    private final Update deleteTriples;

    private static final ValueFactory valueFactory = new ValidatingValueFactory(SimpleValueFactory.getInstance());

    public TTEntityFilerRdf4j(RepositoryConnection conn, Map<String, String> prefixMap) {
        this.conn = conn;
        this.prefixMap = prefixMap;
        deleteTriples = conn.prepareUpdate("DELETE {?concept ?p1 ?o1.\n" +
                "        ?o1 ?p2 ?o2.\n" +
                "        ?o2 ?p3 ?o3.\n" +
                "        ?o3 ?p4 ?o4." +
                "        ?o4 ?p5 ?o5.}\n" +
                "where \n" +
                "    { GRAPH ?graph {?concept ?p1 ?o1.\n" +
                "    OPTIONAL {?o1 ?p2 ?o2.\n" +
                "        filter (isBlank(?o1))\n" +
                "        OPTIONAL { \n" +
                "            ?o2 ?p3 ?o3\n" +
                "            filter (isBlank(?o2))\n" +
                "            OPTIONAL {?o3 ?p4 ?o4.\n" +
                "                filter(isBlank(?o3))" +
                "                OPTIONAL {?o4 ?p5 ?o5" +
                "                    filter(isBlank(?o4))}}}\n" +
                "        }}}");
    }

    public TTEntityFilerRdf4j() {
        this(ConnectionManager.getIMConnection(), new HashMap<>());
    }

    @Override
    public void fileEntity(TTEntity entity, TTIriRef graph) throws TTFilerException {

        if (entity.get(RDFS.LABEL) != null) {
            if (entity.get(IM.HAS_STATUS) == null)
                entity.set(IM.HAS_STATUS, IM.ACTIVE);
            if (entity.get(IM.HAS_SCHEME) == null)
                entity.set(IM.HAS_SCHEME, graph);
        }
        if (entity.getCrud().equals(IM.UPDATE_PREDICATES))
            updatePredicates(entity, graph);
        else if (entity.getCrud().equals(IM.ADD_QUADS))
            addQuads(entity, graph);
        else if (entity.getCrud().equals(IM.UPDATE_ALL))
            replacePredicates(entity, graph);
        else if (entity.getCrud().equals(IM.DELETE_ALL))
            deleteTriples(entity, graph);
        else
            throw new TTFilerException("Entity " + entity.getIri() + " has no crud assigned");

    }

    @Override
    public void updateTct(String entity)  {
            StringJoiner delSupers = new StringJoiner("\n");
            delSupers.add("DELETE {<" + entity + "> <" + IM.IS_A.getIri() + "> ?super.}")
              .add("where { <" + entity + "> <" + IM.IS_A.getIri() + "> ?super.}");
            Update deleteIsas = conn.prepareUpdate(delSupers.toString());
            deleteIsas.execute();
            StringJoiner delSubs= new StringJoiner("\n");
            delSubs
              .add("DELETE {?subentity <" + IM.IS_A.getIri() + "> <" + entity + ">.}")
              .add("where { ?subentity <" + IM.IS_A.getIri() + "> <" + entity + ">.}");
            deleteIsas = conn.prepareUpdate(delSubs.toString());
            deleteIsas.execute();
            String[] topConcepts = {"<http://snomed.info/sct#138875005>", "<" + IM.NAMESPACE + "Concept>"};
            String blockers = String.join(",", topConcepts);
            StringJoiner isaSame = new StringJoiner("\n");
            isaSame.add("INSERT DATA {<" + entity + "> <" + IM.IS_A.getIri() + "> <" + entity + ">.}");
            Update addIsas = conn.prepareUpdate(isaSame.toString());
            addIsas.execute();
            StringJoiner addSuper= new StringJoiner("\n");
            addSuper
              .add("INSERT {<" + entity + "> <" + IM.IS_A.getIri() + "> ?superType.}")
              .add("where { <" + entity + "> <" + RDFS.SUBCLASSOF.getIri() + "> ?superType.}");
            addIsas= conn.prepareUpdate(addSuper.toString());
            addIsas.execute();
            StringJoiner addAncestors= new StringJoiner("\n");
            addAncestors
          .add("INSERT {<" + entity + "> <" + IM.IS_A.getIri() + "> ?ancestor.}")
          .add("where { <" + entity + "> <" + RDFS.SUBCLASSOF.getIri() + "> ?superType." +
            "          ?supertype <"+ IM.IS_A.getIri()+"> ?ancestor}");
        addIsas= conn.prepareUpdate(addAncestors.toString());
        addIsas.execute();
            StringJoiner isSubs= new StringJoiner("\n");
            isSubs
              .add(" INSERT { ?subentity <http://endhealth.info/im#isA> ?superentity.}")
              .add("where {?subentity rdfs:subClassOf+ <"+ entity+">.\n")
              .add("<"+entity+"> <http://endhealth.info/im#isA> ?superentity.")
              .add("filter (?subentity not in (" + blockers + "))")
              .add("filter (?superentity not in (" + blockers + "))}");
            addIsas = conn.prepareUpdate(isSubs.toString());
            addIsas.execute();

    }


    private void replacePredicates(TTEntity entity, TTIriRef graph) throws TTFilerException {
        deleteTriples(entity, graph);
        addQuads(entity, graph);
    }

    private void addQuads(TTEntity entity, TTIriRef graph) throws TTFilerException {
        try {
            ModelBuilder builder = new ModelBuilder();
            builder = builder.namedGraph(graph.getIri());
            for (Map.Entry<TTIriRef, TTArray> entry : entity.getPredicateMap().entrySet()) {
                addTriple(builder, toIri(entity.getIri()), toIri(entry.getKey().getIri()), entry.getValue());
            }
            conn.add(builder.build());
        } catch (RepositoryException | TTFilerException e) {
            throw new TTFilerException("Failed to file entities", e);
        }

    }

    private void deleteTriples(TTEntity entity, TTIriRef graph) throws TTFilerException {
        try {
            deleteTriples.setBinding("concept", valueFactory.createIRI(entity.getIri()));
            deleteTriples.setBinding("graph", valueFactory.createIRI(graph.getIri()));
            deleteTriples.execute();
        } catch (Exception e) {
            throw new TTFilerException("Failed to delete triples : "+ e.getMessage());
        }

    }


    private void deletePredicates(TTEntity entity, TTIriRef graph) throws TTFilerException {
        StringBuilder predList = new StringBuilder();
        int i = 0;
        Map<TTIriRef, TTArray> predicates = entity.getPredicateMap();
        for (Map.Entry<TTIriRef, TTArray> po : predicates.entrySet()) {
            String predicateIri = po.getKey().getIri();
            i++;
            if (i > 1)
                predList.append(", ");
            predList.append("<").append(predicateIri).append(">");
        }
        String spq = "DELETE {?concept ?p1 ?o1.\n" +
                "        ?o1 ?p2 ?o2.\n" +
                "        ?o2 ?p3 ?o3.\n" +
                "        ?o3 ?p4 ?o4.}\n" +
                "where { graph <" + graph.getIri() + "> {\n" +
                "    {?concept ?p1 ?o1.\n" +
                "    filter(?p1 in(" + predList + "))\n" +
                "    OPTIONAL {?o1 ?p2 ?o2.\n" +
                "        filter (isBlank(?o1))\n" +
                "        OPTIONAL { \n" +
                "            ?o2 ?p3 ?o3\n" +
                "            filter (isBlank(?o2))\n" +
                "            OPTIONAL {?o3 ?p4 ?o4.\n" +
                "                filter(!isBlank(?o3))}}\n" +
                "        }} }}\n";
        Update deletePredicates = conn.prepareUpdate(spq);
        deletePredicates.setBinding("concept", valueFactory.createIRI(entity.getIri()));
        try {
            deletePredicates.execute();
        } catch (RepositoryException e) {
            throw new TTFilerException("Failed to delete triples");
        }

    }

    private void updatePredicates(TTEntity entity, TTIriRef graph) throws TTFilerException {

        //Deletes the previous predicate values and adds in the new ones
        deletePredicates(entity, graph);
        addQuads(entity, graph);
    }

    private void addTriple(ModelBuilder builder, Resource subject, IRI predicate, TTArray array) throws TTFilerException {
        for (TTValue value : array.iterator()) {
            addTriple(builder, subject, predicate, value);
        }
    }

    private void addTriple(ModelBuilder builder, Resource subject, IRI predicate, TTValue value) throws TTFilerException {
        if (value.isLiteral()) {
            if (null != value.asLiteral().getValue())
                builder.add(subject, predicate, value.asLiteral().getType() == null
                        ? literal(value.asLiteral().getValue())
                        : literal(value.asLiteral().getValue(), toIri(value.asLiteral().getType().getIri())));
        } else if (value.isIriRef()) {
            builder.add(subject, predicate, toIri(value.asIriRef().getIri()));
        } else if (value.isNode()) {
            TTNode node = value.asNode();
            BNode bNode = bnode();
            builder.add(subject, predicate, bNode);
            for (Map.Entry<TTIriRef, TTArray> entry : node.getPredicateMap().entrySet()) {
                addTriple(builder, bNode, toIri(entry.getKey().getIri()), entry.getValue());
            }
        } else {
            throw new TTFilerException("Arrays of arrays not allowed ");
        }
    }

    private IRI toIri(String iri) throws TTFilerException {
        iri = expand(iri);
        if(iri.startsWith("urn")){
            return iri(iri);
        }

        try {

            String decodedURL = URLDecoder.decode(iri, StandardCharsets.UTF_8);
            URL url = new URL(decodedURL);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            String result = uri.toASCIIString();


            if (!iri.equals(result))
                LOG.trace("Encoded iri [{}] => [{}]", iri, result);

            return iri(result);
        } catch (MalformedURLException | URISyntaxException e) {
            throw new TTFilerException("Unable to encode iri", e);
        }
    }

    public String expand(String iri) {
        if (prefixMap == null)
            return iri;
        try {
            int colonPos = iri.indexOf(":");
            String prefix = iri.substring(0, colonPos);
            String path = prefixMap.get(prefix);
            if (path == null)
                return iri;
            else
                return path + iri.substring(colonPos + 1);
        } catch (StringIndexOutOfBoundsException e) {
            LOG.debug("invalid iri [{}]", iri);
            return null;
        }
    }

}
