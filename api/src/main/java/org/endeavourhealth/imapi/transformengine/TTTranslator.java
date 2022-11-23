package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.iml.ListMode;
import org.endeavourhealth.imapi.model.iml.MapRule;
import org.endeavourhealth.imapi.model.iml.TargetUpdateMode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.Collection;
import java.util.Map;
import java.util.zip.DataFormatException;

public class TTTranslator implements SyntaxTranslator {

	public Object createEntity(String type){
		TTEntity target= new TTEntity();
		target.addType(TTIriRef.iri(type));

		return target;
	}

	@Override
	public Object getPropertyValue(Object source, String property) {
		return null;
	}

	@Override
	public Object getListItems(Object source, ListMode listMode) throws DataFormatException {
		return null;
	}

	@Override
	public boolean isCollection(Object source) {
		if (source instanceof TTArray)
			return true;
		else
			return false;
	}


	@Override
	public Object convertToTarget(Object from) throws DataFormatException {
			if (from instanceof Map){
				TTNode result= new TTNode();
				for (Map.Entry<String,Object> entry: ((Map<String,Object>) from).entrySet()){
					String key= entry.getKey();
					if (!key.contains(":"))
						key= IM.NAMESPACE+key;
					Object value = convertToTargetSingle(entry.getValue());
					if (value instanceof TTArray)
						result.set(TTIriRef.iri(key),(TTArray) value);
					else if (value instanceof TTValue){
						result.set(TTIriRef.iri(key),(TTValue) value);
					}
					else
						throw new DataFormatException("Unknown sub node type in target map "+ value.getClass().getSimpleName());
				}
				return result;
			}
			else if (from instanceof Collection){
				TTArray result= new TTArray();
				for (Object value:(Collection) from){
					result.add((TTValue) convertToTarget(value));
				}
				return result;
			}
				return convertToTargetSingle(from);
	}

	@Override
	public Object convertFromSource(Object from) {
		return null;
	}

	private Object convertToTargetSingle(Object from) throws DataFormatException {
		try {
				if (from instanceof TTEntity){
					TTNode nodeValue= (TTNode) from;
					if (((TTEntity) from).getIri()!=null)
						nodeValue.setIri(((TTEntity) from).getIri());
					return nodeValue;
				}
				else if (from instanceof TTNode){
					return from;
				}
				else if (from instanceof TTValue) {
					return from;
				}
				else {
					if (from instanceof String)
						return TTLiteral.literal((String) from);
					else if (from instanceof Number)
						return TTLiteral.literal(from);
					else
						throw new DataFormatException("Unknown target value type "+ from.getClass().getName());
				}
		}
		catch (JsonProcessingException e){
			throw new DataFormatException("Unknown target value type "+ from.getClass().getName());
		}
	}



	@Override
	public void setPropertyValue(MapRule rule, Object targetEntity, String property, Object targetValue) throws DataFormatException {
		    if (property.equals("@id")|property.equals("id")|property.equals("iri"))
					((TTNode) targetEntity).setIri(((TTLiteral) targetValue).getValue());
				else {
					String predicate= property;
					if (!property.contains(":"))
						predicate= IM.NAMESPACE+property;

					if (targetValue instanceof TTArray) {
						((TTNode) targetEntity).set(TTIriRef.iri(predicate), (TTArray) targetValue);
					}
					else if (targetValue instanceof TTEntity){
						TTNode nodeValue= (TTNode) targetValue;
						if (((TTEntity) targetValue).getIri()!=null)
							nodeValue.setIri(((TTEntity) targetValue).getIri());
						if (rule.getTargetUpdateMode()== TargetUpdateMode.ADDTOLIST){
							((TTNode) targetEntity).addObject(TTIriRef.iri(predicate), nodeValue);
						}
					}
					else if (targetValue instanceof TTValue) {
						if (rule.getTargetUpdateMode() == TargetUpdateMode.ADDTOLIST) {
							((TTNode) targetEntity).addObject(TTIriRef.iri(predicate), (TTValue) targetValue);
						} else
							((TTNode) targetEntity).set(TTIriRef.iri(predicate), (TTValue) targetValue);
					}
					else
						throw new DataFormatException("Value of property : "+property+" cannot be set as its class is invalid ("+ targetValue.getClass().getSimpleName()+")");
				}
	}
}
