package org.endeavourhealth.imapi.query;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;


import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"iri","name","description","select","operator","subQuery","where","clause","groupSort"})
public class Clause {
	private String name;
	private String description;
	private String iri;
	private Operator operator;
	private List<Where> where;
	private List<Clause> clause;
	private List<Clause> subQuery;
	private List<GroupSort> groupSort;
	private List<Select> select;

	
	public List<Select> getSelect() {
		return select;
	}


	public Clause addSelect(Select sct){
		if (this.select==null)
			this.select= new ArrayList<>();
		this.select.add(sct);
		return this;
	}

	public Clause setSelect(List<Select> select) {
		this.select = select;
		return this;
	}

	public String getName() {
		return name;
	}

	public Clause setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Clause setDescription(String description) {
		this.description = description;
		return this;
	}


	public List<Clause> getClause() {
		return clause;
	}

	public Clause setClause(List<Clause> clause) {
		this.clause = clause;
		return this;
	}
	public Clause addClause(Clause clause){
		if (this.clause==null)
			this.clause= new ArrayList<>();
		this.clause.add(clause);
		return this;
	}



	public List<Where> getWhere() {
		return where;
	}

	public Clause setWhere(List<Where> wheres) {
		this.where = wheres;
		return this;
	}
	public Clause addWhere(Where where){
		if (this.where ==null)
			this.where = new ArrayList<>();
		this.where.add(where);
		return this;
	}


	public Operator getOperator() {
		return operator;
	}

	public Clause setOperator(Operator operator) {
		this.operator = operator;
		return this;
	}
	public List<GroupSort> getGroupSort() {
		return groupSort;
	}
	public Clause setGroupSort(List<GroupSort> groupSort) {
		this.groupSort = groupSort;
		return this;
	}

	public Clause addGroupSort(GroupSort groupSort){
		if (this.groupSort ==null)
			this.groupSort = new ArrayList<>();
		this.groupSort.add(groupSort);
		return this;
	}

	public List<Clause> getSubQuery() {
		return subQuery;
	}

	public Clause setSubQuery(List<Clause> subQuery) {
		this.subQuery = subQuery;
		return this;
	}

	public Clause addSubQuery(Clause subQuery){
		if (this.subQuery==null)
			this.subQuery= new ArrayList<>();
		this.subQuery.add(subQuery);
		return this;
	}

	public String getIri() {
		return iri;
	}

	public Clause setIri(String iri) {
		this.iri = iri;
		return this;
	}
}
