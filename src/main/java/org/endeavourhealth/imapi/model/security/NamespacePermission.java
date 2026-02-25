package org.endeavourhealth.imapi.model.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.endeavourhealth.imapi.vocabulary.NAMESPACE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NamespacePermission {
  private NAMESPACE iri;
  private boolean read;
  private boolean write;
}
