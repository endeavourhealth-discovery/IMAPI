package org.endeavourhealth.imapi.model.tripletree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TTContext {
    public static String OUTPUT_CONTEXT = "OUTPUT_CONTEXT";

    private Map<String, String> byIri = new HashMap<>();
    private Map<String, String> byPrefix = new HashMap<>();


    public List<TTPrefix> getPrefixes() {
        return byIri.entrySet().stream()
            .map((e) -> new TTPrefix(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }

    public void add(String iri, String prefix) {
        byIri.put(iri, prefix);
        byPrefix.put(prefix, iri);
    }

    public String prefix(String iri) {
        int end = Integer.max(iri.lastIndexOf("/"), iri.lastIndexOf("#"));
        String path = iri.substring(0, end + 1);
        String prefix = byIri.get(path);
        if (prefix == null)
            return iri;
        else
            return prefix + ":" + iri.substring(end + 1);
    }

    public String expand(String iri) {
        int colonPos = iri.indexOf(":");
        String prefix = iri.substring(0, colonPos);
        String path = byPrefix.get(prefix);
        if (path == null)
            return iri;
        else
            return path + iri.substring(colonPos + 1);
    }
}
