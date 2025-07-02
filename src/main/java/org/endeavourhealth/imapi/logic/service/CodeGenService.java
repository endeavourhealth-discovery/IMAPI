package org.endeavourhealth.imapi.logic.service;

import org.apache.commons.text.WordUtils;
import org.endeavourhealth.imapi.dataaccess.CodeGenRepository;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.codegen.CodeGenTemplate;
import org.endeavourhealth.imapi.model.dto.CodeGenDto;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.EntityType;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.springframework.http.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CodeGenService {
  private final EntityService entityService = new EntityService();
  private final DataModelService dataModelService = new DataModelService();
  private final CodeGenRepository codeGenRepository = new CodeGenRepository();

  public List<String> getCodeTemplateList() {
    return codeGenRepository.getCodeTemplateList();
  }

  public CodeGenDto getCodeTemplate(String name) {
    return codeGenRepository.getCodeTemplate(name);
  }

  public void updateCodeTemplate(String name, String extension, String wrapper, Map<String, String> dataTypeMap, String template, Boolean complexTypes) {
    codeGenRepository.updateCodeTemplate(name, extension, wrapper, dataTypeMap, template, complexTypes);
  }

  public String generateCodeForModel(String modelIri, CodeGenDto template, String namespace, Graph graph) {
    TTIriRef model = getModelSummary(modelIri, graph);
    return generateCodeForModel(template, model, namespace);
  }

  public HttpEntity<Object> generateCode(String iri, String templateName, String namespace, Graph graph) {
    List<TTIriRef> models = (iri == null || iri.isEmpty())
      ? getIMModels(graph)
      : Collections.singletonList(getModelSummary(iri, graph));

    CodeGenDto template = codeGenRepository.getCodeTemplate(templateName);

    return createModelCodeZip(namespace, models, template);
  }

  private List<TTIriRef> getIMModels(Graph graph) {
    List<TTIriRef> models = entityService.getEntitiesByType(EntityType.NODESHAPE, graph);
    return models.stream()
      .filter(m -> m.getIri().startsWith(IM.NAMESPACE.toString()))
      .toList();
  }

  private TTIriRef getModelSummary(String iri, Graph graph) {
    SearchResultSummary summary = entityService.getSummary(iri, graph);
    return new TTIriRef(summary.getIri(), summary.getName()).setDescription(summary.getDescription());
  }

  private HttpEntity<Object> createModelCodeZip(String namespace, List<TTIriRef> models, CodeGenDto template) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      addModelsToZip(namespace, models, template, zos);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to create zip:" + e.getMessage());
    }
    String filename = models.getFirst().getName() + " " + LocalDate.now();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("application", "force-download"));
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + ".txt\"");
    return new HttpEntity<>(baos.toByteArray(), headers);
  }

  private void addModelsToZip(String namespace, List<TTIriRef> models, CodeGenDto template, ZipOutputStream zos) throws IOException {
    for (TTIriRef model : models) {
      String code = generateCodeForModel(template, model, namespace);

      ZipEntry entry = new ZipEntry(clean(toTitleCase(codify(model.getName()))) + template.getExtension());

      zos.putNextEntry(entry);
      zos.write(code.getBytes());
      zos.closeEntry();
    }
  }

  protected String generateCodeForModel(CodeGenDto template, TTIriRef model, String namespace) {
    List<DataModelProperty> properties = dataModelService.getDataModelProperties(model.getIri(), template.getComplexTypes());
    return generateCodeForModel(template, model, properties, namespace);
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
    if (ps_s < 0)
      return new CodeGenTemplate();

    int ps_l = propertyTemp.length();
    if ("\n".equals(template.substring(ps_s + ps_l, ps_s + ps_l + 1)))
      ps_l++;

    int pe_s = template.indexOf(propertyTempEnd, ps_s);
    int pe_l = propertyTempEnd.length();
    if (template.length() > pe_s + pe_l && "\n".equals(template.substring(pe_s + pe_l, pe_s + pe_l + 1)))
      pe_l++;

    int as_s = template.indexOf(arrayTemp);
    int as_l = arrayTemp.length();
    if (as_s > 0 && "\n".equals(template.substring(as_s + as_l, as_s + as_l + 1)))
      as_l++;

    int ae_s = template.indexOf(arrayTempEnd, as_s);

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

    if (namespace != null) result = replaceVariants(result, "NAME SPACE", namespace);

    if (model.hasName()) result = replaceVariants(result, "MODEL NAME", model.getName());

    if (model.hasDescription()) result = replaceVariants(result, "MODEL COMMENT", model.getDescription());

    if (prop != null && prop.hasProperty() && prop.getProperty().hasName() && prop.hasType()) {
      if (prop.hasType() && template.getDataType(prop.getType().getIri()) != null) {
        result = replaceTypedPropertyTokens(template, prop, result);
      } else if (prop.isArray() && template.hasCollectionWrapper()) {
        result = replaceArrayPropertyTokens(template, prop, result);
      } else {
        result = replaceRemainingPropertyTokens(prop, result);
      }

      result = replaceVariants(result, "PROPERTY NAME", prop.getProperty().getName());
    }

    return result;
  }

  private String replaceRemainingPropertyTokens(DataModelProperty prop, String result) {
    String basePropertyType = prop.getType().hasName() ? prop.getType().getName() : "!!UNKNOWN!!";
    result = replaceVariants(result, "DATA TYPE", basePropertyType);
    return result;
  }

  private String replaceArrayPropertyTokens(CodeGenDto template, DataModelProperty prop, String result) {
    if (!template.hasCollectionWrapper())
      return result;

    if (!template.getCollectionWrapper().contains("${"))
      result = replaceVariants(result, "DATA TYPE", template.getCollectionWrapper());
    else {
      String basePropertyType = prop.getType().hasName() ? prop.getType().getName() : "!!UNKNOWN!!";
      String cw = template.getCollectionWrapper();
      String cwType = cw.substring(cw.indexOf("${"), cw.indexOf("}") + 1);
      String placeholder = replaceVariants(template.getCollectionWrapper(), "BASE DATA TYPE", "${basedatatypeplaceholder}");
      result = replaceVariants(result, "DATA TYPE", placeholder);
      result = result.replace("${basedatatypeplaceholder}", cwType);
      result = replaceVariants(result, "BASE DATA TYPE", basePropertyType);
    }
    return result;
  }

  private String replaceTypedPropertyTokens(CodeGenDto template, DataModelProperty prop, String result) {
    String basePropertyType = template.getDataType(prop.getType().getIri());
    String propertyType =
      prop.isArray() && template.hasCollectionWrapper() ? replace(template.getCollectionWrapper(), "BASE DATA TYPE", basePropertyType) : basePropertyType;

    result = replace(result, "BASE DATA TYPE", basePropertyType);
    result = replace(result, "DATA TYPE", propertyType);
    return result;
  }

  private String replaceVariants(String template, String name, String value) {
    return template
      .replace("${" + name + "}", value)                                                // Unaltered
      .replace("${" + toTitleCase(name) + "}", toTitleCase(value))                      // Title Case
      .replace("${" + toCamelCase(name) + "}", toCamelCase(value))                      // camel Case
      .replace("${" + name.toLowerCase() + "}", value.toLowerCase())                    // lower case
      .replace("${" + clean(codify(name)) + "}", clean(codify(value)))                                // Codified
      .replace("${" + clean(toTitleCase(codify(name))) + "}", clean(toTitleCase(codify(value))))      // TitleCase (codified)
      .replace("${" + clean(toCamelCase(codify(name))) + "}", clean(toCamelCase(codify(value))))      // camelCase (codified)
      .replace("${" + clean(codify(name).toLowerCase()) + "}", clean(codify(value).toLowerCase()));   // lowercase (codified)
  }

  private String replace(String template, String name, String value) {
    return template
      .replace("${" + name + "}", value)
      .replace("${" + clean(name) + "}", value)
      .replace("${" + name.toLowerCase() + "}", value)
      .replace("${" + clean(name.toLowerCase()) + "}", value)
      .replace("${" + toCamelCase(name) + "}", value)
      .replace("${" + clean(toCamelCase(name)) + "}", value)
      .replace("${" + toTitleCase(name) + "}", value)
      .replace("${" + clean(toTitleCase(name)) + "}", value);
  }

  private String clean(String name) {
    return name.replace(" ", "");
  }

  private String codify(String name) {
    return name
      .replace("-", " ")
      .replace(".", " ")
      .replace(",", " ")
      .replace("#", " ")
      .replace("'", " ")
      .replace("(", " ")
      .replace(")", " ")
      .replace("\"", " ")
      .replace("/", " ");
  }

  private String toCamelCase(String str) {
    str = toTitleCase(str);
    str = str.substring(0, 1).toLowerCase() + str.substring(1);
    return str;
  }

  private String toTitleCase(String str) {
    return WordUtils.capitalizeFully(str);
  }
}
