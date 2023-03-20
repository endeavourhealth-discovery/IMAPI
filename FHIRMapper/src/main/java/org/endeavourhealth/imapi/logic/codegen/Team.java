package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents team.
* Teams are named groups of individuals that are linked to one or more services
*/
public class Team extends IMDMBase<Team> {


	/**
	* Team constructor with identifier
	*/
	public Team(UUID id) {
		super("Team", id);
	}

	/**
	* Gets the organisation or services of this team
	* @return organisationOrServices
	*/
	public UUID getOrganisationOrServices() {
		return getProperty("organisationOrServices");
	}


	/**
	* Changes the organisation or services of this Team
	* @param organisationOrServices The new organisation or services to set
	* @return Team
	*/
	public Team setOrganisationOrServices(UUID organisationOrServices) {
		setProperty("organisationOrServices", organisationOrServices);
		return this;
	}


	/**
	* Gets the team members of this team
	* @return teamMembers
	*/
	public UUID getTeamMembers() {
		return getProperty("teamMembers");
	}


	/**
	* Changes the team members of this Team
	* @param teamMembers The new team members to set
	* @return Team
	*/
	public Team setTeamMembers(UUID teamMembers) {
		setProperty("teamMembers", teamMembers);
		return this;
	}


	/**
	* Gets the team name of this team
	* @return teamName
	*/
	public String getTeamName() {
		return getProperty("teamName");
	}


	/**
	* Changes the team name of this Team
	* @param teamName The new team name to set
	* @return Team
	*/
	public Team setTeamName(String teamName) {
		setProperty("teamName", teamName);
		return this;
	}
}

