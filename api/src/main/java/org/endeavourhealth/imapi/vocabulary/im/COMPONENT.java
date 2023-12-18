package org.endeavourhealth.imapi.vocabulary.im;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum COMPONENT implements Vocabulary {
    NAMESPACE(IM.NAMESPACE.iri + "Component_"),

    //editor components
    TEXT_DISPLAY(NAMESPACE.iri + "textDisplay"),
    TEXT_INPUT(NAMESPACE.iri + "textInput"),
    HTML_INPUT(NAMESPACE.iri + "htmlInput"),
    ENTITY_MULTI_SEARCH(NAMESPACE.iri + "entityMultiSearch"),
    ENTITY_SEARCH(NAMESPACE.iri + "entitySearch"),
    ENTITY_COMBOBOX(NAMESPACE.iri + "entityComboBox"),
    ENTITY_DROPDOWN(NAMESPACE.iri + "entityDropdown"),
    STEPS_GROUP(NAMESPACE.iri + "stepsGroup"),
    ARRAY_BUILDER(NAMESPACE.iri + "arrayBuilder"),
    ENTITY_AUTO_COMPLETE(NAMESPACE.iri + "entityAutoComplete"),
    MEMBERS_BUILDER(NAMESPACE.iri + "membersBuilder"),
    COMPONENT_GROUP(NAMESPACE.iri + "componentGroup"),
    ARRAY_BUILDER_WITH_DROPDOWN(NAMESPACE.iri + "arrayBuilderWithDropdown"),
    PROPERTY_BUILDER(NAMESPACE.iri + "propertyBuilder"),
    SET_DEFINITION_BUILDER(NAMESPACE.iri + "SetDefinitionBuilder"),
    QUERY_DEFINITION_BUILDER(NAMESPACE.iri + "QueryDefinitionBuilder"),
    TOGGLEABLE(NAMESPACE.iri + "ToggleableComponent"),
    HORIZONTAL_LAYOUT(NAMESPACE.iri + "HorizontalLayout"),
    VERTICAL_LAYOUT(NAMESPACE.iri + "VerticalLayout"),
    DROPDOWN_TEXT_INPUT_CONCATENATOR(NAMESPACE.iri + "dropdownTextInputConcatenator"),
    ROLE_GROUP_BUILDER(NAMESPACE.iri + "roleGroupBuilder"),
    TERM_CODE_EDITOR(NAMESPACE.iri + "termCodeEditor"),
    TEXT_DROPDOWN(NAMESPACE.iri + "textDropdown"),
    ENTITY_DISPLAY(NAMESPACE.iri + "entityDisplay"),
    IRI_BUILDER(NAMESPACE.iri + "iriBuilder");

    public final String iri;

    COMPONENT(String url) {
        this.iri = url;
    }

    @Override
    public TTIriRef asTTIriRef() {
        return iri(this.iri);
    }

    @Override
    @JsonValue
    public String getIri() {
        return iri;
    }

    public static boolean contains(String iri) {
        try {
            COMPONENT.valueOf(iri);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
