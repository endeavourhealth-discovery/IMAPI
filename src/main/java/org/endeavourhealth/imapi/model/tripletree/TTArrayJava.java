package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.json.TTArrayDeserializer;
import org.endeavourhealth.imapi.json.TTArraySerializer;
import org.endeavourhealth.interfacemanager.model.TTArray;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Stream;

@JsonSerialize(using = TTArraySerializer.class)
@JsonDeserialize(using = TTArrayDeserializer.class)
public class TTArrayJava extends TTArray implements Serializable {
  private LinkedHashSet<TTValueJava> elementsAsSet = new LinkedHashSet<>();

  public TTArrayJava add(TTValueJava value) {
    if (elementsAsSet != null && elementsAsSet.contains(value))
      return this;

    if (elementsAsSet == null)
      elementsAsSet = new LinkedHashSet<>();

    elementsAsSet.add(value);

    return this;
  }

  public boolean isEmpty() {
    return elementsAsSet.isEmpty();
  }

  public int size() {
    return elementsAsSet.size();
  }

  public boolean contains(TTValueJava value) {
    return elementsAsSet.contains(value);
  }

  // Single element helpers
  public boolean isLiteral() {
    return elementsAsSet.size() == 1 && elementsAsSet.stream().findFirst().map(TTValueJava::isLiteral).orElse(false);
  }

  public TTLiteralJava asLiteral() {
    return (TTLiteralJava) elementsAsSet.stream().findFirst().orElse(null);
  }

  public boolean isIriRef() {
    return elementsAsSet.size() == 1 && elementsAsSet.stream().findFirst().map(TTValueJava::isIriRef).orElse(false);
  }

  public TTIriRef asIriRef() {
    return (TTIriRef) elementsAsSet.stream().findFirst().orElse(null);
  }

  public boolean isNode() {
    return elementsAsSet.size() == 1 && elementsAsSet.stream().findFirst().map(TTValueJava::isNode).orElse(false);
  }

  public TTNodeJava asNode() {
    return (TTNodeJava) elementsAsSet.stream().findFirst().orElse(null);
  }

  public TTValueJava asValue() {
    return elementsAsSet.stream().findFirst().orElse(null);
  }

  public Iterable<TTValueJava> iterator() {
    return elementsAsSet;
  }

  public TTValueJava get(int index) {
    return getElementsAsList().get(index);
  }

  public List<TTValueJava> getElementsAsList() {
    return new ArrayList<>(elementsAsSet);
  }

  public void remove(TTValueJava remove) {
    elementsAsSet.remove(remove);
  }

  public void clear() {
    elementsAsSet.clear();
  }

  public Stream<TTValueJava> stream() {
    return elementsAsSet.stream();
  }

  @Override
  public boolean equals(Object object) {
    if (getElements().size() == 1 && !(object instanceof TTArrayJava) && getElements().getFirst().equals(object))
      return true;


    return super.equals(object);
  }

}
