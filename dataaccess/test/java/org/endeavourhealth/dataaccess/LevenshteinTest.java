package org.endeavourhealth.dataaccess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevenshteinTest {

    @Test
    void calculateAsthma() {
        String term = "Asthma";
        System.out.println(Levenshtein.calculate(term, "Mixed Asthma"));
        System.out.println(Levenshtein.calculate(term, "Asthma (disorder)"));
    }

    @Test
    void calculate() {
        String term = "Encounter";
        System.out.println(Levenshtein.calculate(term, "http://endhealth.info/EncounterTerm#LE_1"));
        System.out.println(Levenshtein.calculate(term, "Encounter (record type)"));
    }

}
