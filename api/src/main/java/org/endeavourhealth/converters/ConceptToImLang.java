package org.endeavourhealth.converters;

import java.text.MessageFormat;
import java.util.Set;
import java.util.stream.Collectors;

import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.*;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

@Component
public class ConceptToImLang {

    public String translateConceptToImLang(TTConcept concept) {
        String imLangConcept = "";

        // add coreConcept
        imLangConcept = translateCoreConcept(concept, imLangConcept);

        // add subClassOf
        Set<TTIriRef> types = concept.getType().getElements().stream().map(TTValue::asIriRef).collect(Collectors.toSet());
        if (types.contains(OWL.CLASS) ||
            types.contains(IM.RECORD) ||
            types.contains(IM.VALUESET)
        ) {
            imLangConcept = translateExpression(concept, imLangConcept, RDFS.SUBCLASSOF, "SubClassOf");
        }

        // add members
        if (types.contains(IM.VALUESET)) {
            imLangConcept = translateMembers(concept, imLangConcept);
        }

        // add properties
        if (types.contains(IM.RECORD)) {
            imLangConcept = translateProperties(concept, imLangConcept);
        }

        // add equivalentTo
        if (types.contains(OWL.CLASS) ||
            types.contains(IM.RECORD)) {
            imLangConcept = translateExpression(concept, imLangConcept, OWL.EQUIVALENTCLASS, "equivalentTo");
        }

        imLangConcept = StringUtils.removeEnd(imLangConcept, ";");
        imLangConcept = imLangConcept.concat(".\n");

        return imLangConcept;
    }

    private String translateCoreConcept(TTConcept concept, String imLangConcept) {
        // add iri
        imLangConcept = imLangConcept.concat(concept.getIri() + "\n");

        // add type
        if (concept.has(RDF.TYPE)) {
            String types = concept.getType().getElements().stream().map(t -> convertConceptReferenceToString(t.asIriRef())).collect(Collectors.joining(","));
            imLangConcept = imLangConcept.concat("type " + types + ";\n");
        }

        // add name
        imLangConcept = imLangConcept.concat("Name \"" + concept.getName() + "\";\n");

        // add description
        imLangConcept = imLangConcept.concat("description \"" + concept.getDescription() + "\";\n");

        // add code
        imLangConcept = imLangConcept.concat("code \"" + concept.getCode() + "\";\n");

        // add scheme
        if (concept.getScheme() != null) {
        	imLangConcept = imLangConcept.concat("scheme \"" + concept.getScheme().getIri() + "\";\n");
        }

        // add status
        imLangConcept = imLangConcept.concat("status " + convertConceptReferenceToString(concept.getStatus()) + ";\n");

        // add version
        // imLangConcept = imLangConcept.concat("version \"" + concept.getVersion() + "\";\n");

        return imLangConcept;
    }

    private String translateExpression(TTConcept concept, String imLangConcept, TTIriRef expressionType, String expressionName) {
        if (concept.has(expressionType)) {
            imLangConcept = imLangConcept.concat(expressionName + " ");
            for (TTValue expression : concept.getAsArrayElements(expressionType)) {
                if (expression.isIriRef())
                    imLangConcept = imLangConcept.concat(convertConceptReferenceToString(expression.asIriRef()) + ",\n");
                else if (expression.isNode() && expression.asNode().has(OWL.INTERSECTIONOF)) {
                    for (TTValue intersection : expression.asNode().getAsArrayElements(OWL.INTERSECTIONOF)) {
                        if (intersection.isIriRef())
                            imLangConcept = imLangConcept.concat(convertConceptReferenceToString(intersection.asIriRef()) + ",\n");
                        else if (intersection.isNode()) {
                            TTIriRef property = intersection.asNode().getAsIriRef(OWL.ONPROPERTY);
                            TTNode range = intersection.asNode().getAsNode(OWL.SOMEVALUESFROM);
                            imLangConcept = imLangConcept.concat(MessageFormat.format("[{0} {1}],\n",
                                convertConceptReferenceToString(property),
                                convertConceptReferenceToString(range.asIriRef())
                            ));
                        }
                    }
                }
            }
            imLangConcept = StringUtils.removeEnd(imLangConcept, ",\n");
            imLangConcept = imLangConcept.concat(";\n");
        }
        return imLangConcept;
    }

    private String translateMembers(TTConcept concept, String imLangConcept) {
        if (concept.has(IM.HAS_MEMBER)) {
            imLangConcept = imLangConcept.concat("member ");
            for (TTValue subClass : concept.getAsArrayElements(IM.HAS_MEMBER)) {
                if (subClass.isIriRef())
                    imLangConcept = imLangConcept.concat(convertConceptReferenceToString(subClass.asIriRef()) + ", ");

            }
            imLangConcept = StringUtils.removeEnd(imLangConcept, ", ");
            imLangConcept = imLangConcept.concat(";\n");
        }
        return imLangConcept;
    }

    private String translateProperties(TTConcept concept, String imLangConcept) {
        if (concept.has(IM.PROPERTY_GROUP)) {
            imLangConcept = imLangConcept.concat("property ");
            for (TTValue propertyGroup : concept.getAsArrayElements(IM.PROPERTY_GROUP)) {
                // Exclude inherited properties
                if (!propertyGroup.asNode().has(IM.INHERITED_FROM)) {
                    for (TTValue property : propertyGroup.asNode().getAsArrayElements(SHACL.PROPERTY)) {
                        TTIriRef propertyIri = property.asNode().getAsIriRef(SHACL.PATH);
                        TTIriRef propertyType = property.asNode().has(SHACL.CLASS)
                            ? property.asNode().getAsIriRef(SHACL.CLASS)
                            : property.asNode().getAsIriRef(SHACL.DATATYPE);

                        imLangConcept = imLangConcept.concat(MessageFormat.format("[{0} {1}],\n",
                            convertConceptReferenceToString(propertyIri),
                            convertConceptReferenceToString(propertyType)
                        ));
                    }
                }
            }
            imLangConcept = StringUtils.removeEnd(imLangConcept, ", ");
            imLangConcept = imLangConcept.concat(";\n");
        }

        return imLangConcept;
    }

    private String convertConceptReferenceToString(TTIriRef conceptReference) {
        if (conceptReference == null)
            return "\"\"";

        String result = "\"" + conceptReference.getIri();
        if (conceptReference.getName() != null && !conceptReference.getName().isEmpty())
            result += " | " + conceptReference.getName();

        result += "\"";

        return result;
    }

}
