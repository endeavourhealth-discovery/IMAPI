package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.config.Config;

public class ConfigRepositoryImpl implements ConfigRepository {
    @Override
    public Config findByName(String name) {
        switch (name) {
            case "definition":
                return getDefinition();
            case "filterDefaults":
                return getFilterDefaults();
            case "inferredPredicates":
                return getInferredPredicates();
            case "inferredExcludePredicates":
                return getInferredExcludePredicates();
            case "conceptDashboard":
                return getConceptDashboard();
            case "defaultPredicateNames":
                return getDefaultPredicateNames();
            case "xmlSchemaDataTypes":
                return getXmlSchemaDataTypes();
            default:
                throw new DALException("Unhandled config");
        }
    }

    private Config getDefinition() {
        return new Config()
            .setName("definition")
            .setData("[\n" +
                "    {\n" +
                "    \"size\": \"100%\",\n" +
                "    \"type\": \"TextSectionHeader\",\n" +
                "    \"label\": \"Summary\",\n" +
                "    \"order\": 100,\n" +
                "    \"predicate\": \"None\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"size\": \"50%\",\n" +
                "    \"type\": \"TextWithLabel\",\n" +
                "    \"label\": \"Name\",\n" +
                "    \"order\": 101,\n" +
                "    \"predicate\": \"http://www.w3.org/2000/01/rdf-schema#label\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"size\": \"50%\",\n" +
                "    \"type\": \"TextWithLabel\",\n" +
                "    \"label\": \"Iri\",\n" +
                "    \"order\": 102,\n" +
                "    \"predicate\": \"@id\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"size\": \"50%\",\n" +
                "    \"type\": \"TextWithLabel\",\n" +
                "    \"label\": \"Code\",\n" +
                "    \"order\": 103,\n" +
                "    \"predicate\": \"http://endhealth.info/im#code\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"size\": \"50%\",\n" +
                "    \"type\": \"ObjectNameTagWithLabel\",\n" +
                "    \"label\": \"Status\",\n" +
                "    \"order\": 103,\n" +
                "    \"predicate\": \"http://endhealth.info/im#status\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"size\": \"50%\",\n" +
                "    \"type\": \"ArrayObjectNamesToStringWithLabel\",\n" +
                "    \"label\": \"Types\",\n" +
                "    \"order\": 104,\n" +
                "    \"predicate\": \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"size\": \"100%\",\n" +
                "    \"type\": \"TextHTMLWithLabel\",\n" +
                "    \"label\": \"Description\",\n" +
                "    \"order\": 105,\n" +
                "    \"predicate\": \"http://www.w3.org/2000/01/rdf-schema#comment\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"size\": \"100%\",\n" +
                "    \"type\": \"SectionDivider\",\n" +
                "    \"label\": \"SummaryDefinitionDivider\",\n" +
                "    \"order\": 200,\n" +
                "    \"predicate\": \"None\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"size\": \"100%\",\n" +
                "    \"type\": \"TextDefinition\",\n" +
                "    \"label\": \"Definition\",\n" +
                "    \"order\": 201,\n" +
                "    \"predicate\": \"inferred\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"size\": \"100%\",\n" +
                "    \"type\": \"ArrayObjectNameListboxWithLabel\",\n" +
                "    \"label\": \"Has sub types\",\n" +
                "    \"order\": 202,\n" +
                "    \"predicate\": \"subtypes\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"size\": \"100%\",\n" +
                "    \"type\": \"ArrayObjectNameListboxWithLabel\",\n" +
                "    \"label\": \"Is child of\",\n" +
                "    \"order\": 203,\n" +
                "    \"predicate\": \"http://endhealth.info/im#isChildOf\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"size\": \"100%\",\n" +
                "    \"type\": \"ArrayObjectNameListboxWithLabel\",\n" +
                "    \"label\": \"Has children\",\n" +
                "    \"order\": 204,\n" +
                "    \"predicate\": \"http://endhealth.info/im#hasChildren\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"size\": \"100%\",\n" +
                "    \"type\": \"SectionDivider\",\n" +
                "    \"label\": \"DefinitionTermsDivider\",\n" +
                "    \"order\": 300,\n" +
                "    \"predicate\": \"None\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"size\": \"100%\",\n" +
                "    \"type\": \"TermsTable\",\n" +
                "    \"label\": \"Terms\",\n" +
                "    \"order\": 301,\n" +
                "    \"predicate\": \"termCodes\"\n" +
                "    }\n" +
                "    ]");
    }

    private Config getFilterDefaults() {
        return new Config()
            .setName("filterDefaults")
            .setData("{\n" +
                "    \"schemeOptions\": [\n" +
                "    \"Discovery namespace\",\n" +
                "    \"Snomed-CT namespace\"\n" +
                "    ],\n" +
                "    \"statusOptions\": [\n" +
                "    \"Active\",\n" +
                "    \"Draft\"\n" +
                "    ],\n" +
                "    \"typeOptions\": [\n" +
                "    \"Concept\",\n" +
                "    \"Concept Set\",\n" +
                "    \"Folder\",\n" +
                "    \"Node shape\",\n" +
                "    \"ObjectProperty\",\n" +
                "    \"Property\",\n" +
                "    \"Query template\",\n" +
                "    \"Record type\",\n" +
                "    \"Value set\"\n" +
                "    ]\n" +
                "    }");
    }

    private Config getInferredPredicates() {
        return new Config()
            .setName("inferredPredicates")
            .setData("[\n" +
                "    \"http://www.w3.org/2000/01/rdf-schema#subClassOf\",\n" +
                "    \"http://endhealth.info/im#roleGroup\",\n" +
                "    \"http://endhealth.info/im#isContainedIn\"\n" +
                "    ]");
    }

    private Config getInferredExcludePredicates() {
        return new Config()
            .setName("inferredExcludePredicates")
            .setData("[\n" +
                "    \"http://www.w3.org/2000/01/rdf-schema#label\",\n" +
                "    \"http://endhealth.info/im#status\",\n" +
                "    \"http://endhealth.info/im#Status\",\n" +
                "    \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\",\n" +
                "    \"http://www.w3.org/2000/01/rdf-schema#comment\",\n" +
                "    \"http://endhealth.info/im#isChildOf\",\n" +
                "    \"http://endhealth.info/im#hasChildren\",\n" +
                "    \"http://endhealth.info/im#isContainedIn\",\n" +
                "    \"http://endhealth.info/im#code\"\n" +
                "    ]");
    }

    private Config getConceptDashboard() {
        return new Config()
            .setName("conceptDashboard")
            .setData("[\n" +
                "    {\n" +
                "    \"type\": \"ReportTable\",\n" +
                "    \"order\": 100,\n" +
                "    \"iri\": \"http://endhealth.info/im#ontologyOverview\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"type\": \"PieChartDashCard\",\n" +
                "    \"order\": 200,\n" +
                "    \"iri\": \"http://endhealth.info/im#ontologyConceptTypes\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"type\": \"PieChartDashCard\",\n" +
                "    \"order\": 300,\n" +
                "    \"iri\": \"http://endhealth.info/im#ontologyConceptSchemes\"\n" +
                "    },\n" +
                "    {\n" +
                "    \"type\": \"PieChartDashCard\",\n" +
                "    \"order\": 400,\n" +
                "    \"iri\": \"http://endhealth.info/im#ontologyConceptStatus\"\n" +
                "    }\n" +
                "    ]");
    }

    private Config getDefaultPredicateNames() {
        return new Config()
            .setName("defaultPredicateNames")
            .setData("{\n" +
                "    \"http://www.w3.org/2000/01/rdf-schema#subClassOf\": \"Is subclass of\",\n" +
                "    \"http://endhealth.info/im#roleGroup\": \"Where\",\n" +
                "    \"http://www.w3.org/2002/07/owl#equivalentClass\": \"Is equivalent to\",\n" +
                "    \"http://www.w3.org/2002/07/owl#intersectionOf\": \"Combination of\",\n" +
                "    \"http://www.w3.org/2002/07/owl#someValuesFrom\": \"With a value\",\n" +
                "    \"http://www.w3.org/2002/07/owl#onProperty\": \"On property\"\n" +
                "    }");
    }

    private Config getXmlSchemaDataTypes() {
        return new Config()
            .setName("xmlSchemaDataTypes")
            .setData("[\n" +
                "    \"http://www.w3.org/2001/XMLSchema#string\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#boolean\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#float\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#double\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#decimal\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#dateTime\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#duration\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#hexBinary\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#base64Binary\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#ID\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#IDREF\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#ENTITY\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#NOTATION\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#normalizedString\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#token\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#language\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#IDREFS\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#ENTITIES\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#NMTOKEN\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#NMTOKENS\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#Name\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#QName\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#NCName\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#integer\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#nonNegativeInteger\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#positiveInteger\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#nonPositiveInteger\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#negativeInteger\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#byte\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#int\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#long\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#short\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#unsignedByte\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#unsignedInt\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#unsignedLong\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#unsignedShort\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#date\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#time\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#gYearMonth\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#gYear\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#gMonthDay\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#gDay\",\n" +
                "    \"http://www.w3.org/2001/XMLSchema#gMonth\"\n" +
                "    ]");
    }
}

