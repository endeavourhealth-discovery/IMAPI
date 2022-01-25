package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.io.InvalidClassException;
import java.util.List;


public class Match extends TTEntity{

	public Match(){
		setPredicateTemplate(new TTIriRef[]{RDF.TYPE, RDFS.LABEL,RDFS.COMMENT,
			IM.PATH_TO,IM.ENTITY_TYPE,IM.NOT_EXIST,IM.FUNCTION,IM.PROPERTY,
			IM.VALUE_COMPARE,IM.VALUE_IN,IM.VALUE_NOTIN,IM.VALUE_RANGE,IM.VALUE_FUNCTION,IM.VALUE_VAR,
			IM.AND,IM.OR,IM.NOT});
	}

	public TTIriRef getPathTo() {
		return (TTIriRef) TTUtil.get(this, IM.PATH_TO,TTIriRef.class);
	}

	public Match setPathTo(TTIriRef pathTo) {
		set(IM.PATH_TO, pathTo);
		return this;
	}

	public TTIriRef getEntityType() {
		return (TTIriRef) TTUtil.get(this, IM.ENTITY_TYPE,TTIriRef.class);
	}

	public Match setEntityType(TTIriRef entityType) {
		set(IM.ENTITY_TYPE, entityType);
		return this;

	}

	public List<Match> getAnds(){
		return TTUtil.getOrderedList(this,IM.AND,Match.class);
	}

	public Match setAnds(TTArray ands){
		set(IM.AND,ands);
		return this;
	}
	public Match addAnd(Match pv){
		TTUtil.add(this,IM.AND,pv);
		return this;
	}

	public List<Match> getOrs(){
		return TTUtil.getOrderedList(this,IM.OR,Match.class);
	}

	public Match setOrs(TTArray ors){
		set(IM.OR,ors);
		return this;
	}
	public Match addOr(Match pv){
		TTUtil.add(this,IM.OR,pv);
		return this;
	}

	public List<Match> getNot(){
		return TTUtil.getOrderedList(this,IM.NOT,Match.class);
	}

	public Match setNot(TTArray nots){
		set(IM.NOT,nots);
		return this;
	}
	public Match addNot(Match not){
		TTUtil.add(this,IM.NOT,not);
		return this;
	}

	public TTIriRef getProperty() throws InvalidClassException {
		return (TTIriRef) TTUtil.get(this, IM.PROPERTY,TTIriRef.class);
	}

	public Match setProperty(TTIriRef property) {
		set(IM.PROPERTY,property);
		return this;
	}

	public Compare getValue() {
		return (Compare) TTUtil.get(this,IM.VALUE_COMPARE,Compare.class);
	}

	public Match setValue(Compare value){
		set(IM.VALUE_COMPARE,value);
		return this;
	}

	public List<TTIriRef> getValueIn() {
		return TTUtil.getList(this,IM.VALUE_IN,TTIriRef.class);
	}

	public Match setValueIn(TTArray in) {
		set(IM.VALUE_IN,in);
		return this;
	}
	public Match setValueIn(TTIriRef in) {
		set(IM.VALUE_IN,in);
		return this;
	}

	public Match addValueIn(TTIriRef in){
		TTUtil.add(this,IM.VALUE_IN,in);
		return this;
	}

	public List<TTIriRef> getValueNotIn() {
		return TTUtil.getList(this,IM.VALUE_NOTIN,TTIriRef.class);
	}

	public Match setValueNotIn(TTArray notIn) {
		set(IM.VALUE_NOTIN,notIn);
		return this;
	}
	public Match setValueNotIn(TTIriRef notIn) {
		set(IM.VALUE_NOTIN,notIn);
		return this;
	}

	public Match addValueNotIn(TTIriRef notIn){
		TTUtil.add(this,IM.VALUE_NOTIN,notIn);
		return this;
	}

	public Range getValueRange() {
		return (Range) TTUtil.get(this,IM.VALUE_RANGE,Range.class);
	}

	public Match setValueRange(Range range) {
		set(IM.VALUE_RANGE,range);
		return this;
	}

	public Function getValueFunction() {
		return (Function) TTUtil.get(this,IM.VALUE_FUNCTION,Function.class);
	}

	public Match setValueFunction(Function function){
		set(IM.VALUE_FUNCTION,function);
		return this;
	}

	public Match setValueTest(Comparison comp, String value) {
		setValue(new Compare().setComparison(comp).setValue(value));
		return this;
	}

	public Match setRangeValueTest(Comparison fromComp, String fromValue,
																				 Comparison toComp,String toValue){
		setValueRange(new Range()
			.setFrom(new Compare().setComparison(fromComp).setValue(fromValue))
			.setTo(new Compare().setComparison(toComp).setValue(toValue)));
		return this;
	}

	public String getValueVar(){
		return (String) TTUtil.get(this,IM.VALUE_VAR,String.class);
	}

	public Match setValueVar(String var){
		set(IM.VALUE_VAR, TTLiteral.literal(var));
		return this;
	}

	public  Match getTest() {
		return (Match) TTUtil.get(this,IM.TEST,Match.class);
	}

	public Match setTest(Match test) {
		set(IM.TEST,test);
		return this;
	}


	public Function getFunction(){
		return (Function) TTUtil.get(this,IM.FUNCTION,Function.class);
	}
	public Match setFunction(Function function){
		this.set(IM.FUNCTION,function);
		return this;
	}

	public boolean getNotExist(){
		if (get(IM.NOT_EXIST)==null)
			return false;
		else
			return true;
	}

	public Match setNotExist(boolean notExist) {
		if (!notExist)
			getPredicateMap().remove(IM.NOT_EXIST);
		else
			set(IM.NOT_EXIST,TTLiteral.literal(true));
		return this;
	}

}










