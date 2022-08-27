package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"displayText","pathTo","name","iri","alias","includeSubTypes","inverseOf","includeSubProperties"
	,"isConcept","inValueSet","notInValueSet", "inRange","value","function","within","valueVar","valueMatch","isIndex","and","optional"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PropertyValue  extends ConceptRef{
	private boolean optional;
	Compare value;
	List<ConceptRef> inSet;
	List<ConceptRef> notInSet;
	List<ConceptRef> isConcept;
	List<ConceptRef> isNotConcept;
	private List<ConceptRef> pathTo;
	Range inRange;
	Match valueMatch;
	Function function;
	Within within;
	boolean inverseOf=false;
	boolean notExist=false;
	private List<Argument> argument;
	private String displayText;

	public String getDisplayText() {
		return displayText;
	}

	public PropertyValue setDisplayText(String displayText) {
		this.displayText = displayText;
		return this;
	}

	public List<ConceptRef> getPathTo() {
		return pathTo;
	}

	public PropertyValue setPathTo(List<ConceptRef> pathTo) {
		this.pathTo = pathTo;
		return this;
	}

	public PropertyValue addPathTo(ConceptRef pathTo) {
		if (this.pathTo==null)
			this.pathTo= new ArrayList<>();
		this.pathTo.add(pathTo);
		return this;
	}

	public PropertyValue(TTIriRef iri){
		super.setIri(iri.getIri());
	}

	public PropertyValue(String iri, String name) {
		super.setIri(iri);
		if (name!=null)
			super.setName(name);
	}

	public static PropertyValue iri(String iri, String name) {
		return new PropertyValue(iri, name);
	}


	public List<Argument> getArgument() {
		return argument;
	}

	public PropertyValue setArgument(List<Argument> argument) {
		this.argument = argument;
		return this;
	}

	public PropertyValue addArgument(Argument argument) {
		if (this.argument==null)
			this.argument= new ArrayList<>();
		this.argument.add(argument);
		return this;
	}

	public PropertyValue argument(Consumer<Argument> builder) {
		Argument argument= new Argument();
		addArgument(argument);
		builder.accept(argument);
		return this;
	}


	public boolean isOptional() {
		return optional;
	}

	@Override
	@JsonSetter
	public PropertyValue setIri(String iri){
		super.setIri(iri);
		return this;
	}


	@JsonIgnore
	public PropertyValue setIri(TTIriRef iri){
		super.setIri(iri.getIri());
		return this;
	}

	@Override
	public PropertyValue setIncludeSubtypes(boolean subs){
		super.setIncludeSubtypes(subs);
		return this;
	}

	@Override
	public PropertyValue setAlias(String alias){
		super.setAlias(alias);
		return this;
	}


	public PropertyValue setOptional(boolean optional) {
		this.optional = optional;
		return this;
	}

	@JsonIgnore
	public PropertyValue value(Consumer<Compare> builder){
		Compare c= new Compare();
		this.setValue(c);
		builder.accept(c);
		return this;
	}


	public List<ConceptRef> getIsConcept() {
		return isConcept;
	}

	public PropertyValue setIsConcept(List<ConceptRef> valueConcept) {
		this.isConcept = valueConcept;
		return this;
	}

	@JsonIgnore
	public PropertyValue isConcept(List<ConceptRef> valueConcept) {
		this.isConcept = valueConcept;
		return this;
	}

	public PropertyValue addIsConcept(ConceptRef value){
		if (this.isConcept==null)
			this.isConcept= new ArrayList<>();
		this.isConcept.add(value);
		return this;
	}

	public PropertyValue addIsConcept(TTIriRef value){
		ConceptRef cr= new ConceptRef(value.getIri(),value.getName());
		addIsConcept(cr);
		return this;
	}

	public PropertyValue addIsNotConcept(ConceptRef value){
		if (this.isNotConcept ==null)
			this.isNotConcept = new ArrayList<>();
		this.isNotConcept.add(value);
		return this;
	}

	public List<ConceptRef> getIsNotConcept() {
		return isNotConcept;
	}

	public PropertyValue setIsNotConcept(List<ConceptRef> isNotConcept) {
		this.isNotConcept = isNotConcept;
		return this;
	}

	@Override
	public PropertyValue setName(String name){
		super.setName(name);
		return this;
	}

	public Within getWithin() {
		return within;
	}

	@JsonSetter
	public PropertyValue setWithin(Within within) {
		this.within = within;
		return this;
	}
	@JsonIgnore
	public PropertyValue within(Consumer<Within> builder){
		this.within= new Within();
		builder.accept(this.within);
		return this;
	}

	public boolean isNotExist() {
		return notExist;
	}

	public PropertyValue setNotExist(boolean notExist) {
		this.notExist = notExist;
		return this;
	}

	public PropertyValue(String iri) {
		super(iri);
	}
	public PropertyValue() {
	}




	public boolean isInverseOf() {
		return inverseOf;
	}

	public PropertyValue setInverseOf(boolean inverseOf) {
		this.inverseOf = inverseOf;
		return this;
	}

	public Match getMatch() {
		return valueMatch;
	}

	public Match getValueMatch() {
		return valueMatch;
	}

	@JsonSetter
	public PropertyValue setMatch(Match match) {
		this.valueMatch = match;
		return this;
	}

	@JsonIgnore
	public PropertyValue setValueMatch(Match match) {
		this.valueMatch = match;
		return this;
	}

	@JsonIgnore
	public PropertyValue match(Match match) {
		this.valueMatch = match;
		return this;
	}

	@JsonIgnore
	public PropertyValue match(Consumer<Match> builder){
		Match match= new Match();
		this.valueMatch= match;
		builder.accept(match);
		return this;
	}

	@JsonIgnore
	public PropertyValue valueMatch(Consumer<Match> builder){
		Match match= new Match();
		this.valueMatch= match;
		builder.accept(match);
		return this;
	}




	public PropertyValue setValue(Comparison comp, String value) {
		setValue(new Compare().setComparison(comp).setValueData(value));
		return this;
	}




	public Compare getValue() {
		return value;
	}

	public PropertyValue setValue(Compare value) {
		this.value = value;
		return this;
	}


	public List<ConceptRef> getInSet() {
		return inSet;
	}
	public PropertyValue addInSet(TTIriRef value){
		if (this.inSet ==null)
			this.inSet = new ArrayList<>();
		this.inSet.add(new ConceptRef(value));
		return this;
	}

	public PropertyValue addInSet(ConceptRef value){
		if (this.inSet ==null)
			this.inSet = new ArrayList<>();
		this.inSet.add(value);
		return this;
	}

	public PropertyValue addNotInSet(TTIriRef value){
		if (this.notInSet ==null)
			this.notInSet = new ArrayList<>();
		this.notInSet.add(new ConceptRef(value));
		return this;
	}

	public PropertyValue addNotInSet(ConceptRef value){
		if (this.notInSet ==null)
			this.notInSet = new ArrayList<>();
		this.notInSet.add(value);
		return this;
	}

	public PropertyValue setInSet(List<ConceptRef> inSet) {
		this.inSet = inSet;
		return this;
	}

	public List<ConceptRef> getNotInSet() {
		return notInSet;
	}

	public PropertyValue setNotInSet(List<ConceptRef> notInSet) {
		this.notInSet = notInSet;
		return this;
	}

	public Range getInRange() {
		return inRange;
	}

	public PropertyValue setInRange(Range inRange) {
		this.inRange = inRange;
		return this;
	}





	public Function getFunction() {
		return function;
	}

	@JsonSetter
	public PropertyValue setFunction(Function function) {
		this.function = function;
		return this;
	}

	@JsonIgnore
	public PropertyValue function(Consumer<Function> builder){
		Function func= new Function();
		this.function= func;
		builder.accept(func);
		return this;
	}



}
