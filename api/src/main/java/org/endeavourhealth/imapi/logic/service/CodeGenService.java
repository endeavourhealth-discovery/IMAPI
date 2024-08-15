package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.CodeGenRepository;
import org.endeavourhealth.imapi.model.dto.CodeGenDto;

import java.util.List;

public class CodeGenService {
  private CodeGenRepository codeGenRepository = new CodeGenRepository();

  public List<String> getCodeTemplateList() throws JsonProcessingException {
    return codeGenRepository.getCodeTemplateList();
  }

  public CodeGenDto getCodeTemplate(String name) throws JsonProcessingException {
    return codeGenRepository.getCodeTemplate(name);
  }

  public void updateCodeTemplate(String name, String extension, String wrapper, String dataTypeMap, String template) {
    codeGenRepository.updateCodeTemplate(name, extension, wrapper, dataTypeMap, template);
  }
}
