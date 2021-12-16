package org.endeavourhealth.imapi.dataaccess.helpers;

import org.eclipse.rdf4j.query.BindingSet;

import java.util.ArrayList;
import java.util.List;

public class GraphHelper {
    private GraphHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static String inListParams(String param, int size) {
        List<String> q = new ArrayList<>(size);
        for(int i=0; i<size; i++){
            q.add("?" + param + i);
        }
        return "(" + String.join(",", q) + ")";
    }

    public static String getString(BindingSet bs, String s) {
        if (bs.hasBinding(s) && bs.getValue(s)!=null)
            return bs.getValue(s).stringValue();
        else
            return null;
    }
}
