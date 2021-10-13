package org.endeavourhealth.imapi.model.tripletree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TTContext implements Serializable {
    public static final String OUTPUT_CONTEXT = "OUTPUT_CONTEXT";

    private final Map<String, String> byIri = new HashMap<>();
    private final Map<String, String> byPrefix = new HashMap<>();
    private final Map<String,String> toName= new HashMap<>();


    public List<TTPrefix> getPrefixes() {
        return byIri.entrySet().stream()
            .map(e -> new TTPrefix(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }
    public List<TTPrefix> getNameSpaces() {
        List<TTPrefix> prefixes= new ArrayList<>();
        for (Map.Entry<String,String> entry:byIri.entrySet()) {
            TTPrefix prefix = new TTPrefix()
              .setIri(entry.getKey())
              .setPrefix(entry.getValue())
              .setName(toName.get(entry.getKey()));
            prefixes.add(prefix);
        }
        return prefixes;
    }

    public void add(String iri, String prefix) {
        byIri.put(iri, prefix);
        byPrefix.put(prefix, iri);
    }
    public void add(String iri, String prefix,String name) {
        byIri.put(iri, prefix);
        byPrefix.put(prefix, iri);
        toName.put(iri,name);
    }

    public String prefix(String iri) {
        int end = Integer.max(iri.lastIndexOf("/"), iri.lastIndexOf("#"));
        String path = iri.substring(0, end + 1);
        String prefix = byIri.get(path);
        if (prefix == null)
            return iri;
        else
            if (end<iri.length()-1)
                return prefix + ":" + iri.substring(end + 1);
            else if(end == iri.length()-1)
                return prefix + ":";
            else return iri;
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
