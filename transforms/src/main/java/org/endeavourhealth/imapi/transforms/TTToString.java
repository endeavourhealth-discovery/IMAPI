package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;

import java.util.Map;
import java.util.zip.DataFormatException;

public class TTToString {
    private static String regex = "\\s\\(([^)]*)\\)[^(]*$";

    private static Map<String, String> setPredicateDefaults(Map<String, String> predicates) {
        if (predicates.containsKey(IM.IS_A.getIri())) predicates.replace(IM.IS_A.getIri(), "Is a");
        else predicates.put(IM.IS_A.getIri(), "Is a");
        if (predicates.containsKey(IM.ROLE_GROUP.getIri())) predicates.replace(IM.ROLE_GROUP.getIri(), "Where");
        else predicates.put(IM.ROLE_GROUP.getIri(), "Where");
        if (predicates.containsKey(OWL.EQUIVALENTCLASS.getIri())) predicates.replace(OWL.EQUIVALENTCLASS.getIri(), "Is equivalent to");
        else predicates.put(OWL.EQUIVALENTCLASS.getIri(), "Is equivalent to");
        if (predicates.containsKey(OWL.INTERSECTIONOF.getIri())) predicates.replace(OWL.INTERSECTIONOF.getIri(), "Combination of");
        else predicates.put(OWL.INTERSECTIONOF.getIri(), "Combination of");
        if (predicates.containsKey(OWL.SOMEVALUESFROM.getIri())) predicates.replace(OWL.SOMEVALUESFROM.getIri(), "With a value");
        else predicates.put(OWL.SOMEVALUESFROM.getIri(), "With a value");
        if (predicates.containsKey(OWL.ONPROPERTY.getIri())) predicates.replace(OWL.ONPROPERTY.getIri(), "On property");
        else predicates.put(OWL.ONPROPERTY.getIri(), "On property");
        return predicates;
    }

    public static String getBundleAsString(TTBundle bundle) {
        Map<String, String> predicates = bundle.getPredicates();
        predicates = setPredicateDefaults(predicates);
        String result = "";
        for (Map.Entry<TTIriRef, TTValue> element : bundle.getEntity().getPredicateMap().entrySet()) {
            result += ttValueToString(new TTNode().set(element.getKey(), element.getValue()), "object", predicates, 0);
        }
        return result;
    }

    public static String ttValueToString(TTValue node, String previousType, Map<String, String> iriMap, int indent) {
        if (indent == 0) iriMap = setPredicateDefaults(iriMap);
        if (node.isIriRef()) {
            return ttIriToString(node.asIriRef(), previousType, indent, false);
        } else if (node.isNode()) {
            return ttNodeToString(node.asNode(), previousType, indent, iriMap);
        } else if (node.isList()) {
            return ttArrayToString(node.asArray(), indent, iriMap);
        } else {
            return "";
        }
    }

    public static String ttIriToString(TTIriRef iri, String previous, int indent, boolean inline) {
        String pad = new String(new char[indent]).replace("\0", "  ");
        String result = "";
        if (!inline) result += pad;
        if (iri.getName() != null) result += iri.getName().replaceAll(regex, "");
        else result += iri.getIri();
        if (previous == "array") result += "\n";
        return result;
    }

    public static String ttNodeToString(TTNode node, String previousType, int indent, Map<String, String> iriMap) {
        if (indent == 0) iriMap = setPredicateDefaults(iriMap);
        String pad = new String(new char[indent]).replace("\0", "  ");
        String result = "";
        Boolean first = true;
        Boolean last = false;
        int totalKeys = node.getPredicateMap().size();
        int count = 1;
        Boolean group = false;
        if (totalKeys > 1) group = true;
        for (Map.Entry<TTIriRef, TTValue> element : node.getPredicateMap().entrySet()) {
            if (totalKeys == count) last = true;
            if (count == 1) first = true;
            String prefix = "";
            String suffix = "\n";
            if (group) {
                prefix = "  ";
                if (first) {
                    prefix = "( ";
                    first = false;
                }
                if (last) suffix = ")\n";
            }
            if (element.getValue().isIriRef()) {
                if (iriMap.containsKey(element.getKey().getIri())) {
                    result += pad + prefix + iriMap.get(element.getKey().getIri()).replaceAll(regex, "") + " : ";
                    result += ttIriToString(element.getValue().asIriRef(), "object", indent, true);
                    result += suffix;
                } else if (element.getKey().getName() != null) {
                    result += pad + prefix + element.getKey().getName().replaceAll(regex, "") + " : ";
                    result += ttIriToString(element.getValue().asIriRef(), "object", indent, true);
                    result += suffix;
                } else {
                    result += ttIriToString(element.getValue().asIriRef(), "object", indent, false);
                }
            } else {
                if (iriMap.containsKey(element.getKey().getIri())) result += pad + prefix + iriMap.get(element.getKey().getIri()).replaceAll(regex, "") + ":\n";
                else if (element.getKey().getName() != null) result += pad + prefix + element.getKey().getName().replaceAll(regex, "") + ":\n";
                if (previousType == "array") {
                    if (group) {
                        result += ttValueToString(element.getValue(), "object", iriMap, indent + 1);
                    } else {
                        result += ttValueToString(element.getValue(), "object", iriMap, indent);
                    }
                }
                if (previousType == "object") {
                    result += ttValueToString(element.getValue(), "object", iriMap, indent);
                }
            }
            count ++;
        }
        return result;
    }

    public static String ttArrayToString(TTArray arr, int indent, Map<String, String> iriMap) {
        if (indent == 0) iriMap = setPredicateDefaults(iriMap);
        String result = "";
        for (TTValue item : arr.getElements()) {
            result += ttValueToString(item, "array", iriMap, indent + 1);
        }
        return result;
    }
}
