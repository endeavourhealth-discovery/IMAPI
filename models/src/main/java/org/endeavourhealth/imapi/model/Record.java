package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Record extends Concept{


   public Record(){
      this.setConceptType(ConceptType.RECORD);
   }


}
