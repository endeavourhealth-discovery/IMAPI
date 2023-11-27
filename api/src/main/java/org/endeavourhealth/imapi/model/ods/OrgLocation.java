package org.endeavourhealth.imapi.model.ods;

public class OrgLocation {
    private String addrln1;
    private String addrln2;
    private String addrln3;
    private String town;
    private String county;
    private String postcode;
    private String country;
    private String uprn;

    public String getAddrln1() {
        return addrln1;
    }

    public OrgLocation setAddrln1(String addrln1) {
        this.addrln1 = addrln1;
        return this;
    }

    public String getAddrln2() {
        return addrln2;
    }

    public OrgLocation setAddrln2(String addrln2) {
        this.addrln2 = addrln2;
        return this;
    }

    public String getAddrln3() {
        return addrln3;
    }

    public OrgLocation setAddrln3(String addrln3) {
        this.addrln3 = addrln3;
        return this;
    }

    public String getTown() {
        return town;
    }

    public OrgLocation setTown(String town) {
        this.town = town;
        return this;
    }

    public String getCounty() {
        return county;
    }

    public OrgLocation setCounty(String county) {
        this.county = county;
        return this;
    }

    public String getPostcode() {
        return postcode;
    }

    public OrgLocation setPostcode(String postcode) {
        this.postcode = postcode;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public OrgLocation setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getUprn() {
        return uprn;
    }

    public OrgLocation setUprn(String uprn) {
        this.uprn = uprn;
        return this;
    }
}
