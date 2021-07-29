package org.endeavourhealth.imapi.mapping.model;

import java.util.Map;

public class Node {
	private Map<String, Node> nodes;

	public Map<String, Node> getNodes() {
		return nodes;
	}

	public void setNode(String key, Node value) {
		nodes.put(key, value);
	}
}
