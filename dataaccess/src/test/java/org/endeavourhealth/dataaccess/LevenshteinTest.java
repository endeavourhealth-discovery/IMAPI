package org.endeavourhealth.dataaccess;

import org.endeavourhealth.dataaccess.Levenshtein;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevenshteinTest {

    @Test
    void calculateEqual() {
        String testString = "Asthma (disorder)";
        int actual = Levenshtein.calculate(testString, testString);
        assertEquals(0, actual);
    }

    @Test
    void calculate1Edit() {
        String testString = "Asthma (disorder)";
        String editString = "Bsthma (disorder)";
        int actual = Levenshtein.calculate(testString, editString);
        assertEquals(1, actual);
    }

    @Test
    void calculateSymmetric() {
        String testString = "Asthma (disorder)";
        String editString = "Encounter (record type)";
        int actual = Levenshtein.calculate(testString, editString);
        int expected = Levenshtein.calculate(editString, testString);
        assertEquals(expected, actual);
    }

    @Test
    void calculateSwap() {
        // TODO - Add in "swap" check
        String testString = "Asthma (disorder)";
        String editString = "Ashtma (disorder)";
        int actual = Levenshtein.calculate(testString, editString);
        assertEquals(2, actual);
    }

    @Test
    void calculateEmpty() {
        String testString = "";
        int actual = Levenshtein.calculate(testString, testString);
        assertEquals(0, actual);
    }

    @Test
    void calculateNull() {
        String testString = null;
        int actual = Levenshtein.calculate(testString, testString);
        assertEquals(0, actual);
    }
}
