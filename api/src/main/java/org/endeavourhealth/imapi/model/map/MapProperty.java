package org.endeavourhealth.imapi.model.map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.iml.FunctionClause;
import org.endeavourhealth.imapi.model.iml.ListMode;
import org.endeavourhealth.imapi.model.iml.TargetUpdateMode;
import org.endeavourhealth.imapi.model.iml.Where;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"where","source","sourceVariable","listMode","target","functionClause","valueData","valueVariable","targetUpdateMode","propertyMap","objectMap"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MapProperty extends TTIriRef {

	private String source;
	private String sourceVariable;
	private ListMode listMode;
	private Where where;
	private FunctionClause functionClause;
	private String target;
	private String valueData;
	private String valueVariable;
	private TargetUpdateMode targetUpdateMode;
	private MapObject objectMap;
	private List<MapProperty> propertyMap;

	public String getTarget() {
		return target;
	}

	public List<MapProperty> getPropertyMap() {
		return propertyMap;
	}

	@JsonSetter
	public MapProperty setPropertyMap(List<MapProperty> propertyMap) {
		this.propertyMap = propertyMap;
		return this;
	}

	public MapProperty addPropertyMap(MapProperty propertyMap){
		if (this.propertyMap==null)
			this.propertyMap= new ArrayList<>();
		this.propertyMap.add(propertyMap);
		return this;
	}

	public MapProperty propertyMap(Consumer<MapProperty> builder){
		MapProperty propertyMap= new MapProperty();
		addPropertyMap(propertyMap);
		builder.accept(propertyMap);
		return this;
	}

	public MapProperty setSource(String source) {
		this.source = source;
		return this;
	}

	public String getSource() {
		return source;
	}

	public String getSourceVariable() {
		return sourceVariable;
	}

	public MapProperty setSourceVariable(String sourceVariable) {
		this.sourceVariable = sourceVariable;
		return this;
	}

	public MapProperty setTarget(String target) {
		this.target = target;
		return this;
	}

	public MapObject getObjectMap() {
		return objectMap;
	}

	@JsonSetter
	public MapProperty setObjectMap(MapObject objectMap) {
		this.objectMap = objectMap;
		return this;
	}



	public MapProperty objectMap(Consumer<MapObject> builder){
		this.objectMap= new MapObject();
		builder.accept(this.objectMap);
		return this;
	}

	public String getValueData() {
		return valueData;
	}

	public MapProperty setValueData(String valueData) {
		this.valueData = valueData;
		return this;
	}

	public String getValueVariable() {
		return valueVariable;
	}

	public MapProperty setValueVariable(String valueVariable) {
		this.valueVariable = valueVariable;
		return this;
	}





	public TargetUpdateMode getTargetUpdateMode() {
		return targetUpdateMode;
	}

	public MapProperty setTargetUpdateMode(TargetUpdateMode targetUpdateMode) {
		this.targetUpdateMode = targetUpdateMode;
		return this;
	}



	public ListMode getListMode() {
		return listMode;
	}

	public MapProperty setListMode(ListMode listMode) {
		this.listMode = listMode;
		return this;
	}

	public Where getWhere() {
		return where;
	}

	@JsonSetter
	public MapProperty setWhere(Where where) {
		this.where = where;
		return this;
	}

	public MapProperty where (Consumer<Where> builder){
		this.where= new Where();
		builder.accept(this.where);
		return this;
	}



	public FunctionClause getFunction() {
		return functionClause;
	}

	public MapProperty setFunction(FunctionClause functionClause) {
		this.functionClause = functionClause;
		return this;
	}



	public MapProperty function(Consumer<FunctionClause> builder){
		this.functionClause = new FunctionClause();
		builder.accept(this.functionClause);
		return this;
	}

}
