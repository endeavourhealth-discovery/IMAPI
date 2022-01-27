package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;

public class Profile extends TTEntity {
	public Match getMatch(){
		return (Match) TTUtil.get(this,IM.DEFINITION, Match.class);
	}
	public Profile setMatch(Match match){
		set(IM.DEFINITION,match);
		return this;
	}

}
