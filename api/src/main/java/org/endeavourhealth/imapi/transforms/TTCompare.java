package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;

import java.util.Iterator;

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
		if (from.getPredicateMap().size() != to.getPredicateMap().size())
			return false;

		return from.getPredicateMap().entrySet().stream()
			.allMatch(e -> equals(e.getValue(),to.getPredicateMap().get(e.getKey())));
	}

	/**
	 * Tests equality of two TTArrays recursively checking node predicates
	 * @param from first TTArray
	 * @param to second TTArray
	 * @return true or false
	 */
	public static boolean equals(TTArray from, TTArray to){
		if (from.size()!=to.size())
			return false;
		Iterator<TTValue> s1It = from.getElements().iterator();
		Iterator<TTValue> s2It = to.getElements().iterator();
		while (s1It.hasNext()) {
			if (!equals(s1It.next(), s2It.next()))
				return false;
		}
		return true;
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
