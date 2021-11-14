package org.endeavourhealth.imapi.transforms;

import org.apache.commons.text.CaseUtils;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Methods involving XML to triple parser, using a listener if needed to enable partial incremental filing or display
 * of example data.
 */
public class XMLToTT {
	private XMLEventReader eventReader;
	private String namespace;
	private TTDocument schema;
	private TTManager manager;
	private TTDocument document;
	private XMLToTTListener listener;
	private Map<String, TTEntity> entityMap = new HashMap<>();
	private final Set<String> properties = new HashSet<>();
	private static final Logger LOG = LoggerFactory.getLogger(XMLToTTShapes.class);

	/**
	 * Parses xml from a file using a shapes graph that includes an entity XPath map as the data model
	 * @param fileName name of the file
	 * @param schema the shapes graph data model of the xml
	 * @param resourcePath an xpath of resources to file if null the entire document is parsed
	 * @param offset the starting number of the resource for paging null starts at the first resource
	 * @param limit the page count i.e. number of resources returned
	 * @return TTDocument
	 */
	public TTDocument parseFromFile(String fileName, TTDocument schema, String resourcePath,int offset, int limit) throws IOException, XMLStreamException {

		XMLInputFactory factory = XMLInputFactory.newInstance();
		eventReader = factory.createXMLEventReader(new FileReader(fileName));
		return parse(schema,resourcePath,eventReader,offset,limit);

	}




	private TTDocument parse(TTDocument schema,String resourcePath, XMLEventReader eventReader,int offset, int limit) throws XMLStreamException {
		if (schema!=null)
			this.schema=schema;
		namespace=schema.getGraph().getIri();
		manager= new TTManager();
		String path = "";
		buildPathMap();
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
				if (listener!=null)
					listener.startElement(path,subject,document);
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
							if (subject!=null) {
								setProperty(subject, elementName);
								properties.add(path);
							}
						}
					}
					start=false;
				}
			} else if (xmlEvent.isEndElement()){
				if (listener!=null)
					listener.endElement(path,subject,document);
				path=getParentPath(path);
				if (!path.equals(""))
					subject= (entityMap.get(path));
				else
					subject=null;
			}
		}
		return document;
	}

	private void buildPathMap() {
		TTEntity schemaPaths= manager.getEntity(namespace+"XpathMap");
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
				QName name = attribute.getName();
				if (!properties.contains(path+"/"+ name.getLocalPart())){
					TTIriRef prop = TTIriRef.iri(createIri(name.getLocalPart()), name.getLocalPart());
					TTNode property = new TTNode();
					subject.addObject(SHACL.PROPERTY, property);
					property.set(SHACL.PATH, prop);
					property.set(SHACL.MAXCOUNT, TTLiteral.literal(1));
					properties.add(path+"/"+ name.getLocalPart());
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

	private TTIriRef setProperty(TTEntity subject,String elementName){
		TTIriRef path= TTIriRef.iri(createIri(elementName));
		TTNode property = new TTNode();
		subject.addObject(SHACL.PROPERTY, property);
		property.set(SHACL.PATH, path);
		property.set(SHACL.MAXCOUNT, TTLiteral.literal(1));
		return path;
	}

	private TTEntity createEntity(String path,String elementName) {
		if (entityMap.get(path)==null) {
			TTEntity entity = new TTEntity().setIri(createIri(path));
			entity.addType(SHACL.NODESHAPE);
			entity.setName(elementName);
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

	private String createIri(String path) {
		return (namespace+ path);
	}



	private String getParentPath(String childPath){
		int lastPath= childPath.lastIndexOf("/");
		if (lastPath==0)
			return "";
		else
			return (childPath.substring(0,childPath.lastIndexOf("/")));
	}

	public Map<String, TTEntity> getEntityMap() {
		return entityMap;
	}

	public XMLToTT setEntityMap(Map<String, TTEntity> entityMap) {
		this.entityMap = entityMap;
		return this;
	}

	public XMLToTTListener getListener() {
		return listener;
	}

	public XMLToTT setListener(XMLToTTListener listener) {
		this.listener = listener;
		return this;
	}
}
