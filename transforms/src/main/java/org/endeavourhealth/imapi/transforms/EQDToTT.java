package org.endeavourhealth.imapi.transforms;

import org.apache.commons.text.CaseUtils;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.query.Query;
import org.endeavourhealth.imapi.query.QueryDefinition;
import org.endeavourhealth.imapi.query.QueryStep;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Converts an xml enquiry document to the Discovery Query model
 */
public class EQDToTT {
	private final Map<String,String> folderMap= new HashMap<>();
	private final Map<String,String> reportMap= new HashMap<>();
	private final Map<String,TTEntity> agentMap= new HashMap<>();

	private String baseIri;
	private String owningOrganisation;
	private TTDocument document;
	private XPath xPath;
	private Properties dataMap;


	/**
	 * Converts to the RDF query model using a set of builder objects
	 * @param documentStream representing the xml document
	 * @param dataMap a Properties file containing the data map
	 * @param ownerbaseIri the base iri for the owner of the document
	 * @param owningOrganisation the iri for the owning organisation
	 * @return a TTDocument containing the RDF storage format for the query
	 */
	public TTDocument convert(InputStream documentStream,
														Properties dataMap,
														String ownerbaseIri,String owningOrganisation) throws SAXException, ParserConfigurationException, IOException, XPathExpressionException, DataFormatException {
		this.baseIri = baseIri;
		this.dataMap= dataMap;
		this.owningOrganisation= owningOrganisation;
		document = new TTManager().createDocument(ownerbaseIri + "#");
		Document xmlDocument = parseDocument((documentStream));
		xPath = XPathFactory.newInstance().newXPath();
		convertFolders(getAsList(xmlDocument,"enquiryDocument/reportFolder"));
		convertReports(getAsList(xmlDocument,"enquiryDocument/report"));
		return document;
	}

	private void convertReports(Iterable<Node> reports) throws XPathExpressionException, DataFormatException {
		if (reports != null) {
			for (Node xmlReport:reports) {
				try {
					Query query = new Query();
					String uuid = getData(xmlReport, "id");
					String name = getData(xmlReport, "name");
					String description = getData(xmlReport, "description");
					String author= getData(xmlReport,"author/authorName");
					String iri = baseIri + "/query#" + CaseUtils.toCamelCase(name, true).replace(" ", "");
					String folderuuid= getData(xmlReport,"folder");
					String folder= folderMap.get(folderuuid);
					query.setIri(iri);
					query.addType(IM.QUERY);
					query.setName(name);
					if (description!=null)
						query.setDescription(description);
					if (author!=null)
						query.set(IM.HAS_AUTHOR,getPersonInRole(author));
					if (folder!=null)
						query.addObject(IM.IS_CONTAINED_IN,TTIriRef.iri(folder));
					convertDefinition(xmlReport,query);

					document.addEntity(query);
				} catch (NullPointerException e) {
					throw new DataFormatException("Report contains null name ");
				}
			}
		}
	}

	private void convertDefinition(Node xmlReport,Query query) throws DataFormatException {
		QueryDefinition def = query.setQueryDefinition();
		String mainEntity = dataMap.getProperty(getData(xmlReport, "populationType"));
		def.setMainEntity(TTIriRef.iri(mainEntity));
		Node population= getNode(xmlReport, "population");
		if (population!=null){
			Integer xmlGroupCount= ((Element) population).getElementsByTagName("criteriaGroup").getLength();
			for (Node xmlGroup : getAsList(xmlReport, "population/criteriaGroup")) {
				Node xmlDef = getNode(xmlGroup, "definition");
				convertSteps(xmlDef, def,xmlGroupCount);
			}
		}

	}

	private void convertSteps(Node xmlDef, QueryDefinition def,Integer groupCount) throws DataFormatException {
		String memberOperator= getData(xmlDef,"memberOperator");
		String actionTrue= getData(xmlDef,"actionIfTrue");
		String actionFalse= getData(xmlDef,"actionIfFalse");
		String table= getData(xmlDef,"table");
		String negation = getData(xmlDef,"negation");
		QueryStep step= def.addStep();


	}

	private void convertFolders(Iterable<Node> reportFolders) throws DataFormatException {
		if (reportFolders!=null){
			for (Node xmlFolder:reportFolders) {
				String uuid= getData(xmlFolder,"id");
				String folderName= getData(xmlFolder,"name");
				String authorName= getData(xmlFolder,"author/authorName");
				if (uuid==null)
					throw new DataFormatException("No folder id");
				TTEntity folder = new TTEntity();
				String iri= baseIri+"/folder#"+ CaseUtils.toCamelCase(folderName.replace(" ",""),true);
				document.addEntity(folder);
				folder.setIri(iri);
				folder.addType(IM.FOLDER);
				folderMap.put(uuid,folder.getIri());
				if (authorName!=null)
					folder.addObject(IM.HAS_AUTHOR,getPersonInRole(authorName));
				folder.setName(folderName);
			}
		}
	}




	private TTIriRef getPersonInRole(String name) {
		String agentIri= owningOrganisation.replace("org.","uir.")+"/personrole#"+
			CaseUtils.toCamelCase(name
				.replace(" ",""),true)
				.replace("(","_")
				.replace(")","_");
		TTEntity agent= agentMap.get(agentIri);
		if (agent==null) {
			agent = new TTEntity()
				.setIri(agentIri)
				.addType(TTIriRef.iri(IM.NAMESPACE + "PersonInRole"))
				.setName(name);
			agent.addObject(IM.HAS_ROLE_IN, TTIriRef.iri(owningOrganisation));
			document.addEntity(agent);
			agentMap.put(agentIri,agent);
			}
		return TTIriRef.iri(agent.getIri());
		}


	private Document parseDocument(InputStream documentStream) throws SAXException, ParserConfigurationException, IOException {
		DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
		//factory.setNamespaceAware(true);
		//factory.setValidating(true);
		//factory.setXIncludeAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(documentStream);
	}

	private Iterable<Node> getAsList(Object from,String xp) throws DataFormatException {
		try {
			NodeList nodeList = (NodeList) xPath.compile(xp)
				.evaluate(from, XPathConstants.NODESET);
			return () -> new Iterator<>() {
				private int index = 0;

				@Override
				public boolean hasNext() {
					return index < nodeList.getLength();
				}

				@Override
				public Node next() {
					if (!hasNext())
						throw new NoSuchElementException();
					return nodeList.item(index++);
				}
			};
		} catch (XPathExpressionException e) {
			throw new DataFormatException("Invalid xpath ("+ from+") "+e.getMessage());

		}
	}
	private Node getNode(Object from,String elementName) throws DataFormatException {
		try {
			return (Node) xPath.compile(elementName).evaluate(from, XPathConstants.NODE);
		} catch (XPathExpressionException e)
		{
			throw new DataFormatException("Invalid xpath ("+ from+") "+e.getMessage());
		}
	}
	private String getData(Object from,String elementName) throws DataFormatException {
		try {
			Node node = (Node) xPath.compile(elementName).evaluate(from, XPathConstants.NODE);
			if (node == null)
				return null;
			return node.getTextContent();
		} catch (XPathExpressionException e) {
			throw new DataFormatException("Invalid xpath ("+ from+") "+e.getMessage());
		}
	}
}


