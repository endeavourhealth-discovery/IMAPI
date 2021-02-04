package org.endeavourhealth.dataaccess.graph;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.impl.MapBindingSet;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.query.QueryStringUtil;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class PrefixedTupleQuery {
    public static PrefixedTupleQuery prepare(RepositoryConnection conn, String query) {
        return new PrefixedTupleQuery(conn, query);
    }

    public static IRI prefixIri(String iri) {
        return SimpleValueFactory.getInstance().createIRI(iri);
    }

    protected RepositoryConnection conn;
    private String query;
    Set<Namespace> namespaces = new HashSet<>();
    private MapBindingSet bindings = new MapBindingSet();

    protected PrefixedTupleQuery(RepositoryConnection conn, String query) {
        this.conn = conn;
        this.query = query;

        for(Namespace ns: conn.getNamespaces()) {
            this.namespaces.add(ns);
            this.query = "PREFIX " + ns.getPrefix() + ": <" + ns.getName() + ">\n" + this.query;
        }
    }

    public PrefixedTupleQuery bind(String param, Value value) {
        if (value.isIRI())
            bindings.addBinding(param, getFullIri(value.stringValue()));
        else
            bindings.addBinding(param, value);
        return this;
    }

    public PrefixedTupleQueryResult evaluate() {
        String qry = QueryStringUtil.getTupleQueryString(this.query, this.bindings);
        TupleQueryResult result = conn.prepareTupleQuery(qry).evaluate();
        return new PrefixedTupleQueryResult(this, result);
    }

    protected IRI getFullIri(String prefixedIri) {
        // Check if already full Iri
        if (prefixedIri.startsWith("http://") || prefixedIri.startsWith("https://"))
            return iri(prefixedIri);

        String[] parts = prefixedIri.split(":");

        Optional<Namespace> namespace = namespaces.stream().filter(ns -> ns.getPrefix().equals(parts[0])).findFirst();

        if (!namespace.isPresent())
            throw new IllegalStateException("Unknown namespace prefix [" + parts[0] + "]");

        return iri(namespace.get().getName() + parts[1]);
    }
}
