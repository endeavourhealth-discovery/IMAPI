package org.endeavourhealth.imapi.vocabulary;

public class QUERY {
	public static final String DOMAIN = "http://endhealth.info/im#";
	public static final String NAMESPACE = QUERY.DOMAIN + "Query_";
	public static final String ALLOWABLE_RANGES = QUERY.NAMESPACE + "AllowableRanges";
	public static final String GET_ISAS = QUERY.NAMESPACE + "GetIsas";
	public static final String GET_DESCENDANTS = QUERY.NAMESPACE + "GetDescendants";
	public static final String SEARCH_CONTAINED_IN = QUERY.NAMESPACE + "SearchContainedIn";
	public static final String ALLOWABLE_CHILD_TYPES = QUERY.NAMESPACE + "AllowableChildTypes";
	public static final String PROPERTY_RANGE = QUERY.NAMESPACE + "PropertyRange";
	public static final String OBJECT_PROPERTY_RANGE_SUGGESTIONS = QUERY.NAMESPACE + "ObjectPropertyRangeSuggestions";
	public static final String DATA_PROPERTY_RANGE_SUGGESTIONS = QUERY.NAMESPACE + "DataPropertyRangeSuggestions";
	public static final String ALLOWABLE_PROPERTIES = QUERY.NAMESPACE + "AllowableProperties";
	public static final String SEARCH_PROPERTIES = QUERY.NAMESPACE + "SearchProperties";
	public static final String SEARCH_ENTITIES = QUERY.NAMESPACE + "SearchEntities";
	public static final String SEARCH_FOLDERS = QUERY.NAMESPACE + "SearchFolders";
	public static final String SEARCH_ALLOWABLE_CONTAINED_IN = QUERY.NAMESPACE + "SearchAllowableContainedIn";
	public static final String SEARCH_MAIN_TYPES = QUERY.NAMESPACE + "SearchmainTypes";
	public static final String DM_PROPERTY = QUERY.NAMESPACE + "DataModelPropertyByShape";
	public static final String SEARCH_SUBCLASS = QUERY.NAMESPACE + "SearchAllowableSubclass";
}