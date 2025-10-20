package org.endeavourhealth.imapi.model.iml;

import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IMLLanguage {
  @Getter
  private String text;
  @Getter
  private String lang;
  @Getter
  private Map<String,String> info = new HashMap<>();
  private Map<String,String> definitions = new HashMap<>();
  private Map<String,String> prefixes = new HashMap<>();
  @Getter
  private Set<String> keywords= new HashSet<>();
  private Map<String,Set<String>> iriVariables= new HashMap<>();

  public Map<String, Set<String>> getIriVariables() {
    return iriVariables;
  }
  public IMLLanguage setIriVariables(Map<String, Set<String>> iriVariables) {
    this.iriVariables = iriVariables;
    return this;
  }
  public IMLLanguage setText(String text) {
    this.text = text;
    return this;
  }

  public IMLLanguage setLang(String lang) {
    this.lang = lang;
    return this;
  }

  public IMLLanguage setInfo(Map<String, String> info) {
    this.info = info;
    return this;
  }

  public Map<String, String> getDefinitions() {
    return definitions;
  }

  public IMLLanguage setDefinitions(Map<String, String> definitions) {
    this.definitions = definitions;
    return this;
  }

  public IMLLanguage setKeywords(Set<String> keywords) {
    this.keywords = keywords;
    return this;
  }
  public Map<String, String> getPrefixes() {
    return prefixes;
  }
  public IMLLanguage setPrefixes(Map<String, String> prefixes) {
    this.prefixes = prefixes;
    return this;
  }
}
