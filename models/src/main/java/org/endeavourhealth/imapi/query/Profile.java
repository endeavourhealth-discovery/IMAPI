package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;

public class Profile extends TTEntity {

	public TTIriRef getEntityType(){
		return (TTIriRef) TTUtil.get(this,IM.ENTITY_TYPE,TTIriRef.class);
	}

	public Profile setEntityType(TTIriRef type){
		set(IM.ENTITY_TYPE,type);
		return this;
	}
	public Match getMatch(){
		return (Match) TTUtil.get(this,IM.DEFINITION, Match.class);
	}
	public Profile setMatch(Match match){
		set(IM.DEFINITION,match);
		return this;
	}

}
