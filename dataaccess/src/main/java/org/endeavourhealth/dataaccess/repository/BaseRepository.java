package org.endeavourhealth.dataaccess.repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class BaseRepository {
    protected String toFreeTextTerms(String termFilter) {
        return Arrays.stream(termFilter
            .replace("-", " ")
            .split(" "))
            .filter(t -> t.trim().length() >= 3)
            .map(w -> "+" + w + "*")
            .collect(Collectors.joining(" "));
    }

    protected String inList(int size) {
        return "(" + String.join(",", Collections.nCopies(size, "?")) + ")";
    }
}
