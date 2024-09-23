package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.codegen.IMDMBase;

import java.util.UUID;

/**
 * Represents patient.
 * A person in the role of a patient in the context of a single provider organisation. A person may therefore be a patient of several providers. each patient having a different provider level identifier
 */
public class Patient extends IMDMBase<Patient> {


  /**
   * Patient constructor with identifier
   */
  public Patient(UUID id) {
    super("Patient", id);
  }

  /**
   * Gets the stated gender of this patient
   *
   * @return statedGender
   */
  public String getStatedGender() {
    return getProperty("statedGender");
  }


  /**
   * Changes the stated gender of this Patient
   *
   * @param statedGender The new stated gender to set
   * @return Patient
   */
  public Patient setStatedGender(String statedGender) {
    setProperty("statedGender", statedGender);
    return this;
  }


  /**
   * Gets the contact of this patient
   *
   * @return contact
   */
  public String getContact() {
    return getProperty("contact");
  }


  /**
   * Changes the contact of this Patient
   *
   * @param contact The new contact to set
   * @return Patient
   */
  public Patient setContact(String contact) {
    setProperty("contact", contact);
    return this;
  }


  /**
   * Gets the death indicator of this patient
   *
   * @return deathIndicator
   */
  public String getDeathIndicator() {
    return getProperty("deathIndicator");
  }


  /**
   * Changes the death indicator of this Patient
   *
   * @param deathIndicator The new death indicator to set
   * @return Patient
   */
  public Patient setDeathIndicator(String deathIndicator) {
    setProperty("deathIndicator", deathIndicator);
    return this;
  }


  /**
   * Gets the ethnicity of this patient
   *
   * @return ethnicity
   */
  public String getEthnicity() {
    return getProperty("ethnicity");
  }


  /**
   * Changes the ethnicity of this Patient
   *
   * @param ethnicity The new ethnicity to set
   * @return Patient
   */
  public Patient setEthnicity(String ethnicity) {
    setProperty("ethnicity", ethnicity);
    return this;
  }


  /**
   * Gets the title of this patient
   *
   * @return title
   */
  public String getTitle() {
    return getProperty("title");
  }


  /**
   * Changes the title of this Patient
   *
   * @param title The new title to set
   * @return Patient
   */
  public Patient setTitle(String title) {
    setProperty("title", title);
    return this;
  }


  /**
   * Gets the calling name of this patient
   *
   * @return callingName
   */
  public String getCallingName() {
    return getProperty("callingName");
  }


  /**
   * Changes the calling name of this Patient
   *
   * @param callingName The new calling name to set
   * @return Patient
   */
  public Patient setCallingName(String callingName) {
    setProperty("callingName", callingName);
    return this;
  }


  /**
   * Gets the forenames of this patient
   *
   * @return forenames
   */
  public String getForenames() {
    return getProperty("forenames");
  }


  /**
   * Changes the forenames of this Patient
   *
   * @param forenames The new forenames to set
   * @return Patient
   */
  public Patient setForenames(String forenames) {
    setProperty("forenames", forenames);
    return this;
  }


  /**
   * Gets the family name of this patient
   *
   * @return familyName
   */
  public String getFamilyName() {
    return getProperty("familyName");
  }


  /**
   * Changes the family name of this Patient
   *
   * @param familyName The new family name to set
   * @return Patient
   */
  public Patient setFamilyName(String familyName) {
    setProperty("familyName", familyName);
    return this;
  }


  /**
   * Gets the language of this patient
   *
   * @return language
   */
  public String getLanguage() {
    return getProperty("language");
  }


  /**
   * Changes the language of this Patient
   *
   * @param language The new language to set
   * @return Patient
   */
  public Patient setLanguage(String language) {
    setProperty("language", language);
    return this;
  }


  /**
   * Gets the home address of this patient
   *
   * @return homeAddress
   */
  public String getHomeAddress() {
    return getProperty("homeAddress");
  }


  /**
   * Changes the home address of this Patient
   *
   * @param homeAddress The new home address to set
   * @return Patient
   */
  public Patient setHomeAddress(String homeAddress) {
    setProperty("homeAddress", homeAddress);
    return this;
  }


  /**
   * Gets the work address of this patient
   *
   * @return workAddress
   */
  public String getWorkAddress() {
    return getProperty("workAddress");
  }


  /**
   * Changes the work address of this Patient
   *
   * @param workAddress The new work address to set
   * @return Patient
   */
  public Patient setWorkAddress(String workAddress) {
    setProperty("workAddress", workAddress);
    return this;
  }


  /**
   * Gets the temporary address of this patient
   *
   * @return temporaryAddress
   */
  public String getTemporaryAddress() {
    return getProperty("temporaryAddress");
  }


  /**
   * Changes the temporary address of this Patient
   *
   * @param temporaryAddress The new temporary address to set
   * @return Patient
   */
  public Patient setTemporaryAddress(String temporaryAddress) {
    setProperty("temporaryAddress", temporaryAddress);
    return this;
  }


  /**
   * Gets the pDS sensitive of this patient
   *
   * @return pdsSensitive
   */
  public String getPdsSensitive() {
    return getProperty("pdsSensitive");
  }


  /**
   * Changes the pDS sensitive of this Patient
   *
   * @param pdsSensitive The new pDS sensitive to set
   * @return Patient
   */
  public Patient setPdsSensitive(String pdsSensitive) {
    setProperty("pdsSensitive", pdsSensitive);
    return this;
  }


  /**
   * Gets the related persons of this patient
   *
   * @return relatedPersons
   */
  public String getRelatedPersons() {
    return getProperty("relatedPersons");
  }


  /**
   * Changes the related persons of this Patient
   *
   * @param relatedPersons The new related persons to set
   * @return Patient
   */
  public Patient setRelatedPersons(String relatedPersons) {
    setProperty("relatedPersons", relatedPersons);
    return this;
  }


  /**
   * Gets the date of birth of this patient
   *
   * @return dateOfBirth
   */
  public String getDateOfBirth() {
    return getProperty("dateOfBirth");
  }


  /**
   * Changes the date of birth of this Patient
   *
   * @param dateOfBirth The new date of birth to set
   * @return Patient
   */
  public Patient setDateOfBirth(String dateOfBirth) {
    setProperty("dateOfBirth", dateOfBirth);
    return this;
  }


  /**
   * Gets the date of Death of this patient
   *
   * @return dateOfDeath
   */
  public String getDateOfDeath() {
    return getProperty("dateOfDeath");
  }


  /**
   * Changes the date of Death of this Patient
   *
   * @param dateOfDeath The new date of Death to set
   * @return Patient
   */
  public Patient setDateOfDeath(String dateOfDeath) {
    setProperty("dateOfDeath", dateOfDeath);
    return this;
  }


  /**
   * Gets the nHS Number of this patient
   *
   * @return nhsNumber
   */
  public String getNhsNumber() {
    return getProperty("nhsNumber");
  }


  /**
   * Changes the nHS Number of this Patient
   *
   * @param nhsNumber The new nHS Number to set
   * @return Patient
   */
  public Patient setNhsNumber(String nhsNumber) {
    setProperty("nhsNumber", nhsNumber);
    return this;
  }


  /**
   * Gets the Age of this patient
   *
   * @return age
   */
  public Integer getAge() {
    return getProperty("age");
  }


  /**
   * Changes the Age of this Patient
   *
   * @param age The new Age to set
   * @return Patient
   */
  public Patient setAge(Integer age) {
    setProperty("age", age);
    return this;
  }


  /**
   * Gets the home telephone number of this patient
   *
   * @return homeTelephoneNumber
   */
  public String getHomeTelephoneNumber() {
    return getProperty("homeTelephoneNumber");
  }


  /**
   * Changes the home telephone number of this Patient
   *
   * @param homeTelephoneNumber The new home telephone number to set
   * @return Patient
   */
  public Patient setHomeTelephoneNumber(String homeTelephoneNumber) {
    setProperty("homeTelephoneNumber", homeTelephoneNumber);
    return this;
  }


  /**
   * Gets the work telephone number of this patient
   *
   * @return workTelephoneNumber
   */
  public String getWorkTelephoneNumber() {
    return getProperty("workTelephoneNumber");
  }


  /**
   * Changes the work telephone number of this Patient
   *
   * @param workTelephoneNumber The new work telephone number to set
   * @return Patient
   */
  public Patient setWorkTelephoneNumber(String workTelephoneNumber) {
    setProperty("workTelephoneNumber", workTelephoneNumber);
    return this;
  }


  /**
   * Gets the mobile telephone number of this patient
   *
   * @return mobileTelephoneNumber
   */
  public String getMobileTelephoneNumber() {
    return getProperty("mobileTelephoneNumber");
  }


  /**
   * Changes the mobile telephone number of this Patient
   *
   * @param mobileTelephoneNumber The new mobile telephone number to set
   * @return Patient
   */
  public Patient setMobileTelephoneNumber(String mobileTelephoneNumber) {
    setProperty("mobileTelephoneNumber", mobileTelephoneNumber);
    return this;
  }


  /**
   * Gets the primary email of this patient
   *
   * @return primaryEmail
   */
  public String getPrimaryEmail() {
    return getProperty("primaryEmail");
  }


  /**
   * Changes the primary email of this Patient
   *
   * @param primaryEmail The new primary email to set
   * @return Patient
   */
  public Patient setPrimaryEmail(String primaryEmail) {
    setProperty("primaryEmail", primaryEmail);
    return this;
  }


  /**
   * Gets the secondary email of this patient
   *
   * @return secondaryEmail
   */
  public String getSecondaryEmail() {
    return getProperty("secondaryEmail");
  }


  /**
   * Changes the secondary email of this Patient
   *
   * @param secondaryEmail The new secondary email to set
   * @return Patient
   */
  public Patient setSecondaryEmail(String secondaryEmail) {
    setProperty("secondaryEmail", secondaryEmail);
    return this;
  }


  /**
   * Gets the gp current registration of this patient
   *
   * @return gpCurrentRegistration
   */
  public String getGpCurrentRegistration() {
    return getProperty("gpCurrentRegistration");
  }


  /**
   * Changes the gp current registration of this Patient
   *
   * @param gpCurrentRegistration The new gp current registration to set
   * @return Patient
   */
  public Patient setGpCurrentRegistration(String gpCurrentRegistration) {
    setProperty("gpCurrentRegistration", gpCurrentRegistration);
    return this;
  }


  /**
   * Gets the condition of this patient
   *
   * @return condition
   */
  public String getCondition() {
    return getProperty("condition");
  }


  /**
   * Changes the condition of this Patient
   *
   * @param condition The new condition to set
   * @return Patient
   */
  public Patient setCondition(String condition) {
    setProperty("condition", condition);
    return this;
  }
}

