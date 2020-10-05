package com.endavourhealth.valueset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.endavourhealth.valueset.models.ValueSet;

@RestController
@RequestMapping("/Valueset")
public class ValueSetController {

	@Autowired
	ValueSetService ValueSetService;

	@GetMapping(value = "/{iri}")
	public ValueSet getValueSet(@PathVariable("iri") String iri) {
		return ValueSetService.getValueSet(iri);
	}

}
