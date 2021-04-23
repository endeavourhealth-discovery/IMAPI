package org.endeavourhealth.dataaccess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevenshteinTest {

    @Test
    void calculate() {
        String term = "Asthma";
        System.out.println(Levenshtein.calculate(term, "Mixed Asthma"));
        System.out.println(Levenshtein.calculate(term, "Asthma (disorder)"));
    }
}
