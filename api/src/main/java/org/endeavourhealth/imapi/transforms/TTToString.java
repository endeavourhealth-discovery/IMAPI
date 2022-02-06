package org.endeavourhealth.imapi.transforms;

import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.imapi.model.tripletree.*;

import java.util.List;
import java.util.Map;

public class TTToString {
    private TTToString() {
        throw new IllegalStateException("Utility class");
    }
    private static final String REGEX = "\\s\\(([^)]*)\\)[^(]*$";
    private static final String INDENT_SIZE = "  ";
    private static final String OBJECT = "object";
    private static final String ARRAY = "array";

    private static void setPredicateDefaults(Map<String, String> predicates, Map<String, String> defaultPredicates) {
        if(defaultPredicates == null){
            return;
        }
        for (Map.Entry<String, String> defaultPredicate : defaultPredicates.entrySet()) {
            if (predicates.containsKey(defaultPredicate.getKey())) predicates.replace(defaultPredicate.getKey(), defaultPredicate.getValue());
            else predicates.put(defaultPredicate.getKey(), defaultPredicate.getValue());
        }
    }

    public static String getBundleAsString(TTBundle bundle, Map<String, String> defaultPredicates, int indent, boolean withHyperlinks, List<String> blockedIris) {
        Map<String, String> predicates = bundle.getPredicates();
        setPredicateDefaults(predicates, defaultPredicates);
        StringBuilder result = new StringBuilder();
        for (Map.Entry<TTIriRef, TTArray> element : bundle.getEntity().getPredicateMap().entrySet()) {
            result.append(ttValueToString(new TTNode().set(element.getKey(), element.getValue()), OBJECT, predicates, indent, withHyperlinks, blockedIris));
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
            return ttNodeToString(node.asNode(), indent, withHyperlinks, iriMap, blockedIris);
        } else {
            return node.toString();
        }
    }

    public static String ttIriToString(TTIriRef iri, String previous, int indent, boolean withHyperlinks, boolean inline, List<String> blockedIris) {
        String pad = new String(new char[indent]).replace("\0", INDENT_SIZE);
        String result = "";
        if (!inline) result += pad;
        if (withHyperlinks && !blockedIris.contains(iri.getIri())) {
            String escapedUrl = iri.getIri().replace("/","%2F").replace("#", "%23");
            result += "<a href=\"/#/concept/" + escapedUrl + "\">";
        }
        if (iri.getName() != null) result += removeEndBrackets(iri.getName());
        else result += iri.getIri();
        if (withHyperlinks && !blockedIris.contains(iri.getIri())) result += "</a>";
        if (ARRAY.equals(previous)) result += "\n";
        return result;
    }

    public static String ttNodeToString(TTNode node, int indent, boolean withHyperlinks, Map<String, String> iriMap, List<String> blockedIris) {
        String pad = new String(new char[indent]).replace("\0", INDENT_SIZE);
        String result = "";
        boolean first = true;
        boolean last = false;
        int nodeIndent = indent;
        int totalKeys = node.getPredicateMap().size();
        int count = 1;
        boolean group = false;
        if (totalKeys > 1) group = true;
        for (Map.Entry<TTIriRef, TTArray> element : node.getPredicateMap().entrySet()) {
            if (totalKeys == count) last = true;
            if (count == 1) first = true;
            String prefix = "";
            String suffix = "\n";
            if (group) {
                nodeIndent = indent + 1;
                prefix = INDENT_SIZE;
                if (first) {
                    prefix = StringUtils.substring(INDENT_SIZE, 0, INDENT_SIZE.length() - 2) + "( " ;
                    first = false;
                }
                if (last) suffix = " )\n";
            }
            result = processNode(element.getKey(), element.getValue(), result, nodeIndent, iriMap, pad, prefix, suffix, group, last, withHyperlinks, blockedIris);
            count ++;
        }
        return result;
    }

        private static String processNode(TTIriRef key, TTArray value, String result, int indent, Map<String, String> iriMap, String pad, String prefix, String suffix, boolean group, boolean last, boolean withHyperlinks, List<String> blockedIris) {
            if (value.isIriRef()) {
                result += getObjectName(key, iriMap, pad, prefix);
                result += ttIriToString(value.asIriRef(), OBJECT, indent, withHyperlinks, true, blockedIris);
                result += suffix;
            } else if (value.isNode()) {
                result += getObjectName(key, iriMap, pad, prefix);
                result += "\n";
                result += ttValueToString(value, OBJECT, iriMap, indent, withHyperlinks, blockedIris);
                if (group && last && result.endsWith("\n")) result = result.substring(0, result.length() - 1) + " )" + result.substring(result.length() - 1);
                else if (group && last) result += " )\n";
            } else if (value.isLiteral()) {
                result += getObjectName(key, iriMap, pad, prefix);
                result += ttValueToString(value, OBJECT, iriMap, indent, withHyperlinks, blockedIris);
                result += "\n";
            } else {
                result += getObjectName(key, iriMap, pad, prefix);
                result += "\n";
                result += ttValueToString(value, OBJECT, iriMap, indent + 1, withHyperlinks, blockedIris);
                if (group && last && result.endsWith("\n")) result = result.substring(0, result.length() - 1) + " )" + result.substring(result.length() - 1);
                else if (group && last) result += " )\n";
            }
            return result;
        }

    private static String getObjectName(TTIriRef key, Map<String, String> iriMap, String pad, String prefix) {
        if (iriMap != null && iriMap.containsKey(key.getIri())) return pad + prefix + removeEndBrackets(iriMap.get(key.getIri())) + " : ";
        if (key.getName() != null && !"".equals(key.getName())) return pad + prefix + removeEndBrackets(key.getName()) + " : ";
        else return pad + prefix + key.getIri() + " : ";
    }

    private static String removeEndBrackets(String str) {
        return str.replaceAll(REGEX, "");
    }

    public static String ttArrayToString(TTArray arr, int indent, boolean withHyperlinks, Map<String, String> iriMap, List<String> blockedIris) {
        StringBuilder bld = new StringBuilder();
        for (TTValue item : arr.iterator()) {
            bld.append(ttValueToString(item, ARRAY, iriMap, indent, withHyperlinks, blockedIris));
        }
        return bld.toString();
    }
}
