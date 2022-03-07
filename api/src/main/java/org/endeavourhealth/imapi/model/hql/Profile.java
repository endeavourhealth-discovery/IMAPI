package org.endeavourhealth.imapi.model.hql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

public class Profile {
	TTIriRef id;
	String name;
	String description;
	private TTIriRef entityType;
	private List<Match> and;
	private List<Match> or;
	private List<Match> not;
	private Match match;



	public List<Match> getAnd() {
		return and;
	}

	public Profile setAnd(List<Match> and) {
		this.and = and;
		return this;
	}

	public Match addAnd(){
		return addAnd(new Match());
	}

	public Match addAnd(Match and){
		if (this.and==null)
			this.and= new ArrayList<>();
		this.and.add(and);
		return and;
	}

	public Match addOr(){
		return addOr(new Match());
	}

	public Match addOr(Match or){
		if (this.or==null)
			this.or= new ArrayList<>();
		this.or.add(or);
		return or;
	}





	@JsonIgnore
 public String getasJson() throws JsonProcessingException {
	 ObjectMapper objectMapper = new ObjectMapper();
	 objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	 objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
	 objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
	 return objectMapper.writeValueAsString(this);
 }



	public List<Match> getOr() {
		return or;
	}



	public TTIriRef getEntityType() {
		return entityType;
	}

	public Profile setEntityType(TTIriRef entityType) {
		this.entityType = entityType;
		return this;
	}


	public Match getMatch() {
		return match;
	}

	public Profile setMatch(Match match) {
		this.match = match;
		return this;
	}

	@JsonIgnore
	public Match setMatch() {
		this.match = new Match();
		return this.match;
	}

	public Profile setOr(List<Match> or) {
		this.or = or;
		return this;
	}

	public List<Match> getNot() {
		return not;
	}

	public Profile setNot(List<Match> not) {
		this.not = not;
		return this;
	}

	public Match addNot(){
		return addNot(new Match());
	}

	public Match addNot(Match not){
		if (this.not==null)
			this.not= new ArrayList<>();
		this.not.add(not);
		return not;
	}

	public String getName() {
		return name;
	}

	public Profile setName(String name) {
		this.name = name;
		return this;
	}

	public TTIriRef getId() {
		return id;
	}

	public Profile setId(TTIriRef id) {
		this.id = id;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Profile setDescription(String description) {
		this.description = description;
		return this;
	}
}
