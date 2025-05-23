package org.endeavourhealth.imapi.model.dto;

import java.util.Date;
import java.util.List;

public class RecentActivityItemDto {

  private String iri;
  private String name;
  private String type;
  private Date dateTime;
  private String action;
  private String color;
  private List<String> icon;

  public RecentActivityItemDto() {
  }

  public String getIri() {
    return iri;
  }

  public void setIri(String iri) {
    this.iri = iri;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Date getDateTime() {
    return dateTime;
  }

  public void setDateTime(Date dateTime) {
    this.dateTime = dateTime;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public List<String> getIcon() {
    return icon;
  }

  public void setIcon(List<String> icon) {
    this.icon = icon;
  }
}
