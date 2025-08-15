package org.endeavourhealth.imapi.model.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.vocabulary.Graph;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileDocumentRequest {
  private TTDocument document;
  private Graph insertGraph;
}
