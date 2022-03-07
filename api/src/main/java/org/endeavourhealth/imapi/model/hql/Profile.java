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
	private TTIriRef entityType;
	private List<Match> and;
	private List<Match> or;
	Match not;
	private Match match;

	public List<Match> getAnd() {
		return and;
	}

	public Profile setAnd(List<Match> and) {
		this.and = and;
		return this;
	}

	public Match addAnd(){
		if (this.and==null)
			this.and= new ArrayList<>();
		Match newAnd= new Match();
		and.add(newAnd);
		return newAnd;
	}

	public Profile setOr(List<Match> or) {
		this.or = or;
		return this;
	}

	public Match addOr(){
		if (this.or==null)
			this.or= new ArrayList<>();
		Match newOr= new Match();
		and.add(newOr);
		return newOr;
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

	public Match getNot() {
		return not;
	}

	public TTIriRef getEntityType() {
		return entityType;
	}

	public Profile setEntityType(TTIriRef entityType) {
		this.entityType = entityType;
		return this;
	}

	public Match createMatch() {
		this.match = new Match();
		return this.match;
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


	public Profile setNot(Match not) {
		this.not = not;
		return this;
	}

}
