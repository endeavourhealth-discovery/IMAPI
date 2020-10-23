package com.endavourhealth.legacy;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
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


	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	
	ViewerJDBCDAL dal = new ViewerJDBCDAL();
	
	public Connection getConnection() throws SQLException {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(url, username, password);
		return driverManagerDataSource.getConnection();
	}

	@GetMapping(value = "/Search")
	public List<Concept> search(@RequestParam("root") String root, @RequestParam("term") String term,
			@RequestParam(value = "relationship", required = false) List<String> relationships) throws SQLException {
		Connection conn = getConnection();
		List<Concept> result = dal.search(term, root, relationships, conn);
		conn.close();
		return result;
	}

	@GetMapping(value = "/{iri}")
	public Concept getConcept(@PathVariable("iri") String iri) throws SQLException {
		Connection conn = getConnection();
		Concept concept = dal.getConcept(iri, conn);
		conn.close();
		return concept;
	}

	@GetMapping(value = "/{iri}/Tree")
	public List<RelatedConcept> getTree(@PathVariable("iri") String iri, @RequestParam("root") String root,
			@RequestParam(value = "relationship", required = false) List<String> relationships) throws SQLException {
		Connection conn = getConnection();
		List<RelatedConcept> tree = dal.getTree(iri, root, relationships, conn);
		conn.close();
		return tree;
	}

	@GetMapping(value = "/{iri}/Properties")
	public List<Property> getProperties(@PathVariable("iri") String iri,
			@RequestParam(value = "inherited", required = false) Boolean inherited) throws SQLException, IOException {
		Connection conn = getConnection();
		List<Property> properties = dal.getProperties(iri, (inherited == null) ? false : inherited, conn);
		conn.close();
		return properties;
	}

	@GetMapping(value = "/{iri}/Textual")
	public String getTextual(@PathVariable("iri") String iri) throws Exception {
		Connection conn = getConnection();
		String textual = new TextFormat().get(iri, conn);
		conn.close();
		return textual;
	}

	@GetMapping(value = "/{iri}/Definition")
	public List<JsonNode> getDefinition(@PathVariable("iri") String iri) throws SQLException, IOException {
		Connection conn = getConnection();
		List<JsonNode> definition = dal.getAxioms(iri, conn);
		conn.close();
		return definition;
	}

	@GetMapping(value = "/{iri}/Sources")
	public PagedResultSet<RelatedConcept> getSources(@PathVariable("iri") String iri,
			@RequestParam(value = "relationship", required = false) List<String> relationships,
			@RequestParam("limit") Integer limit, @RequestParam("page") Integer page) throws SQLException {
		Connection conn = getConnection();
		PagedResultSet<RelatedConcept> sources = dal.getSources(iri, relationships, limit, page, conn);
		conn.close();
		return sources;
	}

	@GetMapping(value = "/{iri}/Targets")
	public PagedResultSet<RelatedConcept> getTargets(@PathVariable("iri") String iri,
			@RequestParam(value = "relationship", required = false) List<String> relationships,
			@RequestParam("limit") Integer limit, @RequestParam("page") Integer page) throws SQLException {
		Connection conn = getConnection();
		PagedResultSet<RelatedConcept> targets = dal.getTargets(iri, relationships, limit, page, conn);
		conn.close();
		return targets;
	}

	@GetMapping(value = "/{iri}/members")
	public List<ValueSetMember> getMembers(@PathVariable("iri") String iri) throws SQLException, IOException {
		Connection conn = getConnection();
		List<ValueSetMember> members = dal.getValueSetMembers(iri, conn);
		conn.close();
		return members;
	}

	@GetMapping(value = "/{iri}/childCountByScheme")
	public List<SchemeCount> getChildCountByScheme(@PathVariable("iri") String iri) throws SQLException {
		Connection conn = getConnection();
		List<SchemeCount> childCountByScheme = dal.getChildCountByScheme(iri, conn);
		conn.close();
		return childCountByScheme;
	}

	@GetMapping(value = "/{iri}/children")
	public List<SchemeChildren> getChildren(@PathVariable("iri") String iri, @RequestParam("scheme") String scheme)
			throws SQLException {
		Connection conn = getConnection();
		List<SchemeChildren> children = dal.getChildren(iri, scheme, conn);
		conn.close();
		return children;
	}

}
