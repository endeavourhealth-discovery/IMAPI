package org.endeavourhealth.imapi.model.iml;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class QueryClause extends TTAlias {
	private String description;
	private With with;
	private List<Select> select;
	private Where where;
	private List<TTAlias> orderBy;
	private String direction;
	private Integer limit;
	private List<TTAlias> groupBy;

	public String getDescription() {
		return description;
	}

	public QueryClause setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getDirection() {
		return direction;
	}

	public QueryClause setDirection(String direction) {
		this.direction = direction;
		return this;
	}


	public With getWith() {
		return with;
	}

	public QueryClause setWith(With with) {
		this.with=with;
		return this;
	}
	@JsonIgnore
	public QueryClause with (Consumer<With> builder){
		this.with= new With();
		builder.accept(this.with);
		return this;
	}





	public QueryClause where(Consumer<Where> builder){
		Where where= new Where();
		this.where=where;
		builder.accept(where);
		return this;
	}

	public List<Select> getSelect() {
		return select;
	}

	public QueryClause setSelect(List<Select> select) {
		this.select = select;
		return this;
	}

	public QueryClause addSelect(Select select){
		if (this.select==null)
			this.select= new ArrayList<>();
		this.select.add(select);
		return this;
	}

	public QueryClause addSelect(String property){
		if (this.select==null)
			this.select= new ArrayList<>();
		this.select.add(new Select().setProperty(property));
		return this;
	}

	@JsonIgnore
	public QueryClause select(Consumer<Select> builder){
		if (this.select==null)
			this.select= new ArrayList<>();
		Select select= new Select();
		this.select.add(select);
		builder.accept(select);
		return this;
	}

	@JsonIgnore
	public QueryClause select(String property){
		if (this.select==null)
			this.select= new ArrayList<>();
		Select select= new Select();
		this.select.add(select);
		select.setProperty(property);
		return this;
	}

	@JsonIgnore
	public QueryClause select(TTIriRef property){
		if (this.select==null)
			this.select= new ArrayList<>();
		Select select= new Select();
		this.select.add(select);
		select.setProperty(property);
		return this;
	}

	public Where getWhere() {
		return where;
	}

	public QueryClause setWhere(Where where) {
		this.where = where;
		return this;
	}

	public List<TTAlias> getOrderBy() {
		return orderBy;
	}

	public QueryClause setOrderBy(List<TTAlias> orderBy) {
		this.orderBy = orderBy;
		return this;
	}
	
	public QueryClause addOrderBy(TTAlias orderBy){
		if (this.orderBy==null)
			this.orderBy= new ArrayList<>();
		this.orderBy.add(orderBy);
		return this;
	}

	public Integer getLimit() {
		return limit;
	}

	public QueryClause setLimit(Integer limit) {
		this.limit = limit;
		return this;
	}

	public List<TTAlias> getGroupBy() {
		return groupBy;
	}

	public QueryClause setGroupBy(List<TTAlias> groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	public QueryClause addGroupBy(TTAlias groupBy){
		if (this.groupBy==null)
			this.groupBy= new ArrayList<>();
		this.groupBy.add(groupBy);
		return this;
	}
}
