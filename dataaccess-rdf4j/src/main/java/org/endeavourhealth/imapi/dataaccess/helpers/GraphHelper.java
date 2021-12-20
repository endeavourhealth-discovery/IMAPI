package org.endeavourhealth.imapi.dataaccess.helpers;

import org.eclipse.rdf4j.query.BindingSet;

import java.util.ArrayList;
import java.util.List;

public class GraphHelper {

    public static String getString(BindingSet bs, String s) {
        if (bs.hasBinding(s) && bs.getValue(s)!=null)
            return bs.getValue(s).stringValue();
        else
            return null;
    }

    public static String valueList(String param, int size)
    {
        List<String> binding = new ArrayList<>();
        for(int i=0; i<size; i++){
            binding.add("{ BIND(?" + param + i + " AS ?" + param + ") }");
        }
        return String.join(" UNION ", binding);
    }

    public static String inList(String param, int size){
        List<String> q = new ArrayList<>(size);
        for(int i=0; i<size; i++){
            q.add("?" + param + i);
        }
        return "(" + String.join(",", q) + ")";
    }
}
