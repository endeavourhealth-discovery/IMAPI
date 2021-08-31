package org.endeavourhealth.imapi.mapping.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MappingInstructionWrapper {

	public MappingInstructionWrapper() {
		this.setInstructions(new LinkedHashMap<String, List<MappingInstruction>>());
	}

	private String iterator;
	private Map<String, List<MappingInstruction>> instructions;

	public String getNestedPropName() {
		if (this.iterator == null) {
			return null;
		}
		String[] parts = this.iterator.split("\\.");
		String lastProp = parts[parts.length - 1];
		if (lastProp.endsWith("[*]")) {
			return lastProp.substring(0, lastProp.indexOf("["));
		}

		return lastProp;
	}

	public MappingInstructionWrapper addInstruction(String mapName, MappingInstruction instruction) {
		if (!this.instructions.containsKey(mapName)) {
			this.instructions.put(mapName, new ArrayList<MappingInstruction>());
		}
		this.instructions.get(mapName).add(instruction);
		return this;
	}

	public String getIterator() {
		return iterator;
	}

	public MappingInstructionWrapper setIterator(String iterator) {
		this.iterator = iterator;
		return this;
	}

	public Map<String, List<MappingInstruction>> getInstructions() {
		return instructions;
	}

	public MappingInstructionWrapper setInstructions(Map<String, List<MappingInstruction>> instructions) {
		this.instructions = instructions;
		return this;
	}

}
