package org.endeavourhealth.imapi.dataaccess.helpers;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.BindingSet;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class SparqlHelper {
  private SparqlHelper() {
    throw new IllegalStateException("Utility class");
  }

  public static String getString(BindingSet bs, String s) {
    if (bs.hasBinding(s) && bs.getValue(s) != null)
      return bs.getValue(s).stringValue();
    else
      return null;
  }

  public static String valueList(String param, Collection<String> iris) {
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

  public static String inList(String param, int size) {
    List<String> q = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      q.add("?" + param + i);
    }
    return "(" + String.join(",", q) + ")";
  }

  public static String addSparqlPrefixes(String sparql) {
    String prefixes = """
      PREFIX rdfs: <%s>
      PREFIX im: <%s>
      PREFIX rdf: <%s>
      PREFIX sn: <%s>
      PREFIX sh: <%s>
      PREFIX xsd: <%s>
      
      """.formatted(RDFS.NAMESPACE, IM.NAMESPACE, RDF.NAMESPACE, SNOMED.NAMESPACE, SHACL.NAMESPACE, XSD.NAMESPACE);
    StringJoiner sj = new StringJoiner(System.lineSeparator());
    sj.add(prefixes);
    sj.add(sparql);
    return sj.toString();
  }

}
