package org.endeavourhealth.imapi.model.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.endeavourhealth.imapi.vocabulary.Namespace;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NamespacePermission {
  private String iri;
  private boolean read;
  private boolean write;
}
