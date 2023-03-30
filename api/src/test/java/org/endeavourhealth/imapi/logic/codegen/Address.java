package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents address.
 * A standard mailing/postal address
 */
public class Address extends IMDMBase<Address> {


    /**
     * Address constructor with identifier
     */
    public Address(UUID id) {
        super("Address", id);
    }

    /**
     * Gets the address line of this address
     * @return addressLine
     */
    public String getAddressLine() {
        return getProperty("addressLine");
    }


    /**
     * Changes the address line of this Address
     * @param addressLine The new address line to set
     * @return Address
     */
    public Address setAddressLine(String addressLine) {
        setProperty("addressLine", addressLine);
        return this;
    }


    /**
     * Gets the Locality of this address
     * @return locality
     */
    public String getLocality() {
        return getProperty("locality");
    }


    /**
     * Changes the Locality of this Address
     * @param locality The new Locality to set
     * @return Address
     */
    public Address setLocality(String locality) {
        setProperty("locality", locality);
        return this;
    }


    /**
     * Gets the Region of this address
     * @return region
     */
    public String getRegion() {
        return getProperty("region");
    }


    /**
     * Changes the Region of this Address
     * @param region The new Region to set
     * @return Address
     */
    public Address setRegion(String region) {
        setProperty("region", region);
        return this;
    }


    /**
     * Gets the city of this address
     * @return city
     */
    public String getCity() {
        return getProperty("city");
    }


    /**
     * Changes the city of this Address
     * @param city The new city to set
     * @return Address
     */
    public Address setCity(String city) {
        setProperty("city", city);
        return this;
    }


    /**
     * Gets the country of this address
     * @return country
     */
    public String getCountry() {
        return getProperty("country");
    }


    /**
     * Changes the country of this Address
     * @param country The new country to set
     * @return Address
     */
    public Address setCountry(String country) {
        setProperty("country", country);
        return this;
    }


    /**
     * Gets the Post Code of this address
     * @return postCode
     */
    public String getPostCode() {
        return getProperty("postCode");
    }


    /**
     * Changes the Post Code of this Address
     * @param postCode The new Post Code to set
     * @return Address
     */
    public Address setPostCode(String postCode) {
        setProperty("postCode", postCode);
        return this;
    }


    /**
     * Gets the location of this address
     * @return location
     */
    public String getLocation() {
        return getProperty("location");
    }


    /**
     * Changes the location of this Address
     * @param location The new location to set
     * @return Address
     */
    public Address setLocation(String location) {
        setProperty("location", location);
        return this;
    }
}

