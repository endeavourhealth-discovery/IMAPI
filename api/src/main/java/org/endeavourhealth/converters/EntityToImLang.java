package org.endeavourhealth.converters;

import java.text.MessageFormat;
import java.util.Set;
import java.util.stream.Collectors;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.*;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

@Component
public class EntityToImLang {

    public String translateEntityToImLang(TTEntity entity) {
        String imLangEntity = "";

        // add coreEntity
        imLangEntity = translateCoreEntity(entity, imLangEntity);

        // add subClassOf
        Set<TTIriRef> types = entity.getType().getElements().stream().map(TTValue::asIriRef).collect(Collectors.toSet());
        if (types.contains(OWL.CLASS) ||
            types.contains(IM.RECORD) ||
            types.contains(IM.VALUESET)
        ) {
            imLangEntity = translateExpression(entity, imLangEntity, RDFS.SUBCLASSOF, "SubClassOf");
        }

        // add members
        if (types.contains(IM.VALUESET)) {
            imLangEntity = translateMembers(entity, imLangEntity);
        }

        // add properties
        if (types.contains(IM.RECORD)) {
            imLangEntity = translateProperties(entity, imLangEntity);
        }

        // add equivalentTo
        if (types.contains(OWL.CLASS) ||
            types.contains(IM.RECORD)) {
            imLangEntity = translateExpression(entity, imLangEntity, OWL.EQUIVALENTCLASS, "equivalentTo");
        }

        imLangEntity = StringUtils.removeEnd(imLangEntity, ";");
        imLangEntity = imLangEntity.concat(".\n");

        return imLangEntity;
    }

    private String translateCoreEntity(TTEntity entity, String imLangEntity) {
        // add iri
        imLangEntity = imLangEntity.concat(entity.getIri() + "\n");

        // add type
        if (entity.has(RDF.TYPE)) {
            String types = entity.getType().getElements().stream().map(t -> convertEntityReferenceToString(t.asIriRef())).collect(Collectors.joining(","));
            imLangEntity = imLangEntity.concat("type " + types + ";\n");
        }

        // add name
        imLangEntity = imLangEntity.concat("Name \"" + entity.getName() + "\";\n");

        // add description
        imLangEntity = imLangEntity.concat("description \"" + entity.getDescription() + "\";\n");

        // add code
        imLangEntity = imLangEntity.concat("code \"" + entity.getCode() + "\";\n");

        // add scheme
        if (entity.getScheme() != null) {
        	imLangEntity = imLangEntity.concat("scheme " + convertEntityReferenceToString(entity.getScheme()) + ";\n");
        }

        // add status
        imLangEntity = imLangEntity.concat("status " + convertEntityReferenceToString(entity.getStatus()) + ";\n");

        // add version
        // imLangEntity = imLangEntity.concat("version \"" + entity.getVersion() + "\";\n");

        return imLangEntity;
    }

    private String translateExpression(TTEntity entity, String imLangEntity, TTIriRef expressionType, String expressionName) {
        if (entity.has(expressionType)) {
            imLangEntity = imLangEntity.concat(expressionName + " ");
            for (TTValue expression : entity.getAsArrayElements(expressionType)) {
                if (expression.isIriRef())
                    imLangEntity = imLangEntity.concat(convertEntityReferenceToString(expression.asIriRef()) + ",\n");
                else if (expression.isNode() && expression.asNode().has(OWL.INTERSECTIONOF)) {
                    for (TTValue intersection : expression.asNode().getAsArrayElements(OWL.INTERSECTIONOF)) {
                        if (intersection.isIriRef())
                            imLangEntity = imLangEntity.concat(convertEntityReferenceToString(intersection.asIriRef()) + ",\n");
                        else if (intersection.isNode()) {
                            TTIriRef property = intersection.asNode().getAsIriRef(OWL.ONPROPERTY);
                            try {
                            	TTNode range = intersection.asNode().getAsNode(OWL.SOMEVALUESFROM);
                            	imLangEntity = imLangEntity.concat(MessageFormat.format("[{0} {1}],\n",
		                                convertEntityReferenceToString(property),
		                                convertEntityReferenceToString(range.asIriRef())
		                            ));
							} catch (ClassCastException e) {
								TTIriRef range = intersection.asNode().getAsIriRef(OWL.SOMEVALUESFROM);
								imLangEntity = imLangEntity.concat(MessageFormat.format("[{0} {1}],\n",
		                                convertEntityReferenceToString(property),
		                                convertEntityReferenceToString(range.asIriRef())
		                            ));
							} 
                        }
                    }
                }
            }
            imLangEntity = StringUtils.removeEnd(imLangEntity, ",\n");
            imLangEntity = imLangEntity.concat(";\n");
        }
        return imLangEntity;
    }

    private String translateMembers(TTEntity entity, String imLangEntity) {
        if (entity.has(IM.HAS_MEMBER)) {
            imLangEntity = imLangEntity.concat("member ");
            for (TTValue subClass : entity.getAsArrayElements(IM.HAS_MEMBER)) {
                if (subClass.isIriRef())
                    imLangEntity = imLangEntity.concat(convertEntityReferenceToString(subClass.asIriRef()) + ", ");

            }
            imLangEntity = StringUtils.removeEnd(imLangEntity, ", ");
            imLangEntity = imLangEntity.concat(";\n");
        }
        return imLangEntity;
    }

    private String translateProperties(TTEntity entity, String imLangEntity) {
        if (entity.has(IM.PROPERTY_GROUP)) {
            imLangEntity = imLangEntity.concat("property ");
            for (TTValue propertyGroup : entity.getAsArrayElements(IM.PROPERTY_GROUP)) {
                // Exclude inherited properties
                if (!propertyGroup.asNode().has(IM.INHERITED_FROM)) {
                    for (TTValue property : propertyGroup.asNode().getAsArrayElements(SHACL.PROPERTY)) {
                        TTIriRef propertyIri = property.asNode().getAsIriRef(SHACL.PATH);
                        TTIriRef propertyType = property.asNode().has(SHACL.CLASS)
                            ? property.asNode().getAsIriRef(SHACL.CLASS)
                            : property.asNode().getAsIriRef(SHACL.DATATYPE);

                        imLangEntity = imLangEntity.concat(MessageFormat.format("[{0} {1}],\n",
                            convertEntityReferenceToString(propertyIri),
                            convertEntityReferenceToString(propertyType)
                        ));
                    }
                }
            }
            imLangEntity = StringUtils.removeEnd(imLangEntity, ", ");
            imLangEntity = imLangEntity.concat(";\n");
        }

        return imLangEntity;
    }

    private String convertEntityReferenceToString(TTIriRef entityReference) {
        if (entityReference == null)
            return "\"\"";
        
        String[] strings = entityReference.getIri().split("/");
        String abbrIri = strings[strings.length - 1].replaceAll("#", ":");

        String result = "\"" + abbrIri;
        if (entityReference.getName() != null && !entityReference.getName().isEmpty())
            result += " | " + entityReference.getName();

        result += "\"";

        return result;
    }

}
