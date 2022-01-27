package org.endeavourhealth.imapi.filer;


import org.endeavourhealth.imapi.filer.rdf4j.ClosureGeneratorRdf4j;
import org.endeavourhealth.imapi.filer.rdf4j.TTDocumentFilerRdf4j;

public class TTFilerFactory {
    public static boolean skipDeletes=false;
    private TTFilerFactory() {}

    public static TTDocumentFiler getDocumentFiler() throws TTFilerException {
        return new TTDocumentFilerRdf4j();
    }

    public static TCGenerator getClosureGenerator() throws TTFilerException {
        return new ClosureGeneratorRdf4j();
    }
}