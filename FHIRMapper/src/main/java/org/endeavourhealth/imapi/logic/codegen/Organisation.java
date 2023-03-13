package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents organisation
* A record of an organisation and its relationships. Named collections of people that have come together to achieve an objective
*/
public class Organisation extends IMDMBase<Organisation> {


	/**
	* Organisation constructor 
	*/
	public Organisation() {
		super("Organisation");
	}

	/**
	* Organisation constructor with identifier
	*/
	public Organisation(String id) {
		super("Organisation", id);
	}

	/**
	* Gets the address of this organisation
	* @return address
	*/
	public Address getAddress() {
		return getProperty("address");
	}


	/**
	* Changes the address of this Organisation
	* @param address The new address to set
	* @return Organisation
	*/
	public Organisation setAddress(Address address) {
		setProperty("address", address);
		return this;
	}


	/**
	* Gets the contact of this organisation
	* @return contact
	*/
	public String getContact() {
		return getProperty("contact");
	}


	/**
	* Changes the contact of this Organisation
	* @param contact The new contact to set
	* @return Organisation
	*/
	public Organisation setContact(String contact) {
		setProperty("contact", contact);
		return this;
	}


	/**
	* Gets the ODS code of this organisation
	* @return odsCode
	*/
	public String getOdsCode() {
		return getProperty("odsCode");
	}


	/**
	* Changes the ODS code of this Organisation
	* @param odsCode The new ODS code to set
	* @return Organisation
	*/
	public Organisation setOdsCode(String odsCode) {
		setProperty("odsCode", odsCode);
		return this;
	}


	/**
	* Gets the organisation/ service category of this organisation
	* @return organisationServiceCategory
	*/
	public String getOrganisationServiceCategory() {
		return getProperty("organisationServiceCategory");
	}


	/**
	* Changes the organisation/ service category of this Organisation
	* @param organisationServiceCategory The new organisation/ service category to set
	* @return Organisation
	*/
	public Organisation setOrganisationServiceCategory(String organisationServiceCategory) {
		setProperty("organisationServiceCategory", organisationServiceCategory);
		return this;
	}


	/**
	* Gets the speciality of this organisation
	* @return speciality
	*/
	public String getSpeciality() {
		return getProperty("speciality");
	}


	/**
	* Changes the speciality of this Organisation
	* @param speciality The new speciality to set
	* @return Organisation
	*/
	public Organisation setSpeciality(String speciality) {
		setProperty("speciality", speciality);
		return this;
	}


	/**
	* Gets the name of this organisation
	* @return name
	*/
	public String getName() {
		return getProperty("name");
	}


	/**
	* Changes the name of this Organisation
	* @param name The new name to set
	* @return Organisation
	*/
	public Organisation setName(String name) {
		setProperty("name", name);
		return this;
	}


	/**
	* Gets the operational start date of this organisation
	* @return operationalStartDate
	*/
	public PartialDateTime getOperationalStartDate() {
		return getProperty("operationalStartDate");
	}


	/**
	* Changes the operational start date of this Organisation
	* @param operationalStartDate The new operational start date to set
	* @return Organisation
	*/
	public Organisation setOperationalStartDate(PartialDateTime operationalStartDate) {
		setProperty("operationalStartDate", operationalStartDate);
		return this;
	}


	/**
	* Gets the operational end date of this organisation
	* @return operationalEndDate
	*/
	public PartialDateTime getOperationalEndDate() {
		return getProperty("operationalEndDate");
	}


	/**
	* Changes the operational end date of this Organisation
	* @param operationalEndDate The new operational end date to set
	* @return Organisation
	*/
	public Organisation setOperationalEndDate(PartialDateTime operationalEndDate) {
		setProperty("operationalEndDate", operationalEndDate);
		return this;
	}


	/**
	* Gets the legal start date of this organisation
	* @return legalStartDate
	*/
	public PartialDateTime getLegalStartDate() {
		return getProperty("legalStartDate");
	}


	/**
	* Changes the legal start date of this Organisation
	* @param legalStartDate The new legal start date to set
	* @return Organisation
	*/
	public Organisation setLegalStartDate(PartialDateTime legalStartDate) {
		setProperty("legalStartDate", legalStartDate);
		return this;
	}


	/**
	* Gets the legalend date of this organisation
	* @return legalendDate
	*/
	public PartialDateTime getLegalendDate() {
		return getProperty("legalendDate");
	}


	/**
	* Changes the legalend date of this Organisation
	* @param legalendDate The new legalend date to set
	* @return Organisation
	*/
	public Organisation setLegalendDate(PartialDateTime legalendDate) {
		setProperty("legalendDate", legalendDate);
		return this;
	}


	/**
	* Gets the has organisation role of this organisation
	* @return hasOrganisationRole
	*/
	public String getHasOrganisationRole() {
		return getProperty("hasOrganisationRole");
	}


	/**
	* Changes the has organisation role of this Organisation
	* @param hasOrganisationRole The new has organisation role to set
	* @return Organisation
	*/
	public Organisation setHasOrganisationRole(String hasOrganisationRole) {
		setProperty("hasOrganisationRole", hasOrganisationRole);
		return this;
	}


	/**
	* Gets the main location of this organisation
	* @return mainLocation
	*/
	public Location getMainLocation() {
		return getProperty("mainLocation");
	}


	/**
	* Changes the main location of this Organisation
	* @param mainLocation The new main location to set
	* @return Organisation
	*/
	public Organisation setMainLocation(Location mainLocation) {
		setProperty("mainLocation", mainLocation);
		return this;
	}


	/**
	* Gets the is operated by of this organisation
	* @return isOperatedBy
	*/
	public Organisation getIsOperatedBy() {
		return getProperty("isOperatedBy");
	}


	/**
	* Changes the is operated by of this Organisation
	* @param isOperatedBy The new is operated by to set
	* @return Organisation
	*/
	public Organisation setIsOperatedBy(Organisation isOperatedBy) {
		setProperty("isOperatedBy", isOperatedBy);
		return this;
	}


	/**
	* Gets the is located in the geography of of this organisation
	* @return isLocatedInTheGeographyOf
	*/
	public Organisation getIsLocatedInTheGeographyOf() {
		return getProperty("isLocatedInTheGeographyOf");
	}


	/**
	* Changes the is located in the geography of of this Organisation
	* @param isLocatedInTheGeographyOf The new is located in the geography of to set
	* @return Organisation
	*/
	public Organisation setIsLocatedInTheGeographyOf(Organisation isLocatedInTheGeographyOf) {
		setProperty("isLocatedInTheGeographyOf", isLocatedInTheGeographyOf);
		return this;
	}


	/**
	* Gets the is commissioned by of this organisation
	* @return isCommissionedBy
	*/
	public Organisation getIsCommissionedBy() {
		return getProperty("isCommissionedBy");
	}


	/**
	* Changes the is commissioned by of this Organisation
	* @param isCommissionedBy The new is commissioned by to set
	* @return Organisation
	*/
	public Organisation setIsCommissionedBy(Organisation isCommissionedBy) {
		setProperty("isCommissionedBy", isCommissionedBy);
		return this;
	}


	/**
	* Gets the is directed by of this organisation
	* @return isDirectedBy
	*/
	public Organisation getIsDirectedBy() {
		return getProperty("isDirectedBy");
	}


	/**
	* Changes the is directed by of this Organisation
	* @param isDirectedBy The new is directed by to set
	* @return Organisation
	*/
	public Organisation setIsDirectedBy(Organisation isDirectedBy) {
		setProperty("isDirectedBy", isDirectedBy);
		return this;
	}


	/**
	* Gets the is partner to of this organisation
	* @return isPartnerTo
	*/
	public Organisation getIsPartnerTo() {
		return getProperty("isPartnerTo");
	}


	/**
	* Changes the is partner to of this Organisation
	* @param isPartnerTo The new is partner to to set
	* @return Organisation
	*/
	public Organisation setIsPartnerTo(Organisation isPartnerTo) {
		setProperty("isPartnerTo", isPartnerTo);
		return this;
	}


	/**
	* Gets the is nominated payee for of this organisation
	* @return isNominatedPayeeFor
	*/
	public Organisation getIsNominatedPayeeFor() {
		return getProperty("isNominatedPayeeFor");
	}


	/**
	* Changes the is nominated payee for of this Organisation
	* @param isNominatedPayeeFor The new is nominated payee for to set
	* @return Organisation
	*/
	public Organisation setIsNominatedPayeeFor(Organisation isNominatedPayeeFor) {
		setProperty("isNominatedPayeeFor", isNominatedPayeeFor);
		return this;
	}


	/**
	* Gets the is covid nominated payee for of this organisation
	* @return isCovidNominatedPayeeFor
	*/
	public Organisation getIsCovidNominatedPayeeFor() {
		return getProperty("isCovidNominatedPayeeFor");
	}


	/**
	* Changes the is covid nominated payee for of this Organisation
	* @param isCovidNominatedPayeeFor The new is covid nominated payee for to set
	* @return Organisation
	*/
	public Organisation setIsCovidNominatedPayeeFor(Organisation isCovidNominatedPayeeFor) {
		setProperty("isCovidNominatedPayeeFor", isCovidNominatedPayeeFor);
		return this;
	}


	/**
	* Gets the is a subdivision of of this organisation
	* @return isASubdivisionOf
	*/
	public Organisation getIsASubdivisionOf() {
		return getProperty("isASubdivisionOf");
	}


	/**
	* Changes the is a subdivision of of this Organisation
	* @param isASubdivisionOf The new is a subdivision of to set
	* @return Organisation
	*/
	public Organisation setIsASubdivisionOf(Organisation isASubdivisionOf) {
		setProperty("isASubdivisionOf", isASubdivisionOf);
		return this;
	}
}

