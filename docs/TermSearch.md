#Term searches
Term searches use IMAPI, which uses AWS open search to perform a series of searches on the concept index.

Term searches accept codes, IRIs partial or full words in any order. Words may be parts of preferred names or alternative synonyms or key words (aka snomed descriptions).

QOF clode cluster acronyms are examples of keys.

Paging is suppported in an approximate manner.

Results are sorted according firstly by whether the matched term starts with the entered term and then by length as the shorter terms tend to be higher level concepts.

The API accepts a Request document, which is a JSON document containing a number of filters.

##IMAPI

POST API   ...../api/entity/public/search

Post body containing search request json e.g.

`{
   "termFilter":"substance",
      "statusFilter":["http://endhealth.info/im#Active"],
       "isA":["http://snomed.info/sct#105590001"],
      "size":20,
      "page" : 1,
      "select":["iri","code",name"]
}`

In the above example the client is looking for matches of the term substance that are subclasses of the concept substance, and return iri, code and name

###Search match parameters
(n.b. prefix im: = http://endhealth.info/im#)


Parameter | meaning |
--- | --- | 
termFilter | the word, code, iri, phrase, partial phrase to search on |
statusFilter | whether active, inactive, draft, unassigned. Without this all statuses will be returned |
typeFilter | the high level types of entity e.g. im:Concept im:ConceptSet im:Query im:DataModelEntity |
isA | the iris of the concepts that the results must be a subtype of (including itself)|
select | list of fields to return (defaults to iri, name, entityType, code, scheme, status,termCode)
page  |  page number
size  | size per page 

###Response body
An array of json documents conforming to a SearchReultSummary class, with the following optional fields

field | meaning |
--- | --- |
iri  |           iri of the entity
name  |          name of the entity
match  |         the term or synonym that matched the search term (may or may not be the same as the name)
code    |        code of the concept
description   |  description of the concept
status | status of entity
scheme | scheme of the entity i.e. the owner of the entity
entityType | the entity type of the entity
weighting | an integer representing how often used in real systems
key | list of keys for this entity
isA |  list of super types that this entity is a subtype of
termCode  | object containing a term and code
 ...code  | code of the term code
 ...term  | term of the term code

###Logic
SearchService is called with the controller and SearchService creates an open search query object.
OSQuery is an object that processes the open search request, creates the elastic query syntax, submits the search (using environment variables for authentication),
and processes the response.
