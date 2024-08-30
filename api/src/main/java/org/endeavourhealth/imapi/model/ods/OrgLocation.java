package org.endeavourhealth.imapi.model.ods;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"AddrLn1", "AddrLn2", "AddrLn3", "Town", "County", "PostCode", "Country", "UPRN"})
public class OrgLocation {
  private String addrln1;
  private String addrln2;
  private String addrln3;
  private String town;
  private String county;
  private String postcode;
  private String country;
  private String uprn;

  @JsonProperty("AddrLn1")
  public String getAddrln1() {
    return addrln1;
  }

  public OrgLocation setAddrln1(String addrln1) {
    this.addrln1 = addrln1;
    return this;
  }

  @JsonProperty("AddrLn2")
  public String getAddrln2() {
    return addrln2;
  }

  public OrgLocation setAddrln2(String addrln2) {
    this.addrln2 = addrln2;
    return this;
  }

  @JsonProperty("AddrLn3")
  public String getAddrln3() {
    return addrln3;
  }

  public OrgLocation setAddrln3(String addrln3) {
    this.addrln3 = addrln3;
    return this;
  }

  @JsonProperty("Town")
  public String getTown() {
    return town;
  }

  public OrgLocation setTown(String town) {
    this.town = town;
    return this;
  }

  @JsonProperty("County")
  public String getCounty() {
    return county;
  }

  public OrgLocation setCounty(String county) {
    this.county = county;
    return this;
  }

  @JsonProperty("PostCode")
  public String getPostcode() {
    return postcode;
  }

  public OrgLocation setPostcode(String postcode) {
    this.postcode = postcode;
    return this;
  }

  @JsonProperty("Country")
  public String getCountry() {
    return country;
  }

  public OrgLocation setCountry(String country) {
    this.country = country;
    return this;
  }

  @JsonProperty("UPRN")
  public String getUprn() {
    return uprn;
  }

  public OrgLocation setUprn(String uprn) {
    this.uprn = uprn;
    return this;
  }
}
