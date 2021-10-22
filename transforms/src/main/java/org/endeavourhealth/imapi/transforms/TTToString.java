package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;

import java.util.Map;
import java.util.zip.DataFormatException;

public class TTToString {
    public static String getBundleAsString(TTBundle bundle) throws DataFormatException {
        Map<String, String> predicates = bundle.getPredicates();
        if (predicates.containsKey(IM.IS_A.getIri())) predicates.replace(IM.IS_A.getIri(), "Is a");
        if (predicates.containsKey(IM.ROLE_GROUP.getIri())) predicates.replace(IM.ROLE_GROUP.getIri(), "Where");
        if (predicates.containsKey(OWL.EQUIVALENTCLASS.getIri())) predicates.replace(OWL.EQUIVALENTCLASS.getIri(), "Is equivalent to");
        if (predicates.containsKey(OWL.INTERSECTIONOF.getIri())) predicates.replace(OWL.INTERSECTIONOF.getIri(), "Combination of");
        if (predicates.containsKey(OWL.SOMEVALUESFROM.getIri())) predicates.replace(OWL.SOMEVALUESFROM.getIri(), "With a value");
        if (predicates.containsKey(OWL.ONPROPERTY.getIri())) predicates.replace(OWL.ONPROPERTY.getIri(), "Is a");
        String result = "";
        result += ttValueToString(bundle.getEntity(), "object", predicates, 0);
        return result;
    }

    public static String ttValueToString(TTValue node, String previousType, Map<String, String> iriMap, int indent) throws DataFormatException {
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
        if (iri.getName() != null) result += iri.getName().replaceAll("/ *\\([^)]*\\) */g", "");
        else result += iri.getIri();
        if (previous == "array") result += "\n";
        return result;
    }

    public static String ttNodeToString(TTNode node, String previousType, int indent, Map<String, String> iriMap) throws DataFormatException {
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
            if (element.getValue().isNode() && element.getValue().asNode().has(TTIriRef.iri("@id"))) {
                if (iriMap.containsKey(element.getKey())) {
                    result += pad + prefix + iriMap.get(element.getKey()).replaceAll("/ *\\([^)]*\\) */g", "") + " : ";
                    result += ttIriToString(element.getValue().asIriRef(), "object", indent, true);
                    result += suffix;
                } else {
                    result += ttIriToString(element.getValue().asIriRef(), "object", indent, false);
                }
            } else {
                if (iriMap.containsKey(element.getKey())) result += pad + prefix + iriMap.get(element.getKey()).replaceAll("/ *\\([^)]*\\) */g", "") + ":\n";
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

    public static String ttArrayToString(TTArray arr, int indent, Map<String, String> iriMap) throws DataFormatException {
        String result = "";
        for (TTValue item : arr.getElements()) {
            result += ttValueToString(item, "array", iriMap, indent + 1);
        }
        return result;
    }
}
