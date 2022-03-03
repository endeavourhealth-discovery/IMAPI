package org.endeavourhealth.imapi.filer;


import org.endeavourhealth.imapi.filer.rdf4j.ClosureGeneratorBulk;
import org.endeavourhealth.imapi.filer.rdf4j.ClosureGeneratorRdf4j;
import org.endeavourhealth.imapi.filer.rdf4j.TTBulkFiler;
import org.endeavourhealth.imapi.filer.rdf4j.TTDocumentFilerRdf4j;

public class TTFilerFactory {
    private static boolean skipDeletes=false;
    private static boolean bulk = false;
    private TTFilerFactory() {}

    public static boolean isSkipDeletes() {
        return skipDeletes;
    }

    public static void setSkipDeletes(boolean skipDeletes) {
        TTFilerFactory.skipDeletes = skipDeletes;
    }

    public static TTDocumentFiler getDocumentFiler() {
        if (!bulk)
            return new TTDocumentFilerRdf4j();
        else
            return new TTBulkFiler();
    }

    public static TCGenerator getClosureGenerator() throws TTFilerException {
        if (!bulk)
         return new ClosureGeneratorRdf4j();
        else
            return new ClosureGeneratorBulk();
    }

    public static boolean isBulk() {
        return bulk;
    }

    public static void setBulk(boolean bulk) {
        TTFilerFactory.bulk = bulk;
    }
}
