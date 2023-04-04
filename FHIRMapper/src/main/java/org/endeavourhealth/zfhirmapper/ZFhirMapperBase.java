package org.endeavourhealth.zfhirmapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.endeavourhealth.imapi.logic.codegen.IMDMBase;
import org.endeavourhealth.persistence.IMPFiler;
import org.endeavourhealth.persistence.IMPFilerCSV;

import java.io.File;
import java.util.Collection;

public abstract class ZFhirMapperBase<T> {
    public T execute(String infile, String outpath) throws Exception {
        int c = 1;

        FhirContext ctx = FhirContext.forDstu2();
        IParser parser = ctx.newJsonParser();

        try (IMPFiler filer = new IMPFilerCSV(outpath)) {

            File file = new File(infile);
            LineIterator it = FileUtils.lineIterator(file, "UTF-8");
            while (it.hasNext()) {
                String line = it.nextLine();
                Collection<IMDMBase> pojos = RunMapper(line, parser);
                filer.fileIMPs(pojos);

                if (c % 100 == 0) System.out.println(c);
                c++;
            }
        }

        return (T) this;
    }

    public abstract Collection<IMDMBase> RunMapper(String str, IParser parser) throws Exception;
}
