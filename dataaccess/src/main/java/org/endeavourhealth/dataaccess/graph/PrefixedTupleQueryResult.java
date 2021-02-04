package org.endeavourhealth.dataaccess.graph;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.Binding;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.impl.MapBindingSet;

import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class PrefixedTupleQueryResult implements AutoCloseable, Iterable<BindingSet>, Iterator<BindingSet> {
    private final PrefixedTupleQuery query;
    private final TupleQueryResult result;

    protected PrefixedTupleQueryResult(PrefixedTupleQuery query, TupleQueryResult result) {
        this.query = query;
        this.result = result;
    }

    @Override
    public void close() {
        this.result.close();
    }

    @Override
    public Iterator<BindingSet> iterator() {
        return this;
    }

    @Override
    public void forEach(Consumer<? super BindingSet> action) {
        while (hasNext()) {
            action.accept(next());
        }
    }

    @Override
    public Spliterator<BindingSet> spliterator() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return result.hasNext();
    }

    @Override
    public BindingSet next() {
        BindingSet bs = result.next();

        MapBindingSet result = new MapBindingSet();

        for(Binding x: bs) {
            if (x.getValue().isIRI()) {
                result.addBinding(x.getName(), prefixIri(x.getValue().stringValue()));
            } else
                result.addBinding(x);
        }

        return result;
    }

    private IRI prefixIri(String fullIri) {
        // Check if full iri
        if (!fullIri.startsWith("http://") && !fullIri.startsWith("https://"))
            return iri(fullIri);

        Optional<Namespace> namespace = query.namespaces.stream().filter(ns -> fullIri.startsWith(ns.getName())).findFirst();

        if (!namespace.isPresent())
            throw new IllegalStateException("Unknown namespace for [" + fullIri + "]");

        return SimpleValueFactory.getInstance().createIRI(fullIri.replace(namespace.get().getName(), namespace.get().getPrefix() + ":"));
    }
}
