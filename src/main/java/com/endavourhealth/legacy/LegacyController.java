package com.endavourhealth.legacy;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.endavourhealth.legacy.dal.ViewerJDBCDAL;
import com.endavourhealth.legacy.logic.TextFormat;
import com.endavourhealth.legacy.models.Concept;
import com.endavourhealth.legacy.models.PagedResultSet;
import com.endavourhealth.legacy.models.Property;
import com.endavourhealth.legacy.models.RelatedConcept;
import com.endavourhealth.legacy.models.SchemeChildren;
import com.endavourhealth.legacy.models.SchemeCount;
import com.endavourhealth.legacy.models.ValueSetMember;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/concepts")
@CrossOrigin
public class LegacyController {
	
	@GetMapping(value = "/Search")
	public List<Concept> search(@RequestParam("root") String root, @RequestParam("term") String term, @RequestParam("relationship") List<String> relationships) throws SQLException {
		ViewerJDBCDAL dal = new ViewerJDBCDAL();
		return dal.search(term, root, relationships);
	}
	
	@GetMapping(value = "/{iri}")
	public Concept getConcept(@PathVariable("iri") String iri) throws SQLException {
		ViewerJDBCDAL dal = new ViewerJDBCDAL();
		return dal.getConcept(iri);
	}
	
	@GetMapping(value = "/{iri}/Tree")
	public List<RelatedConcept> getTree(@PathVariable("iri") String iri, @RequestParam("root") String root, @RequestParam("relationship") List<String> relationships) throws SQLException {
		ViewerJDBCDAL dal = new ViewerJDBCDAL();
		return dal.getTree(iri, root, relationships);
	}
	
	@GetMapping(value = "/{iri}/Properties")
	public List<Property> getProperties(@PathVariable("iri") String iri, @RequestParam("inherited") Boolean inherited) throws SQLException, IOException {
		ViewerJDBCDAL dal = new ViewerJDBCDAL();
		return dal.getProperties(iri, (inherited == null) ? false : inherited);
	}
	
	@GetMapping(value = "/{iri}/Textual")
	public String getTextual(@PathVariable("iri") String iri) throws Exception {
		ViewerJDBCDAL dal = new ViewerJDBCDAL();
		return new TextFormat().get(iri);
	}
	
	@GetMapping(value = "/{iri}/Definition")
	public List<JsonNode> getDefinition(@PathVariable("iri") String iri) throws SQLException, IOException {
		ViewerJDBCDAL dal = new ViewerJDBCDAL();
		return dal.getAxioms(iri);
	}
	
	@GetMapping(value = "/{iri}/Sources")
	public PagedResultSet<RelatedConcept> getSources(@PathVariable("iri") String iri, @RequestParam("relationship") List<String> relationships, @RequestParam("limit") Integer limit, @RequestParam("page") Integer page) throws SQLException {
		ViewerJDBCDAL dal = new ViewerJDBCDAL();
		return dal.getSources(iri, relationships, limit, page);
	}
	
	@GetMapping(value = "/{iri}/Targets")
	public PagedResultSet<RelatedConcept> getTargets(@PathVariable("iri") String iri, @RequestParam("relationship") List<String> relationships, @RequestParam("limit") Integer limit, @RequestParam("page") Integer page) throws SQLException {
		ViewerJDBCDAL dal = new ViewerJDBCDAL();
		return dal.getTargets(iri, relationships, limit, page);
	}
	
	@GetMapping(value = "/{iri}/members")
	public List<ValueSetMember> getMembers(@PathVariable("iri") String iri) throws SQLException, IOException {
		ViewerJDBCDAL dal = new ViewerJDBCDAL();
		return dal.getValueSetMembers(iri);
	}
	
	@GetMapping(value = "/{iri}/childCountByScheme")
	public List<SchemeCount> getChildCountByScheme(@PathVariable("iri") String iri) throws SQLException {
		ViewerJDBCDAL dal = new ViewerJDBCDAL();
		return dal.getChildCountByScheme(iri);
	}
	
	@GetMapping(value = "/{iri}/children")
	public List<SchemeChildren> getChildren(@PathVariable("iri") String iri, @RequestParam("scheme") String scheme) throws SQLException {
		ViewerJDBCDAL dal = new ViewerJDBCDAL();
		return dal.getChildren(iri, scheme);
	}
	
}
