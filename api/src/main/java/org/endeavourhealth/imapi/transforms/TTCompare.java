package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;

/**
 * Uitilities to compare Triple tree objects examning only predicates and values, ignoring entity IRI
 * or private properties
 */
public class TTCompare {

	private TTCompare() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Tests equality the predicate keys and values of a node
	 * @param from first node
	 * @param to second node
	 * @return if the same or not
	 */
	public static boolean equals(TTNode from, TTNode to){
		if (isNull(from.getPredicateMap(),to.getPredicateMap()))
			return false;
		if (from.getPredicateMap().size() != to.getPredicateMap().size())
			return false;

		return from.getPredicateMap().entrySet().stream()
			.allMatch(e -> equals(e.getValue(),to.getPredicateMap().get(e.getKey())));
	}

	private static boolean isNull(Object from, Object to){
		if (from==null&&to!=null)
			return true;
		return to == null && from != null;
	}

	/**
	 * Tests equality of two TTArrays recursively checking node predicates
	 * @param from first TTArray
	 * @param to second TTArray
	 * @return true or false
	 */
	public static boolean equals(TTArray from, TTArray to){
		if (isNull(from,to))
			return false;
		if (from != null && from.size()!=to.size())
			return false;
		if ((from != null) && (arrayEquals(from, to))) return false;
		return true;
	}

    private static boolean arrayEquals(TTArray from, TTArray to) {
        for (TTValue fromVal: from.getElements()){
            boolean found=false;
            for (TTValue toVal: to.getElements()){
                if (equals(fromVal,toVal))
                    found=true;
            }
            if (!found)
                return true;
        }
        return false;
    }

    public static boolean equals(TTValue from, TTValue to) {
		if (from.isIriRef())
			return from.equals(to);
		if (from.isLiteral())
			return from.asLiteral().getValue().equals(to.asLiteral().getValue());
		else
			if (from.isNode() && to.isNode())
				return equals((TTNode) from,(TTNode) to);
			else
				return false;
	}


}
