package org.endeavourhealth.imapi.model.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class EntityDocument {
	Integer id;
	String iri;
	String name;
	String code;
	Set<String> key= new HashSet<>();
	TTIriRef scheme;
	Set<TTIriRef> entityType = new HashSet<>();
	TTIriRef status;
	Set<SearchTermCode> termCode = new HashSet<>();
	Integer weighting;
	String match;

	private List<TTIriRef> isDescendentOf = new ArrayList<>();


	public Integer getId() {
		return id;
	}

	public EntityDocument setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getIri() {
		return iri;
	}

	public EntityDocument setIri(String iri) {
		this.iri = iri;
		return this;
	}

	public String getName() {
		return name;
	}

	public EntityDocument setName(String name) {
		this.name = name;
		return this;
	}

	public String getCode() {
		return code;
	}

	public EntityDocument setCode(String code) {
		this.code = code;
		return this;
	}

	public TTIriRef getScheme() {
		return scheme;
	}

	public EntityDocument setScheme(TTIriRef scheme) {
		this.scheme = scheme;
		return this;
	}



	public TTIriRef getStatus() {
		return status;
	}

	public EntityDocument setStatus(TTIriRef status) {
		this.status = status;
		return this;
	}

	public EntityDocument addType(TTIriRef type) {
		this.entityType.add(type);
		return this;
	}



	public Integer getWeighting() {
		return weighting;
	}

	public EntityDocument setWeighting(Integer weighting) {
		this.weighting = weighting;
		return this;
	}




	public List<TTIriRef> getIsDescendentOf() {
		return isDescendentOf;
	}

	public EntityDocument setIsDescendentOf(List<TTIriRef> isDescendentOf) {
		this.isDescendentOf = isDescendentOf;
		return this;
	}

	public Set<TTIriRef> getEntityType() {
		return entityType;
	}

	public EntityDocument setEntityType(Set<TTIriRef> entityType) {
		this.entityType = entityType;
		return this;
	}


	public Set<SearchTermCode> getTermCode() {
		return termCode;
	}

	public EntityDocument setTermCode(Set<SearchTermCode> searchTermCode) {
		this.termCode = searchTermCode;
		return this;
	}

	public EntityDocument addTermCode(String term, String code, TTIriRef status){

		SearchTermCode tc= new SearchTermCode();
		tc.setTerm(term).setCode(code).setStatus(status);
		this.termCode.add(tc);
		return this;
	}





	public String getMatch() {
		return match;
	}

	public EntityDocument setMatch(String match) {
		this.match = match;
		return this;
	}

	public Set<String> getKey() {
		return key;
	}

	public EntityDocument setKey(Set<String> keys) {
		this.key = keys;
		return this;
	}

	public EntityDocument addKey(String key){
		this.key.add(key);
		return this;
	}
}
