package org.endeavourhealth.imapi.transforms;

import org.apache.commons.text.CaseUtils;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Methods to read an xml file and create a graph model equivalent to an XSD schema in graph form
 */
public class XMLToTTShapes {

	private String namespace;
	private TTDocument document;
	private final Map<String, TTEntity> entityMap = new HashMap<>();
	private final Set<String> properties = new HashSet<>();
	private final Map<String,String> pathIriMap = new HashMap<>();
	private final Map<String,String> iriPathMap = new HashMap<>();
	private TTIriRef modelFolder;
	private static final Logger LOG = LoggerFactory.getLogger(XMLToTTShapes.class);

	/**
	 * Parses the xml from a file and produces data model as a shapes graph
	 * @param fileName name of the file
	 * @param graphName iri for the graph to be returned
	 * @param modelFolder the iri of the data model folder that the model is contained in
	 * @return TTDocument
	 */
	public TTDocument parseFromFile(String fileName, String graphName,String modelFolder ) throws IOException, XMLStreamException {

		this.modelFolder= TTIriRef.iri(modelFolder);
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(fileName));
		return parse(graphName,eventReader);

	}


	/**
	 * Parses the xml from a stream of characters and produces data model as a shapes graph
	 * @param xml the stream of xml
	 * @param graphName iri for the graph to be returned
	 * @param modelFolder the iri of the data model folder that the model is contained in
	 * @return TTDocument The shapes graph of entities
	 */
	public TTDocument parseFromStream(InputStream xml, String graphName, String modelFolder) throws XMLStreamException {
		this.modelFolder= TTIriRef.iri(modelFolder);
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory.createXMLEventReader(xml);
		return parse(graphName,eventReader);
	}

	/**
		* Parses xml using a stax parser and creates a data model in the form of a shapes graph
	 * @param graphName graph for the document
	 * @param eventReader event reader initialized

	 * @return TTDocument containing entities.
	 */
	private TTDocument parse(String graphName,XMLEventReader eventReader) throws XMLStreamException {
		createDocument(graphName);
		String path = "";
		TTEntity subject = null;
		TTEntity subSubject;
		int resourceCount=0;
		String elementName=null;
		boolean start=false;
		while (eventReader.hasNext()) {
			XMLEvent xmlEvent = eventReader.nextEvent();
			if (xmlEvent.isStartElement()) {
				if (!path.equals("")) {
					if (entityMap.get(path) == null) {
						subject = createSub(subject, path, elementName);
						entityMap.put(path, subject);
					}
					subject = entityMap.get(path);
				}

				StartElement startElement = xmlEvent.asStartElement();
				elementName = startElement.getName().getLocalPart();
				start=true;
				path = path + "/" + elementName;
				resourceCount++;
				if (resourceCount % 1000000 ==0)
					LOG.info(resourceCount + " "+ path);
				boolean hasAttributes = hasAttributes(startElement);
				if (hasAttributes) {
					if (entityMap.get(path) == null) {
						subSubject = createSub(subject, path, elementName);
						entityMap.put(path, subSubject);
					} else
						subSubject = entityMap.get(path);
					processAttributes(subSubject, startElement, path);
				}
			} else if (xmlEvent.isCharacters()) {
				if (start) {
					Characters dataEvent = (Characters) xmlEvent;
					String data = getData(dataEvent.getData());
					if (data != null) {
						if (!properties.contains(path)) {
							assert subject != null;
							setProperty(subject, elementName);
							properties.add(path);
						}
					}
					start=false;
				}
			} else if (xmlEvent.isEndElement()){
					path=getParentPath(path);
					if (!path.equals(""))
						subject= (entityMap.get(path));
				else
					subject=null;
				}
		}
		resolveDuplicates();
		if (!pathIriMap.isEmpty()){
			TTEntity pathMaps= new TTEntity();
			pathMaps.setIri(namespace+"XpathTypeMap");
			pathMaps.addType(IM.TEXT_MAPS);
			document.addEntity(pathMaps);
			for (Map.Entry<String,String> entry:pathIriMap.entrySet()){
				TTNode map= new TTNode();
				pathMaps.addObject(IM.HAS_MAP,map);
				map.set(IM.SOURCE_TEXT,TTLiteral.literal(entry.getKey()));
				map.set(IM.TARGET_TEXT,TTLiteral.literal(entry.getValue()));

			}
		}
		return document;
	}

	private void resolveDuplicates() {
		Map<String,TTEntity> iriToEntity= new HashMap<>();
		Set<TTEntity> duplicates = new HashSet<>();
		Map<String,String> duplicateIris= new HashMap<>();
		for (TTEntity entity:document.getEntities()){
			String iri= entity.getIri();
			if (!iri.contains("_"))
				iriToEntity.put(iri,entity);
		}
		for (TTEntity entity:document.getEntities()){
			String iri=  entity.getIri();
			if (iri.contains("_")){
				boolean duplicate=true;
				TTEntity original= iriToEntity.get(iri.substring(0,iri.indexOf("_")));
				if (entity.get(SHACL.PROPERTY)!=null){
					for (TTValue prop: entity.get(SHACL.PROPERTY).asArray().getElements()) {
						TTIriRef property = prop.asNode().get(SHACL.PATH).asIriRef();
						if (!hasProperty(original, property))
							duplicate = false;
					}

				}
				if (duplicate) {
					duplicates.add(entity);
					duplicateIris.put(entity.getIri(), original.getIri());
				}
			}
		}
		if (!duplicates.isEmpty()){
			for (TTEntity entity:duplicates)
				document.getEntities().remove(entity);
			for (TTEntity entity:document.getEntities()) {
				if (entity.get(SHACL.PROPERTY) != null) {
					for (TTValue prop : entity.get(SHACL.PROPERTY).asArray().getElements()) {
						if (prop.asNode().get(SHACL.NODE) != null) {
							TTIriRef nodeIriRef = prop.asNode().get(SHACL.NODE).asIriRef();
							String nodeiri = nodeIriRef.getIri();
							if (duplicateIris.get(nodeiri) != null)
								prop.asNode().set(SHACL.NODE, TTIriRef.iri(duplicateIris.get(nodeiri), nodeIriRef.getName()));
						}
					}
				}
			}
			Map<String,String> replacePaths= new HashMap<>();
			for (Map.Entry<String,String> entry:pathIriMap.entrySet()){
				String path=entry.getKey();
				String iri= entry.getValue();
				if (duplicateIris.get(namespace+ iri)!=null) {
					String duplicateIri= namespace+iri;
					String replaceIri= duplicateIris.get(duplicateIri).substring(duplicateIri.lastIndexOf("#")+1);
					replacePaths.put(path, replaceIri);
				}
			}
			if (!replacePaths.isEmpty()) {
				for (Map.Entry<String, String> replacement : replacePaths.entrySet()) {
					pathIriMap.put(replacement.getKey(), replacement.getValue());
				}
			}
		}
	}

	private boolean hasProperty(TTEntity entity, TTIriRef property){
		if (entity.get(SHACL.PROPERTY)!=null){
			for (TTValue prop:entity.get(SHACL.PROPERTY).asArray().getElements()){
				if (prop.asNode().get(SHACL.PATH).equals(property))
					return true;
			}
		}
		return false;
	}

	private String getParentPath(String childPath){
		int lastPath= childPath.lastIndexOf("/");
		if (lastPath==0)
			return "";
		else
			return (childPath.substring(0,childPath.lastIndexOf("/")));
	}

	private boolean hasAttributes(StartElement startElement){
		Iterator<Attribute> iterator = startElement.getAttributes();
		return iterator.hasNext();
	}

	private void processAttributes(TTEntity subject,StartElement startElement,String path) {
		if (subject!=null) {
			Iterator<Attribute> iterator = startElement.getAttributes();
			while (iterator.hasNext()) {
				Attribute attribute = iterator.next();
				String propertyName = CaseUtils.toCamelCase(attribute.getName().getLocalPart(),false);
				if (!properties.contains(path+"/"+ propertyName)){
					TTIriRef prop = TTIriRef.iri(getIri(propertyName, propertyName));
					TTNode property = new TTNode();
					subject.addObject(SHACL.PROPERTY, property);
					property.set(SHACL.PATH, prop);
					property.set(SHACL.MAXCOUNT, TTLiteral.literal(1));
					properties.add(path+"/"+ propertyName);
				}
			}
		}
	}

	private TTEntity createSub(TTEntity subject,String path,String elementName) {

		TTIriRef prop= TTIriRef.iri(namespace+ CaseUtils.toCamelCase(elementName,false));
		TTEntity object= createEntity(path,elementName);
		if (subject!=null) {
			TTNode property= new TTNode();
			subject.addObject(SHACL.PROPERTY,property);
			property.set(SHACL.PATH,prop);
			property.set(SHACL.NODE,TTIriRef.iri(object.getIri()));
		}
		//System.out.print("\n"+path);
		return object;
	}

	private void setProperty(TTEntity subject, String elementName){
		elementName=CaseUtils.toCamelCase(elementName,false);
		TTIriRef path= TTIriRef.iri(getIri(elementName,elementName));
		TTNode property = new TTNode();
		subject.addObject(SHACL.PROPERTY, property);
		property.set(SHACL.PATH, path);
		property.set(SHACL.MAXCOUNT, TTLiteral.literal(1));
	}

	private TTEntity createEntity(String path,String elementName) {
		if (entityMap.get(path)==null) {
			TTEntity entity = new TTEntity().setIri(getIri(path,elementName));
			entity.addType(SHACL.NODESHAPE);
			entity.setName(elementName);
			entity.set(IM.IS_CONTAINED_IN,new TTArray().add(modelFolder));
			document.addEntity(entity);
			entityMap.put(path,entity);
		}
		return entityMap.get(path);
	}

	private void createDocument(String graphName) {
		document = new TTDocument();
		namespace = graphName.substring(0,graphName.lastIndexOf("#"))+"#";
		String defaultPrefix= graphName.substring(graphName.lastIndexOf("#")+1);
		TTContext context = new TTContext();
		context.add(namespace, defaultPrefix);
		context.add(RDF.NAMESPACE, "rdf");
		context.add(RDFS.NAMESPACE, "rdfs");
		context.add(OWL.NAMESPACE, "owl");
		context.add(IM.NAMESPACE, "im");
		context.add(SHACL.NAMESPACE, "sh");

		document.setContext(context);
		document.setGraph(TTIriRef.iri(graphName));
	}

	private String getData(String data){
		if (data.replace("\n","").replace(" ","").equals("")){
			return null;
		} else
			return data;
	}

	private String getIri(String path,String elementName) {
		elementName=elementName.replace("_","-");
		if (pathIriMap.get(path)!=null)
			return namespace+ pathIriMap.get(path);
		else if (iriPathMap.get(elementName) == null) {
			iriPathMap.put(elementName, path);
			pathIriMap.put(path,elementName);
			return namespace + elementName;
		} else {
			int i = 0;
			String newIri = null;
			while (newIri == null) {
				i++;
				if (iriPathMap.get(elementName + "_" + i) == null) {
					iriPathMap.put(elementName + "_" + i, path);
					pathIriMap.put(path,elementName+"_"+i);
					newIri = namespace + (elementName + "_" + i);
				} else if (iriPathMap.get(elementName + "_" + i).equals(path)) {
					newIri = namespace + (elementName + "_" + i);
				}
			}
			return newIri;
		}
	}
}
