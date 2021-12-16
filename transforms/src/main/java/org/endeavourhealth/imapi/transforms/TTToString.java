package org.endeavourhealth.imapi.transforms;

import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.imapi.model.tripletree.*;

import java.util.List;
import java.util.Map;

public class TTToString {
    private static final String regex = "\\s\\(([^)]*)\\)[^(]*$";
    private static final String indentSize = "  ";

    private static Map<String, String> setPredicateDefaults(Map<String, String> predicates, Map<String, String> defaultPredicates) {
        if(defaultPredicates == null){
            return predicates;
        }
        for (Map.Entry<String, String> defaultPredicate : defaultPredicates.entrySet()) {
            if (predicates.containsKey(defaultPredicate.getKey())) predicates.replace(defaultPredicate.getKey(), defaultPredicate.getValue());
            else predicates.put(defaultPredicate.getKey(), defaultPredicate.getValue());
        }
        return predicates;
    }

    public static String getBundleAsString(TTBundle bundle, Map<String, String> defaultPredicates, int indent, boolean withHyperlinks, List<String> blockedIris) {
        Map<String, String> predicates = bundle.getPredicates();
        predicates = setPredicateDefaults(predicates, defaultPredicates);
        StringBuilder result = new StringBuilder();
        for (Map.Entry<TTIriRef, TTArray> element : bundle.getEntity().getPredicateMap().entrySet()) {
            result.append(ttValueToString(new TTNode().set(element.getKey(), element.getValue()), "object", predicates, indent, withHyperlinks, blockedIris));
        }
        return result.toString();
    }

    public static String ttValueToString(TTArray node, String previousType, Map<String, String> iriMap, int indent, boolean withHyperlinks, List<String> blockedIris) {
        return ttArrayToString(node, indent, withHyperlinks, iriMap, blockedIris);
    }

    public static String ttValueToString(TTValue node, String previousType, Map<String, String> iriMap, int indent, boolean withHyperlinks, List<String> blockedIris) {
        if (node.isIriRef()) {
            return ttIriToString(node.asIriRef(), previousType, indent, withHyperlinks, false, blockedIris);
        } else if (node.isNode()) {
            return ttNodeToString(node.asNode(), previousType, indent, withHyperlinks, iriMap, blockedIris);
        } else {
            return node.toString();
        }
    }

    public static String ttIriToString(TTIriRef iri, String previous, int indent, boolean withHyperlinks, boolean inline, List<String> blockedIris) {
        String pad = new String(new char[indent]).replace("\0", indentSize);
        String result = "";
        if (!inline) result += pad;
        if (withHyperlinks && !blockedIris.contains(iri.getIri())) {
            String escapedUrl = iri.getIri().replaceAll("/","%2F").replaceAll("#", "%23");
            String rootUrl = System.getenv("API_URL");
            result += "<a href=\"" + rootUrl + "/#/concept/" + escapedUrl + "\">";
        }
        if (iri.getName() != null) result += removeEndBrackets(iri.getName());
        else result += iri.getIri();
        if (withHyperlinks && !blockedIris.contains(iri.getIri())) result += "</a>";
        if ("array".equals(previous)) result += "\n";
        return result;
    }

    public static String ttNodeToString(TTNode node, String previousType, int indent, boolean withHyperlinks, Map<String, String> iriMap, List<String> blockedIris) {
        String pad = new String(new char[indent]).replace("\0", indentSize);
        String result = "";
        boolean first = true;
        boolean last = false;
        int nodeIndent = indent;
        int totalKeys = node.getPredicateMap().size();
        int count = 1;
        Boolean group = false;
        if (totalKeys > 1) group = true;
        for (Map.Entry<TTIriRef, TTArray> element : node.getPredicateMap().entrySet()) {
            if (totalKeys == count) last = true;
            if (count == 1) first = true;
            String prefix = "";
            String suffix = "\n";
            if (group) {
                nodeIndent = indent + 1;
                prefix = indentSize;
                if (first) {
                    prefix = StringUtils.substring(indentSize, 0, indentSize.length() - 2) + "( " ;
                    first = false;
                }
                if (last) suffix = " )\n";
            }
            result = processNode(element.getKey().getIri(), element.getValue(), result, nodeIndent, iriMap, pad, prefix, suffix, group, last, withHyperlinks, blockedIris);
            count ++;
        }
        return result;
    }

        private static String processNode(String key, TTArray value, String result, int indent, Map<String, String> iriMap, String pad, String prefix, String suffix, Boolean group, Boolean last, boolean withHyperlinks, List<String> blockedIris) {
            if (value.isIriRef()) {
                result += getObjectName(key, iriMap, pad, prefix);
                result += ttIriToString(value.asIriRef(), "object", indent, withHyperlinks, true, blockedIris);
                result += suffix;
            } else if (value.isNode()) {
                result += getObjectName(key, iriMap, pad, prefix);
                result += "\n";
                result += ttValueToString(value, "object", iriMap, indent, withHyperlinks, blockedIris);
                if (group && last && result.endsWith("\n")) result = result.substring(0, result.length() - 1) + " )" + result.substring(result.length() - 1);
                else if (group && last) result += " )\n";
            } else if (value.isLiteral()) {
                result += getObjectName(key, iriMap, pad, prefix);
                result += ttValueToString(value, "object", iriMap, indent, withHyperlinks, blockedIris);
                result += "\n";
            } else {
                result += getObjectName(key, iriMap, pad, prefix);
                result += "\n";
                result += ttValueToString(value, "object", iriMap, indent + 1, withHyperlinks, blockedIris);
                if (group && last && result.endsWith("\n")) result = result.substring(0, result.length() - 1) + " )" + result.substring(result.length() - 1);
                else if (group && last) result += " )\n";
            }
            return result;
        }

    private static String getObjectName(String key, Map<String, String> iriMap, String pad, String prefix) {
        if (iriMap != null && iriMap.containsKey(key)) return pad + prefix + removeEndBrackets(iriMap.get(key)) + " : ";
        else return pad + prefix + key + " : ";
    }

    private static String removeEndBrackets(String str) {
        return str.replaceAll(regex, "");
    }

    public static String ttArrayToString(TTArray arr, int indent, boolean withHyperlinks, Map<String, String> iriMap, List<String> blockedIris) {
        String result = "";
        for (TTValue item : arr.iterator()) {
            result += ttValueToString(item, "array", iriMap, indent, withHyperlinks, blockedIris);
        }
        return result;
    }
}
