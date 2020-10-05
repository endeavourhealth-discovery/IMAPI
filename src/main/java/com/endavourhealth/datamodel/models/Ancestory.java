package com.endavourhealth.datamodel.models;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Ancestory
 */
@Validated

public class Ancestory {
	@JsonProperty("up")
	private DataModel up = null;

	@JsonProperty("down")
	private DataModel down = null;

	@JsonProperty("node")
	private DataModel node = null;

	@Valid
	public DataModel getUp() {
		return up;
	}

	public void setUp(DataModel up) {
		this.up = up;
	}

	@Valid
	public DataModel getDown() {
		return down;
	}

	public void setDown(DataModel down) {
		this.down = down;
	}

	@NotNull

	@Valid
	public DataModel getNode() {
		return node;
	}

	public void setNode(DataModel node) {
		this.node = node;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((down == null) ? 0 : down.hashCode());
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		result = prime * result + ((up == null) ? 0 : up.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ancestory other = (Ancestory) obj;
		if (down == null) {
			if (other.down != null)
				return false;
		} else if (!down.equals(other.down))
			return false;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		if (up == null) {
			if (other.up != null)
				return false;
		} else if (!up.equals(other.up))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Ancestory [up=" + up + ", down=" + down + ", node=" + node + "]";
	}
}
