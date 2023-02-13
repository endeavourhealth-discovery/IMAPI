package org.endeavourhealth.imapi.filer;


import org.endeavourhealth.imapi.filer.rdf4j.*;

public class TTFilerFactory {
    private static boolean bulk = false;
    private static int privacyLevel=0;
    private static boolean transactional= false;
    private TTFilerFactory() {}

    public static boolean isTransactional() {
        return transactional;
    }

    public static void setTransactional(boolean transactional) {
        TTFilerFactory.transactional = transactional;
    }

    public static TTDocumentFiler getDocumentFiler() {
        if (!bulk)
            return new TTDocumentFilerRdf4j();
        else
            return new TTBulkFiler();
    }

    public static TTEntityFiler getEntityFiler(){
        return new TTEntityFilerRdf4j();
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

    public static int getPrivacyLevel() {
        return privacyLevel;
    }

    public static void setPrivacyLevel(int privacyLevel) {
        TTFilerFactory.privacyLevel = privacyLevel;
    }
}
