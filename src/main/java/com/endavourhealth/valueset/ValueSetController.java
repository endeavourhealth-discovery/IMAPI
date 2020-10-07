package com.endavourhealth.valueset;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.endavourhealth.datamodel.models.Ancestory;
import com.endavourhealth.datamodel.models.DataModelDetail;
import com.endavourhealth.valueset.models.ValueSet;
import com.endavourhealth.valueset.models.ValueSetDetail;

@RestController
@RequestMapping("/Valueset")
public class ValueSetController {

	@Autowired
	ValueSetService valueSetService;
	
	@GetMapping(value = "/search")
	public List<ValueSet> search(@RequestParam("term") String term) {
		return valueSetService.search(term);
	}

	@GetMapping(value = "/{iri}")
	public ValueSetDetail getValueSet(@PathVariable("iri") String iri) {
		return valueSetService.getValueSet(iri);
	}
	
	@GetMapping(value = "/{iri}/members")
	public List<String> getMembers(@PathVariable("iri") String iri) {
		List<String> members = null;
		return members;
	}
	
	@GetMapping(value = "/{iri}/parents")
	public List<Ancestory> getParents(@PathVariable("iri") String iri) {
		List<Ancestory> parents = null;
		return parents;
	}
	
	@GetMapping(value = "/{iri}/children")
	public List<DataModelDetail> getChildren(@PathVariable("iri") String iri) {
		List<DataModelDetail> children = null;
		return children;
	}

}
