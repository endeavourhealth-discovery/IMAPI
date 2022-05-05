DataSet dataSet= query1();
	output(dataSet);
	dataSet= query2();
	output(dataSet);
	dataSet= query3();
	output(dataSet);
	dataSet= query4();
	output(dataSet);
	dataSet= query5();
	output(dataSet);

	}

private DataSet query5(){
	DataSet dataSet= new DataSet()
	.setName("AllowablePropertiesForCovid")
	.setDistinct(true)
	.addSelect(new Select()
	.setBinding("id"))
	.addSelect(new Select()
	.setBinding("code"))
	.addSelect(new Select()
	.setBinding("term"))
	.setMatch(new Match()
	.addOptional(new Match()
	.setProperty(RDFS.LABEL)
	.setValueVar("term"))
	.addOptional(new Match()
	.setProperty(IM.CODE)
	.setValueVar("code"))
	.addAnd(new Match()
	.setProperty(RDFS.DOMAIN)
	.setIncludeSubEntities(true))
	.addAnd(new Match()
	.setProperty(IM.IS_A)
	.setInverseOf(true)
	.setValueIn(List.of(TTIriRef.iri(SNOMED.NAMESPACE+"674814021000119106")))));
	return dataSet;
	}

private DataSet query4() {


	DataSet dataSet= new DataSet()
	.setName("AsthmaSubTypesCore")
	.setMatch(new Match()
	.setEntityId(TTIriRef.iri(SNOMED.NAMESPACE+"195967001"))
	.setIncludeSubEntities(true)
	.addOptional(new Match()
	.setProperty(RDFS.LABEL)
	.setValueVar("label"))
	.addOptional(new Match()
	.setProperty(IM.CODE)
	.setValueVar("code")))
	.addSelect(new Select()
	.setBinding("label")
	.setAlias("term"))
	.addSelect(new Select()
	.setBinding("code")
	.setAlias("code"));

	return dataSet;
	}

private DataSet query3() {
	DataSet dataSet = new DataSet()
	.setName("EncounterExpansionRelational")
	.setResultFormat(ResultFormat.RELATIONAL)
	.addSelect(new Select().setBinding("code"))
	.addSelect (new Select().setBinding("term"))
	.addSelect(new Select().setBinding("legacyCode"))
	.addSelect(new Select().setBinding("legacyTerm"))
	.addSelect(new Select().setBinding("scheme"))
	.setMatch(new Match()
	.setEntityId(TTIriRef.iri(IM.NAMESPACE+"VSET_EncounterTypes"))
	.setIncludeMembers(true)
	.addOptional(new Match()
	.setProperty(IM.CODE)
	.setValueVar("code"))
	.addOptional(new Match()
	.setProperty(RDFS.LABEL)
	.setValueVar("term"))
	.addOptional(new Match()
	.setProperty(IM.MATCHED_TO)
	.setInverseOf(true)
	.setValueObject(new Match()
	.addOptional(new Match()
	.setProperty(IM.CODE)
	.setValueVar("legacyCode"))
	.addOptional(new Match()
	.setProperty(RDFS.LABEL)
	.setValueVar("legacyTerm"))
	.addAnd(new Match()
	.setProperty(IM.HAS_SCHEME)
	.setValueVar("scheme")
	.setValueIn(List.of(IM.GRAPH_ENCOUNTERS))))));
	return dataSet;
	}

private DataSet query2() {
	DataSet dataSet = new DataSet()
	.setName("ShapesUsingDateOfBirth")
	.setUsePrefixes(true)
	.setResultFormat(ResultFormat.OBJECT)
	.addSelect(new Select()
	.setBinding("*"))
	.setMatch(new Match()
	.setEntityType(SHACL.NODESHAPE)
	.setProperty(SHACL.PROPERTY)
	.setValueObject(new Match()
	.setProperty(SHACL.PATH)
	.setValueIn(List.of(TTIriRef.iri(IM.NAMESPACE+"dateOfBirth")))));
	return dataSet;

	}

private void output(DataSet dataSet) throws IOException, DataFormatException {
	SearchService ss= new SearchService();
	String json= dataSet.getasJson();
	try (FileWriter wr= new FileWriter("c:\\examples\\query\\"+ dataSet.getName()+".json")){
	wr.write(json);
	}
	String result= ss.queryIM(dataSet);
	try (FileWriter wr= new FileWriter("c:\\examples\\"+ dataSet.getName()+"_result.json")){
	wr.write(result);
	}
	}

private DataSet query1() {
	DataSet dataSet = new DataSet()
	.setName("EncounterExpansionObjectFormat")
	.setResultFormat(ResultFormat.OBJECT)
	.setUsePrefixes(true)
	.addSelect(new Select()
	.setBinding("term"))
	.addSelect(new Select()
	.setBinding("code"))
	.addSelect(new Select()
	.setBinding("matchedTo")
	.addObject(new Select()
	.setBinding("legacyTerm"))
	.addObject(new Select()
	.setBinding("legacyCode"))
	.addObject(new Select()
	.setBinding("scheme")))
	.setMatch(new Match()
	.setEntityId(TTIriRef.iri(IM.NAMESPACE+"VSET_EncounterTypes"))
	.setIncludeMembers(true)
	.addOptional(new Match()
	.setProperty(IM.CODE)
	.setValueVar("code"))
	.addOptional(new Match()
	.setProperty(RDFS.LABEL)
	.setValueVar("term"))
	.addOptional(new Match()
	.setProperty(IM.MATCHED_TO)
	.setInverseOf(true)
	.setValueVar("matchedTo")
	.setValueObject(new Match()
	.addOptional(new Match()
	.setProperty(IM.CODE)
	.setValueVar("legacyCode"))
	.addOptional(new Match()
	.setProperty(RDFS.LABEL)
	.setValueVar("legacyTerm"))
	.addAnd(new Match()
	.setProperty(IM.HAS_SCHEME)
	.setValueVar("scheme")
	.setValueIn(List.of(IM.GRAPH_ENCOUNTERS))))));
	return dataSet;
	}

