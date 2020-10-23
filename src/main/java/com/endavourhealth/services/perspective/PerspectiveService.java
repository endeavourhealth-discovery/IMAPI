package com.endavourhealth.services.perspective;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.endavourhealth.services.perspective.models.Perspective;

@Service
public class PerspectiveService {

	@Autowired
	List<Perspective> perspectives;
	
	public Perspective getPerspective(String name) {
		Perspective match = null;
		
		Iterator<Perspective> perspectiveItr = perspectives.iterator();
		
		while(perspectiveItr.hasNext() && match == null) {
			Perspective current = perspectiveItr.next();
			if(current.getName().equals(name)) {
				match = current;
			}
		}
		
		return match;
	}
}
