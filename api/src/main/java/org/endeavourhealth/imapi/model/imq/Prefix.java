package org.endeavourhealth.imapi.model.imq;

public class Prefix {
	private String prefix;
	private String namespace;

	public String getPrefix() {
		return prefix;
	}

	public Prefix setPrefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	public String getNamespace() {
		return namespace;
	}

	public Prefix setNamespace(String namespace) {
		this.namespace = namespace;
		return this;
	}

}
