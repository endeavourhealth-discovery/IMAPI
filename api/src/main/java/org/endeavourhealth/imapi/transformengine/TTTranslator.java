package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.map.MapProperty;
import org.endeavourhealth.imapi.model.iml.TargetUpdateMode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.Collection;
import java.util.List;
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
					if (from instanceof String)
						return TTLiteral.literal((String) from);
					else if (from instanceof Number)
						return TTLiteral.literal(from);
					else {
						return from;
					}
		}
		catch (JsonProcessingException e){
			throw new DataFormatException("Unknown target value type "+ from.getClass().getName());
		}
	}



	@Override
	public void setPropertyValue(MapProperty rule, Object targetEntity, String property, Object targetValue) throws DataFormatException {
		try {
			if (property.equals("@id") || property.equals("id") || property.equals("iri"))
				((TTNode) targetEntity).setIri(((TTLiteral) targetValue).getValue());
			else {
				String predicate = property;
				if (!property.contains(":"))
					predicate = IM.NAMESPACE + property;
				if (targetValue instanceof List) {
                    setPropertyValueList((TTNode) targetEntity, (List) targetValue, predicate);
                } else if (targetValue instanceof TTArray) {
                    setPropertyValueTTArray((TTNode) targetEntity, (TTArray) targetValue, predicate);
                } else if (targetValue instanceof TTEntity) {
                    setPropertyValueTTEntity(rule, (TTNode) targetEntity, targetValue, predicate);
                } else if (targetValue instanceof TTValue) {
                    setPropertyValueTTValue(rule, (TTNode) targetEntity, (TTValue) targetValue, predicate);
                } else {
                    setPropertyValueLiteral((TTNode) targetEntity, targetValue, predicate);
                }
			}
		} catch (JsonProcessingException e) {
			 throw new DataFormatException("Value of property : " + property + " cannot be set as its class is invalid (" + targetValue.getClass().getSimpleName() + ")");
		}
	}

    private void setPropertyValueList(TTNode targetEntity, List targetValue, String predicate) throws DataFormatException {
        TTArray array = new TTArray();
        for (Object item : targetValue) {
            array.add((TTValue) convertToTargetSingle(item));
        }
        setPropertyValueTTArray(targetEntity, array, predicate);
    }

    private static void setPropertyValueTTArray(TTNode targetEntity, TTArray targetValue, String predicate) {
        targetEntity.set(TTIriRef.iri(predicate), targetValue);
    }

    private static void setPropertyValueTTEntity(MapProperty rule, TTNode targetEntity, Object targetValue, String predicate) {
        TTNode nodeValue = (TTNode) targetValue;
        if (((TTEntity) targetValue).getIri() != null)
            nodeValue.setIri(((TTEntity) targetValue).getIri());
        if (rule.getTargetUpdateMode() == TargetUpdateMode.ADDTOLIST) {
            targetEntity.addObject(TTIriRef.iri(predicate), nodeValue);
        }
    }

    private static void setPropertyValueTTValue(MapProperty rule, TTNode targetEntity, TTValue targetValue, String predicate) {
        if (rule.getTargetUpdateMode() == TargetUpdateMode.ADDTOLIST) {
            targetEntity.addObject(TTIriRef.iri(predicate), targetValue);
        } else
            targetEntity.set(TTIriRef.iri(predicate), targetValue);
    }

    private static void setPropertyValueLiteral(TTNode targetEntity, Object targetValue, String predicate) throws JsonProcessingException {
        targetEntity.set(TTIriRef.iri(predicate), TTLiteral.literal(targetValue));
    }

}
