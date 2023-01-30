package org.endeavourhealth.imapi.model.imq;

public class With extends Where implements Sortable{
	private String latest;
	private String earliest;
	private String maximum;
	private String minimum;
	private Integer count;
	public String getLatest() {
		return latest;
	}

	public With setCount(Integer count) {
		this.count = count;
		return this;
	}
	public With setLatest(String latest) {
		this.latest = latest;
		return this;
	}

	public String getEarliest() {
		return earliest;
	}

	public With setEarliest(String earliest) {
		this.earliest = earliest;
		return this;
	}

	public String getMaximum() {
		return maximum;
	}

	public With setMaximum(String maximum) {
		this.maximum = maximum;
		return this;
	}

	public String getMinimum() {
		return minimum;
	}

	public With setMinimum(String minimum) {
		this.minimum = minimum;
		return this;
	}

	public Integer getCount() {
		return count;
	}
}
