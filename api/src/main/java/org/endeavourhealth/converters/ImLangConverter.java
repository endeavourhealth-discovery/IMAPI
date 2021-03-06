package org.endeavourhealth.converters;

import java.text.MessageFormat;

import org.endeavourhealth.imapi.model.ClassExpression;
import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

@Component
public class ImLangConverter {

	public String convertToImLang(Concept concept) {
		String imLangConcept = "";

		// add coreConcept
		imLangConcept = translateCoreConcept(concept, imLangConcept);

		// add subClassOf
		imLangConcept = translateSubClassOf(concept, imLangConcept);

		// add members
		imLangConcept = translateMembers(concept, imLangConcept);

		// add equivalentTo
		imLangConcept = translateEquivalentTo(concept, imLangConcept);

		return imLangConcept;
	}

	private String translateCoreConcept(Concept concept, String imLangConcept) {
		// add iri
		imLangConcept = imLangConcept.concat(concept.getIri() + "\n");

		// add type
		imLangConcept = imLangConcept.concat("type " + concept.getConceptType().getName() + ";\n");

		// add name
		imLangConcept = imLangConcept.concat("Name \"" + concept.getName() + "\";\n");

		// add description
		imLangConcept = imLangConcept.concat("description \"" + concept.getDescription() + "\";\n");

		// add code
		imLangConcept = imLangConcept.concat("code \"" + concept.getCode() + "\";\n");
		return imLangConcept;
	}

	private String translateSubClassOf(Concept concept, String imLangConcept) {
		if (concept.getSubClassOf() != null) {
			imLangConcept = imLangConcept.concat("SubClassOf ");
			for (ClassExpression subClass : concept.getSubClassOf()) {
				
				if (subClass.getClazz() != null) {
					imLangConcept = imLangConcept.concat(convertConceptReferrenceToString(subClass.getClazz()) + ", ");
				}
				
				if (subClass.getIntersection() != null) {
					for (ClassExpression intersection : subClass.getIntersection()) {
						if (intersection.getClazz() != null) {
							imLangConcept = imLangConcept.concat(convertConceptReferrenceToString(intersection.getClazz()) + ", ");
						}
					}
				}
			}
			imLangConcept = StringUtils.removeEnd(imLangConcept, ", ");
			imLangConcept = imLangConcept.concat(";\n");
		}
		return imLangConcept;
	}

	private String translateMembers(Concept concept, String imLangConcept) {
		if(concept.getSubClassOf() != null) {
			for (ClassExpression subClass : concept.getSubClassOf()) {
				if(subClass.getIntersection() != null) {
					for (ClassExpression intersection : subClass.getIntersection()) {
						if (intersection.getObjectPropertyValue() != null && intersection.getObjectPropertyValue().getValueType() != null) {
							imLangConcept = imLangConcept.concat("member " + convertConceptReferrenceToString(intersection.getObjectPropertyValue().getValueType()));
						}
						
						if (intersection.getObjectPropertyValue() != null && intersection.getObjectPropertyValue().getExpression() != null) {
							for (ClassExpression member : intersection.getObjectPropertyValue().getExpression().getUnion()) {
								imLangConcept = imLangConcept.concat(convertConceptReferrenceToString(member.getClazz()) + ",");
							}
							imLangConcept = imLangConcept.concat(".\n");
						}
					}
				}
			}
		}
		
		return imLangConcept;
	}

	private String translateEquivalentTo(Concept concept, String imLangConcept) {
		if(concept.getEquivalentTo() != null) {
			imLangConcept = imLangConcept.concat("equivalentTo ");
			for (ClassExpression equivalentTo : concept.getEquivalentTo()) {
				if(equivalentTo.getIntersection() != null) {
					
					int i = 0;
					for (ClassExpression intersection : equivalentTo.getIntersection()) {
						i++;
						if(intersection.getClazz() != null) {
							imLangConcept = imLangConcept.concat(convertConceptReferrenceToString(intersection.getClazz()) + ",\n");
						}
						
						if(intersection.getObjectPropertyValue() != null) {
							
							imLangConcept = imLangConcept.concat(MessageFormat.format("[{0} {1}; minCount {2}]", 
									convertConceptReferrenceToString(intersection.getObjectPropertyValue().getProperty()),
									convertConceptReferrenceToString(intersection.getObjectPropertyValue().getValueType()),
									intersection.getObjectPropertyValue().getMin()
									));
							if(i != equivalentTo.getIntersection().size()) {
								imLangConcept = imLangConcept.concat(",\n");
							}
						}
					}
				}
			}
			imLangConcept = imLangConcept.concat(".");
		}
		
		return imLangConcept;
	}

	private String convertConceptReferrenceToString(ConceptReference conceptReference) {
		return MessageFormat.format("\"{0} | {1}\"", conceptReference.getIri(), conceptReference.getName());
	}

}
