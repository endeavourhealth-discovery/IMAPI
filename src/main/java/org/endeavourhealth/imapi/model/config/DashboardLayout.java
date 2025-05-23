package org.endeavourhealth.imapi.model.config;

public class DashboardLayout {
  private String size;
  private String type;
  private Number order;
  private String iri;

  public DashboardLayout() {
  }

  public DashboardLayout(String size, String type, Number order, String iri) {
    this.size = size;
    this.type = type;
    this.order = order;
    this.iri = iri;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Number getOrder() {
    return order;
  }

  public void setOrder(Number order) {
    this.order = order;
  }

  public String getIri() {
    return iri;
  }

  public void setIri(String iri) {
    this.iri = iri;
  }
}
