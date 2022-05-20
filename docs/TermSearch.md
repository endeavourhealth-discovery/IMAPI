#Term searches
Term searches use IMAPI which uses AWS open search to perform a series of searches on the concept index.
Term searches accept codes, IRIs partial or full words in any order. Words may be parts of preferred names or alternative synonyms or key words (aka snomed descriptions).
QOF clode cluster acronyms are examples of keys
Results are sorted according firstly as to whether the matched term starts with the entered term and then by length as the shorter terms tend to be higher level concepts.
The API accepts a Request document, which is a JSON document containing a number of filters
##IMAPI

