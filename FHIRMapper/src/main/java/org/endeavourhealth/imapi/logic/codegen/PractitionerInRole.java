package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents practitioner in role
* This term refers to any person who provides care or is part of the health care process, excluding personally engaged or family carers.<p>A practitioner in role means a person providing care in the context of an organisation or service. The same person may have a number of roles across a number of organisations in which case it is expected that several entries may be created.
*/
public class PractitionerInRole extends IMDMBase<PractitionerInRole> {


	/**
	* Practitioner in role constructor 
	*/
	public PractitionerInRole() {
		super("PractitionerInRole");
	}

	/**
	* Practitioner in role constructor with identifier
	*/
	public PractitionerInRole(String id) {
		super("PractitionerInRole", id);
	}

	/**
	* Gets the active/ Inactive of this practitioner in role
	* @return activeInactive
	*/
	public String getActiveInactive() {
		return getProperty("activeInactive");
	}


	/**
	* Changes the active/ Inactive of this PractitionerInRole
	* @param activeInactive The new active/ Inactive to set
	* @return PractitionerInRole
	*/
	public PractitionerInRole setActiveInactive(String activeInactive) {
		setProperty("activeInactive", activeInactive);
		return this;
	}


	/**
	* Gets the contact of this practitioner in role
	* @return contact
	*/
	public String getContact() {
		return getProperty("contact");
	}


	/**
	* Changes the contact of this PractitionerInRole
	* @param contact The new contact to set
	* @return PractitionerInRole
	*/
	public PractitionerInRole setContact(String contact) {
		setProperty("contact", contact);
		return this;
	}


	/**
	* Gets the contract period of this practitioner in role
	* @return contractPeriod
	*/
	public String getContractPeriod() {
		return getProperty("contractPeriod");
	}


	/**
	* Changes the contract period of this PractitionerInRole
	* @param contractPeriod The new contract period to set
	* @return PractitionerInRole
	*/
	public PractitionerInRole setContractPeriod(String contractPeriod) {
		setProperty("contractPeriod", contractPeriod);
		return this;
	}


	/**
	* Gets the title of this practitioner in role
	* @return title
	*/
	public String getTitle() {
		return getProperty("title");
	}


	/**
	* Changes the title of this PractitionerInRole
	* @param title The new title to set
	* @return PractitionerInRole
	*/
	public PractitionerInRole setTitle(String title) {
		setProperty("title", title);
		return this;
	}


	/**
	* Gets the family name of this practitioner in role
	* @return familyName
	*/
	public String getFamilyName() {
		return getProperty("familyName");
	}


	/**
	* Changes the family name of this PractitionerInRole
	* @param familyName The new family name to set
	* @return PractitionerInRole
	*/
	public PractitionerInRole setFamilyName(String familyName) {
		setProperty("familyName", familyName);
		return this;
	}


	/**
	* Gets the calling name of this practitioner in role
	* @return callingName
	*/
	public String getCallingName() {
		return getProperty("callingName");
	}


	/**
	* Changes the calling name of this PractitionerInRole
	* @param callingName The new calling name to set
	* @return PractitionerInRole
	*/
	public PractitionerInRole setCallingName(String callingName) {
		setProperty("callingName", callingName);
		return this;
	}


	/**
	* Gets the gender of this practitioner in role
	* @return gender
	*/
	public String getGender() {
		return getProperty("gender");
	}


	/**
	* Changes the gender of this PractitionerInRole
	* @param gender The new gender to set
	* @return PractitionerInRole
	*/
	public PractitionerInRole setGender(String gender) {
		setProperty("gender", gender);
		return this;
	}


	/**
	* Gets the identifiers of this practitioner in role
	* @return identifiers
	*/
	public String getIdentifiers() {
		return getProperty("identifiers");
	}


	/**
	* Changes the identifiers of this PractitionerInRole
	* @param identifiers The new identifiers to set
	* @return PractitionerInRole
	*/
	public PractitionerInRole setIdentifiers(String identifiers) {
		setProperty("identifiers", identifiers);
		return this;
	}


	/**
	* Gets the role type of this practitioner in role
	* @return roleType
	*/
	public String getRoleType() {
		return getProperty("roleType");
	}


	/**
	* Changes the role type of this PractitionerInRole
	* @param roleType The new role type to set
	* @return PractitionerInRole
	*/
	public PractitionerInRole setRoleType(String roleType) {
		setProperty("roleType", roleType);
		return this;
	}


	/**
	* Gets the service or organisation of this practitioner in role
	* @return serviceOrOrganisation
	*/
	public String getServiceOrOrganisation() {
		return getProperty("serviceOrOrganisation");
	}


	/**
	* Changes the service or organisation of this PractitionerInRole
	* @param serviceOrOrganisation The new service or organisation to set
	* @return PractitionerInRole
	*/
	public PractitionerInRole setServiceOrOrganisation(String serviceOrOrganisation) {
		setProperty("serviceOrOrganisation", serviceOrOrganisation);
		return this;
	}


	/**
	* Gets the speciality of this practitioner in role
	* @return speciality
	*/
	public String getSpeciality() {
		return getProperty("speciality");
	}


	/**
	* Changes the speciality of this PractitionerInRole
	* @param speciality The new speciality to set
	* @return PractitionerInRole
	*/
	public PractitionerInRole setSpeciality(String speciality) {
		setProperty("speciality", speciality);
		return this;
	}


	/**
	* Gets the work address of this practitioner in role
	* @return workAddress
	*/
	public String getWorkAddress() {
		return getProperty("workAddress");
	}


	/**
	* Changes the work address of this PractitionerInRole
	* @param workAddress The new work address to set
	* @return PractitionerInRole
	*/
	public PractitionerInRole setWorkAddress(String workAddress) {
		setProperty("workAddress", workAddress);
		return this;
	}


	/**
	* Gets the date of birth of this practitioner in role
	* @return dateOfBirth
	*/
	public String getDateOfBirth() {
		return getProperty("dateOfBirth");
	}


	/**
	* Changes the date of birth of this PractitionerInRole
	* @param dateOfBirth The new date of birth to set
	* @return PractitionerInRole
	*/
	public PractitionerInRole setDateOfBirth(String dateOfBirth) {
		setProperty("dateOfBirth", dateOfBirth);
		return this;
	}
}

