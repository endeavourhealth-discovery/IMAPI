package org.endeavourhealth.imapi.filer;


import org.endeavourhealth.imapi.filer.rdf4j.ClosureGeneratorRdf4j;
import org.endeavourhealth.imapi.filer.rdf4j.TTDocumentFilerRdf4j;

public class TTFilerFactory {
    private static boolean skipDeletes=false;
    private TTFilerFactory() {}

    public static boolean isSkipDeletes() {
        return skipDeletes;
    }

    public static void setSkipDeletes(boolean skipDeletes) {
        TTFilerFactory.skipDeletes = skipDeletes;
    }

    public static TTDocumentFiler getDocumentFiler() {
        return new TTDocumentFilerRdf4j();
    }

    public static TCGenerator getClosureGenerator() throws TTFilerException {
        return new ClosureGeneratorRdf4j();
    }
}
