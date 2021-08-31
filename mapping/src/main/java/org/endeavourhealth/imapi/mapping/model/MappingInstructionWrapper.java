package org.endeavourhealth.imapi.mapping.model;

import java.util.ArrayList;
import java.util.List;

public class MappingInstructionWrapper {

	public MappingInstructionWrapper() {
		this.instructions = new ArrayList<MappingInstruction>();
	}

	private String iterator;
	private List<MappingInstruction> instructions;

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

	public MappingInstructionWrapper addInstruction(MappingInstruction instruction) {
		this.instructions.add(instruction);
		return this;
	}

	public List<MappingInstruction> getInstructions() {
		return instructions;
	}

	public MappingInstructionWrapper setInstructions(List<MappingInstruction> instructions) {
		this.instructions = instructions;
		return this;
	}

	public String getIterator() {
		return iterator;
	}

	public MappingInstructionWrapper setIterator(String iterator) {
		this.iterator = iterator;
		return this;
	}

}
