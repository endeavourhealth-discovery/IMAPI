package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;

public class Range extends TTNode {

	public Range(){
		setPredicateTemplate(new TTIriRef[]{IM.FROM, IM.TO});
	}

	public Compare getFrom() {
		return (Compare) TTUtil.get(this,IM.FROM,Compare.class);
	}

	public Range setFrom(Compare from) {
		set(IM.FROM,from);
		return this;
	}


	public Compare getTo() {

		return (Compare) TTUtil.get(this,IM.TO,Compare.class);
	}

	public Range setTo(Compare to) {
		set(IM.TO,to);
		return this;
	}

}
