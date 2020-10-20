package com.endavourhealth.datamodel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.endavourhealth.datamodel.models.Ancestory;
import com.endavourhealth.datamodel.models.DataModel;
import com.endavourhealth.datamodel.models.DataModelDetail;
import com.endavourhealth.datamodel.models.Properties;

// TODO - discus with Dan - there's no summary endpoint. Is this concept no longer needed?
@RestController
@RequestMapping("/datamodel")
@CrossOrigin
public class DataModelController {

	@Autowired
	DataModelService dataModelService;

	@GetMapping(value = "/search")
	public List<DataModel> search(@RequestParam("term") String term) {
		return dataModelService.search(term);
	}

	@GetMapping(value = "/{iri}")
	public DataModelDetail getDataModel(@PathVariable("iri") String iri) {
		return dataModelService.getDataModelDetail(iri);
	}

	@GetMapping(value = "/{iri}/properties")
	public Properties getProperties(@PathVariable("iri") String iri) {
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

	@PostMapping("/")
	DataModelDetail newDataModel(@RequestBody DataModelDetail newDataModel) {
		// TODO
		return null;
	}

	@PutMapping("/{iri}")
	DataModelDetail replaceDataModel(@RequestBody DataModelDetail newEmployee, @PathVariable("iri") String iri) {
		// TODO
		return null;
	}

}
