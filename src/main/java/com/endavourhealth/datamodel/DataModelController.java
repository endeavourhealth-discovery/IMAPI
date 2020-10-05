package com.endavourhealth.datamodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.endavourhealth.datamodel.models.DataModel;

@RestController
@RequestMapping("/datamodel")
public class DataModelController {

	@Autowired
	DataModelService dataModelService;

	@GetMapping(value = "/{iri}")
	public DataModel getDataModel(@PathVariable("iri") String iri) {
		return dataModelService.getDataModel(iri);
	}

}
