package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.imapi.model.iml.*;
import org.endeavourhealth.imapi.model.tripletree.TTVariable;

import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Transformer engine to transform a collection of source objects to target objects using a transformation map
 * All source objects must be able to support graph like property paths (e.g. json, xml, object reflection.
 * <p> The transformation engine does not include extracting data from csv or tables.
 * However, the format dependent plugins may be extended to retrieve or cache data</p>
 */
public class Transformer {
	/*
	private String sourceFormat;
	private String targetFormat;
	private TransformMap transformMap;
	private Map<String, TransformMap> imports;
	private Map<String, Set<Object>> typedTargets;
	private Map<String, String> sourceVarMap;
	private Map<String, List<Object>> typedSources;


	/**
	 * Transforms a collection of typed objects to a set of objects.
	 * The types should match the source list in the transformation map (or any of its imported maps).
	 * The objects are object references in a logical form that matches the type
	 * Note that the actual format of source and target (e.g. json) are used as arguments to this function so that the engine could transform
	 * The same logical source in different formats if the plugins have been developed
	 *
	 * @param typedSources a map of type (iri) to object set containing instances of source objects of the type. If the trasnform map has an iterator then it can be a list of objects of the same type.
	 * @param transformMap an object containing the map definition conformant with the IM map definition shape.
	 * @param sourceFormat JSON or other supported input format. As maps are logically independent of syntax, several import formats may be supported for a source type
	 * @param targetFormat JSON-LD or other supported output syntax.
	 * @param imports      Map iri to transform map for imported maps, objects instantiated by the client before transform is called.
	 * @return an instance (or collection) of targets List of objects in the target format, the list being generated in the order as defined in the map
	 */
	/*
	public Object transform(Map<String, List<Object>> typedSources, TransformMap transformMap,
													String sourceFormat, String targetFormat, Map<String, TransformMap> imports) throws DataFormatException {

		this.imports = imports;
		validateInputs(sourceFormat, targetFormat, transformMap);
		typedTargets = new HashMap<>();
		sourceVarMap = new HashMap<>();
		try {
			try {
				for (TTVariable sourceVar : transformMap.getSource()) {
					sourceVarMap.put(sourceVar.getVariable(), sourceVar.getIri());
				}
			} catch (Exception e) {
				throw new DataFormatException("Transform map does not have valid source type and variable list");
			}
			try {
				transformRules();
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		} catch (Exception e) {
			throw new RuntimeException("Unable to run transform due to " + e.getMessage());
		}

		return null;
	}

	private void validateInputs(String sourceFormat, String targetFormat, TransformMap transformMap) throws DataFormatException {
		if (sourceFormat == null)
			throw new DataFormatException("Source format must be defined in request (e.g. sourceFormat : JSON)");
		if (targetFormat == null)
			throw new DataFormatException("Target format must be defined in request (e.g. targetFormat : JSON-LD)");
		this.sourceFormat = sourceFormat;
		this.targetFormat = targetFormat;
		if (transformMap == null)
			throw new DataFormatException("Map iri (\"@id\" : \"http...\" must be present in request");
		this.transformMap = transformMap;
	}

	private void transformRules() throws DataFormatException {
		if (transformMap.getRule() != null) {
			for (MapRule mapRule : transformMap.getRule()) {
				for (MapRuleTarget ruleTarget : mapRule.getTarget()) {
					if (ruleTarget.getFunction() != null) {
						if (ruleTarget.getFunction().getArgument() != null) {
							Map<String, Object> arguments = getFunctionArguments(mapRule, ruleTarget.getFunction());
						}
					}
				}
			}

		}
	}

	private Map<String, Object> getFunctionArguments(MapRule mapRule, Function function) throws DataFormatException {
		Map<String, Object> result = null;
		if (function.getArgument() != null) {
			result = new HashMap<>();
			int i = 0;
			for (Argument argument : function.getArgument()) {
				i++;
				String parameter = argument.getParameter();
				if (parameter == null)
					parameter = String.valueOf(i);
				if (argument.getValueData() != null)
					result.put(parameter, argument.getValueData());
				else if (argument.getValueVariable() != null) {
					result.put(parameter, getSourceFromVariable(mapRule, argument.getValueVariable()));

				}
			}
		}
		return result;
	}

	private Object getSourceFromVariable(MapRule mapRule, String valueVariable) throws DataFormatException {
		for (MapRuleSource ruleSource : mapRule.getSource()) {
			if (ruleSource.getVariable().equals(valueVariable)) {
				String path = ruleSource.getPath();
				String listMode = ruleSource.getListMode();
				String sourceType = ruleSource.getType();
				String sourceContext = ruleSource.getContext();
				//Binds map context variable to actual sources
				if (sourceContext != null) {
					sourceType = sourceVarMap.get(sourceContext);
				}
				if (sourceType == null)
					throw new DataFormatException("Source map variable " + sourceContext + " of type " + sourceType + " is not in the typed source list");
				for (Object source : typedSources.get(sourceType)) {
						Object sourceValue = getValueFromPath(source, path);
				}

			}

		}
		return null;


	}

	private Object getValueFromPath(Object source, String path) throws DataFormatException {
		if (sourceFormat.equalsIgnoreCase("JSON")) {
			if (source instanceof JsonNode) {
				return ((JsonNode) source).get(path);
			} else
				throw new DataFormatException("Source object not compatible with source format (" + sourceFormat + " )");
		}
		return null;
	}

	 */

}