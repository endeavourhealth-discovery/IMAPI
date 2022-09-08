package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"alias","name","with","path","and","or","not","with","where","orderBy","limit","direction"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Match extends TTIriRef{
	private static final Logger LOG = LoggerFactory.getLogger(Match.class);
	private String alias;
	private String name;
	private String with;
	private String path;
	private List<Match> and;
	private List<Match> or;
	private Match not;
	private Where where;
	private List<String> orderBy;
	private int limit;
	private String direction;

	public String getPath() {
		return path;
	}

	public Match setPath(String path) {
		this.path = path;
		return this;
	}

	public String getWith() {
		return with;
	}



	public Match setWith(String with) {
		this.with = with;
		return this;
	}

	public String getDirection() {
		return direction;
	}

	public Match setDirection(String direction) {
		this.direction = direction;
		return this;
	}

	public List<String> getOrderBy() {
		return orderBy;
	}

	public Match setOrderBy(List<String> orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public int getLimit() {
		return limit;
	}

	public Match setLimit(int limit) {
		this.limit = limit;
		return this;
	}


	public String getAlias() {
		return alias;
	}

	public Match setAlias(String alias) {
		this.alias = alias;
		return this;
	}

	public String getName() {
		return name;
	}

	public Match setName(String name) {
		this.name = name;
		return this;
	}

	public List<Match> getAnd() {
		return and;
	}

	public Match setAnd(List<Match> all) {
		this.and = all;
		return this;
	}

	public Match addAnd(Match match) {
		if (this.and==null)
			this.and= new ArrayList<>();
		this.and.add(match);
		return this;
	}

	@JsonIgnore
	public Match and (Consumer<Match> builder){
		Match match = new Match();
		this.addAnd(match);
		builder.accept(match);
		return this;
	}

	public Match addOr(Match match) {
		if (this.or ==null)
			this.or = new ArrayList<>();
		this.or.add(match);
		return this;
	}

	@JsonIgnore
	public Match or (Consumer<Match> builder){
		Match match = new Match();
		this.addOr(match);
		builder.accept(match);
		return this;
	}

	public List<Match> getOr() {
		return or;
	}

	public Match setOr(List<Match> oneOf) {
		this.or = oneOf;
		return this;
	}

	public Match getNot() {
		return not;
	}

	public Match setNot(Match not) {
		this.not = not;
		return this;
	}



	public Match not(Consumer<Match> builder){
		Match not= new Match();
		this.not= not;
		builder.accept(not);
		return this;
	}


	public Where getWhere() {
		return where;
	}

	public Match setWhere(Where where) {
		this.where = where;
		return this;
	}



	public Match where(Consumer<Where> builder) {
		Where where= new Where();
		this.where= where;
		builder.accept(where);
		return this;
	}
}
