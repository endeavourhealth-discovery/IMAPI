package org.endeavourhealth.imapi.logic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class LevenshteinTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "Asthma (disorder)"})
    void calculateEqual(String testString) {
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
        String testString = "Asthma (disorder)";
        String editString = "Ashtma (disorder)";
        int actual = Levenshtein.calculate(testString, editString);
        assertEquals(2, actual);
    }

    @Test
    void calculateNull() {
        String testString = null;
        int actual = Levenshtein.calculate(testString, testString);
        assertEquals(0, actual);
    }
}
