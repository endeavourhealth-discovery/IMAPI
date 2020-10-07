package com.endavourhealth.datamodel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.endavourhealth.datamodel.models.Ancestory;
import com.endavourhealth.datamodel.models.DataModel;
import com.endavourhealth.datamodel.models.DataModelDetail;
import com.endavourhealth.datamodel.models.Property;

@RestController
@RequestMapping("/datamodel")
public class DataModelController {

	@Autowired
	DataModelService dataModelService;
	
	@GetMapping(value = "/search")
	public List<DataModel> search(@RequestParam("term") String term) {
		return dataModelService.search(term);
	}

	@GetMapping(value = "/{iri}")
	public DataModelDetail getDataModel(@PathVariable("iri") String iri) {
		return dataModelService.getDataModel(iri);
	}
	
	@GetMapping(value = "/{iri}/properties")
	public List<Property> getProperties(@PathVariable("iri") String iri) {
		return dataModelService.getDataModelProperties(iri);
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
