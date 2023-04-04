package org.endeavourhealth.zfhirmapper;

public class ZFhirMapperAll {
    public static void main(String[] argv) throws Exception {
        if (argv.length != 2) {
            System.err.println("Usage: ZFhirMapperAll <in folder> <out folder>");
            System.exit(-1);
        }

        new ZFhirMapperPatient().execute(argv[0] + "patient.txt", argv[1] + "patient_");
        new ZFhirMapperEncounter().execute(argv[0] + "enc_1.txt", argv[1] + "enc1_");
        new ZFhirMapperEncounter().execute(argv[0] + "enc_2.txt", argv[1] + "enc2_");
        new ZFhirMapperEncounter().execute(argv[0] + "enc_3.txt", argv[1] + "enc3_");
        new ZFhirMapperEpisodeOfCare().execute(argv[0] + "eoc.txt", argv[1] + "eoc_");
        new ZFhirMapperOrganization().execute(argv[0] + "organization.txt", argv[1] + "org_");
        new ZFhirMapperPractitioner().execute(argv[0] + "practitioner.txt", argv[1] + "practitioner_");
        new ZFhirMapperMedicationStatement().execute(argv[0] + "rx_statement.txt", argv[1] + "rxs_");
        new ZFhirMapperObservation().execute(argv[0] + "obs_1.txt", argv[1] + "obs1_");
        new ZFhirMapperObservation().execute(argv[0] + "obs_2.txt", argv[1] + "obs2_");
        new ZFhirMapperObservation().execute(argv[0] + "obs_3.txt", argv[1] + "obs3_");
        new ZFhirMapperObservation().execute(argv[0] + "obs_4.txt", argv[1] + "obs4_");
        new ZFhirMapperObservation().execute(argv[0] + "obs_5.txt", argv[1] + "obs5_");
        new ZFhirMapperObservation().execute(argv[0] + "obs_6.txt", argv[1] + "obs6_");
        new ZFhirMapperObservation().execute(argv[0] + "full_bp.txt", argv[1] + "fullbp_");
        new ZFhirMapperObservation().execute(argv[0] + "individual_bp.txt", argv[1] + "individualbp_");
    }
}
