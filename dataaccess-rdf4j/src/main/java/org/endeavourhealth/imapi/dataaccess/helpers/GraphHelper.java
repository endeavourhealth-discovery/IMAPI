package org.endeavourhealth.imapi.dataaccess.helpers;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.BindingSet;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;

import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class GraphHelper {
    private GraphHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static String getString(BindingSet bs, String s) {
        if (bs.hasBinding(s) && bs.getValue(s)!=null)
            return bs.getValue(s).stringValue();
        else
            return null;
    }

    public static String valueList(String param, Collection<String> iris)
    {
        if (iris == null || iris.isEmpty())
            return "";

        StringBuilder value = new StringBuilder();
        value.append("    VALUES ?").append(param).append(" {");
        for (String iri : iris) {
            IRI i = iri(iri);
            value.append(" <").append(i.stringValue()).append(">");
        }
        value.append("}");

        return value.toString();
    }

    public static String inList(String param, int size){
        List<String> q = new ArrayList<>(size);
        for(int i=0; i<size; i++){
            q.add("?" + param + i);
        }
        return "(" + String.join(",", q) + ")";
    }
    /**
     * Retrieves a set of IRIs from a node or array, including nested nodes
     * @param node to retrieve the IRIs from
     * @return a set of iris
     */
    public static Set<TTIriRef> getIrisFromNode(TTNode node){
        Set<TTIriRef> iris = new HashSet<>();
        return addToIrisFromNode(node,iris);
    }

    private static Set<TTIriRef> addToIrisFromNode(TTValue subject, Set<TTIriRef> iris){
        if (subject.isIriRef())
            iris.add(subject.asIriRef());
        else if (subject.isNode()){
            if (subject.asNode().getPredicateMap()!=null){
                for (Map.Entry<TTIriRef, TTArray> entry:subject.asNode().getPredicateMap().entrySet()){
                    iris.add(entry.getKey());
                    for (TTValue v:entry.getValue().getElements()){
                        if (v.isIriRef())
                            iris.add(v.asIriRef());
                        else if (v.isNode())
                            addToIrisFromNode(v,iris);
                    }
                }
            }
        }
        return iris;
    }

}
