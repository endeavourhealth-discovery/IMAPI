package org.endeavourhealth.imapi.logic.service;

import org.apache.commons.text.CaseUtils;
import org.endeavourhealth.imapi.dataaccess.CodeGenRepository;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.codegen.CodeGenTemplate;
import org.endeavourhealth.imapi.model.dto.CodeGenDto;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.springframework.http.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CodeGenService {
  private EntityService entityService = new EntityService();
  private CodeGenRepository codeGenRepository = new CodeGenRepository();

  public List<String> getCodeTemplateList() {
    return codeGenRepository.getCodeTemplateList();
  }

  public CodeGenDto getCodeTemplate(String name) {
    return codeGenRepository.getCodeTemplate(name);
  }

  public void updateCodeTemplate(String name, String extension, String wrapper, Map<String, String> dataTypeMap, String template) {
    codeGenRepository.updateCodeTemplate(name, extension, wrapper, dataTypeMap, template);
  }

  public HttpEntity<Object> generateCode(String iri, String templateName, String namespace) {
    CodeGenDto template = codeGenRepository.getCodeTemplate(templateName);
    List<TTIriRef> models = getModelAndDependencies(iri);

    HttpEntity<Object> headers = createModelCodeZip(namespace, models, template);
    if (headers != null) return headers;

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to generate code");
  }

  private List<TTIriRef> getModelAndDependencies(String iri) {
    return getModelAndDependencies(iri, new ArrayList<>());
  }

  private List<TTIriRef> getModelAndDependencies(String iri, List<TTIriRef> models) {
    if (models.stream().anyMatch(m -> m.getIri().equals(iri)))
      return models;

    SearchResultSummary summary = entityService.getSummary(iri);
    models.add(new TTIriRef(summary.getIri(), summary.getName()).setDescription(summary.getDescription()));

    for (EntityReferenceNode child : entityService.getImmediateChildren(iri, null, null, null, false)) {
      getModelAndDependencies(child.getIri(), models);
    }

    return models;
  }

  private HttpEntity<Object> createModelCodeZip(String namespace, List<TTIriRef> models, CodeGenDto template) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      addModelsToZip(namespace, models, template, zos);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to create zip:" + e.getMessage());
    }
    String filename = models.get(0).getName() + " " + LocalDate.now();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("application", "force-download"));
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + ".txt\"");
    return new HttpEntity<>(baos.toByteArray(), headers);
  }

  private void addModelsToZip(String namespace, List<TTIriRef> models, CodeGenDto template, ZipOutputStream zos) throws IOException {
    for (TTIriRef model : models) {
      List<DataModelProperty> properties = entityService.getDataModelProperties(model.getIri());
      String code = generateCodeForModel(template, model, properties, namespace);

      zos.putNextEntry(new ZipEntry(codify(model.getName()) + template.getExtension()));
      zos.write(code.getBytes());
      zos.closeEntry();
    }
  }


  protected String generateCodeForModel(CodeGenDto template, TTIriRef model, List<DataModelProperty> properties, String namespace) {
    StringBuilder code = new StringBuilder();

    CodeGenTemplate t = splitTemplate(template.getTemplate());

    code.append(replaceTokens(template, t.getHeader(), namespace, model, null));

    if (properties != null && !properties.isEmpty()) {
      for (DataModelProperty prop : properties) {
        code.append(replacePropertyTokens(template, t.getProperty(), t.getCollectionProperty(), namespace, model, prop));
      }
    }

    code.append(t.getFooter());

    return code.toString();
  }

  private CodeGenTemplate splitTemplate(String template) {
    String propertyTemp = "<template #property>";
    String propertyTempEnd = "</template #property>";
    String arrayTemp = "<template #array>";
    String arrayTempEnd = "</template #array>";

    int ps_s = template.indexOf(propertyTemp);
    int ps_l = propertyTemp.length();
    int pe_s = template.indexOf(propertyTempEnd);
    int pe_l = propertyTempEnd.length();
    int as_s = template.indexOf(arrayTemp);
    int as_l = arrayTemp.length();
    int ae_s = template.indexOf(arrayTempEnd);

    String header = template.substring(0, ps_s);
    String footer = template.substring(pe_s + pe_l);
    String property = template.substring(ps_s + ps_l, as_s > 0 ? as_s : pe_s);
    String array = as_s > 0 ? template.substring(as_s + as_l, ae_s) : "";

    return new CodeGenTemplate()
      .setHeader(header)
      .setFooter(footer)
      .setProperty(property)
      .setCollectionProperty(array);
  }

  private String replacePropertyTokens(
    CodeGenDto template,
    String property,
    String collectionProperty,
    String namespace,
    TTIriRef model,
    DataModelProperty prop
  ) {
    if (prop.isArray())
      property = property + collectionProperty;

    return replaceTokens(template, property, namespace, model, prop);
  }

  private String replaceTokens(CodeGenDto template, String subTemplate, String namespace, TTIriRef model, DataModelProperty prop) {
    String result = subTemplate;

    if (namespace != null) result = replaceVariants(result, "NAMESPACE", namespace);

    if (model.hasName()) result = replaceVariants(result, "MODEL NAME", model.getName());

    if (model.hasDescription()) result = replaceVariants(result, "MODEL COMMENT", model.getDescription());

    if (prop != null && prop.hasProperty() && prop.getProperty().hasName() && prop.hasType()) {
      if (prop.hasType() && template.getDataType(prop.getType().getIri()) != null) {
        String basePropertyType = template.getDataType(prop.getType().getIri());
        String propertyType =
          prop.isArray() && template.hasCollectionWrapper() ? replace(template.getCollectionWrapper(), "BASE DATA TYPE", basePropertyType) : basePropertyType;

        result = replace(result, "BASE DATA TYPE", basePropertyType);
        result = replace(result, "DATA TYPE", propertyType);
      } else {
        String basePropertyType = prop.getType().hasName() ? prop.getType().getName() : "!!UNKNOWN!!";
        String propertyType = prop.isArray() && template.hasCollectionWrapper() ? replaceVariants(template.getCollectionWrapper(), "BASE DATA TYPE", basePropertyType) : basePropertyType;

        result = replaceVariants(result, "BASE DATA TYPE", basePropertyType);
        result = replaceVariants(result, "DATA TYPE", propertyType);
      }
      result = replaceVariants(result, "PROPERTY NAME", prop.getProperty().getName());
    }

    return result;
  }

  private String replaceVariants(String template, String name, String value) {
    return template
      .replace("${" + name + "}", value)
      .replace("${" + codify(name) + "}", codify(value))
      .replace("${" + name.toLowerCase() + "}", value.toLowerCase())
      .replace("${" + codify(name.toLowerCase()) + "}", codify(value.toLowerCase()))
      .replace("${" + toCamelCase(name) + "}", toCamelCase(value))
      .replace("${" + codify(toCamelCase(name)) + "}", codify(toCamelCase(value)))
      .replace("${" + toTitleCase(name) + "}", toTitleCase(value))
      .replace("${" + codify(toTitleCase(name)) + "}", codify(toTitleCase(value)));
  }

  private String replace(String template, String name, String value) {
    return template
      .replace("${" + name + "}", value)
      .replace("${" + codify(name) + "}", value)
      .replace("${" + name.toLowerCase() + "}", value)
      .replace("${" + codify(name.toLowerCase()) + "}", value)
      .replace("${" + toCamelCase(name) + "}", value)
      .replace("${" + codify(toCamelCase(name)) + "}", value)
      .replace("${" + toTitleCase(name) + "}", value)
      .replace("${" + codify(toTitleCase(name)) + "}", value);
  }

  private String codify(String name) {
    return name
      .replace(" ", "")
      .replace("-", "")
      .replace(".", "")
      .replace(",", "")
      .replace("#", "")
      .replace("'", "")
      .replace("(", "")
      .replace(")", "")
      .replace("\"", "")
      .replace("/", "");
  }

  private String toCamelCase(String str) {
    return CaseUtils.toCamelCase(str, false, ' ');
  }

  private String toTitleCase(String str) {
    return CaseUtils.toCamelCase(str, true, ' ');
  }
}
