package org.endeavourhealth.imapi.model.mapping;

import java.util.List;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

import com.fasterxml.jackson.annotation.JsonAlias;

public class PRSBMapping {

	private List<Dataset> dataset;

	public List<Dataset> getDataset() {
		return dataset;
	}

	public void setDataset(List<Dataset> dataset) {
		this.dataset = dataset;
	}

	public static class Dataset {

		private String id;
		private String effectiveDate;
		private String statusCode;
		private String versionLabel;
		private String prefix;
		private String transactionId;
		private String transactionEffectiveDate;
		private String transactionStatusCode;
		private String transactionVersionLabel;
		private String transactionType;
		private String transactionIddisplay;
		private String shortName;
		private List<Implementation> implementation;
		private List<Inherit> inherit;
		private String iddisplay;
		private List<DatasetObject> name;
		private List<DatasetObject> synonym;
		private List<DatasetObject> desc;
		private List<DatasetConcept> concept;

		public static class DatasetObject {
			private String language;
			@JsonAlias("#text")
			private String text;
			private String name;

			public String getText() {
				return text;
			}

			public void setText(String text) {
				this.text = text;
			}

			public String getLanguage() {
				return language;
			}

			public void setLanguage(String language) {
				this.language = language;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}
		}

		public static class Implementation {
			private String shortName;

			public String getShortName() {
				return shortName;
			}

			public void setShortName(String shortName) {
				this.shortName = shortName;
			}
		}

		public static class Relationship {
			private String type;
			private String ref;
			private String flexibility;
			private String prefix;
			private String datasetId;
			private String datasetEffectiveDate;
			private String datasetStatusCode;
			private String datasetVersionLabel;
			private String iType;
			private String iStatusCode;
			private String iEffectiveDate;
			private String iVersionLabel;
			private String refdisplay;
			private List<DatasetObject> name;

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

			public String getRef() {
				return ref;
			}

			public void setRef(String ref) {
				this.ref = ref;
			}

			public String getFlexibility() {
				return flexibility;
			}

			public void setFlexibility(String flexibility) {
				this.flexibility = flexibility;
			}

			public String getPrefix() {
				return prefix;
			}

			public void setPrefix(String prefix) {
				this.prefix = prefix;
			}

			public String getDatasetId() {
				return datasetId;
			}

			public void setDatasetId(String datasetId) {
				this.datasetId = datasetId;
			}

			public String getDatasetEffectiveDate() {
				return datasetEffectiveDate;
			}

			public void setDatasetEffectiveDate(String datasetEffectiveDate) {
				this.datasetEffectiveDate = datasetEffectiveDate;
			}

			public String getDatasetStatusCode() {
				return datasetStatusCode;
			}

			public void setDatasetStatusCode(String datasetStatusCode) {
				this.datasetStatusCode = datasetStatusCode;
			}

			public String getDatasetVersionLabel() {
				return datasetVersionLabel;
			}

			public void setDatasetVersionLabel(String datasetVersionLabel) {
				this.datasetVersionLabel = datasetVersionLabel;
			}

			public String getiType() {
				return iType;
			}

			public void setiType(String iType) {
				this.iType = iType;
			}

			public String getiStatusCode() {
				return iStatusCode;
			}

			public void setiStatusCode(String iStatusCode) {
				this.iStatusCode = iStatusCode;
			}

			public String getiEffectiveDate() {
				return iEffectiveDate;
			}

			public void setiEffectiveDate(String iEffectiveDate) {
				this.iEffectiveDate = iEffectiveDate;
			}

			public String getRefdisplay() {
				return refdisplay;
			}

			public void setRefdisplay(String refdisplay) {
				this.refdisplay = refdisplay;
			}

			public List<DatasetObject> getName() {
				return name;
			}

			public void setName(List<DatasetObject> name) {
				this.name = name;
			}

			public String getiVersionLabel() {
				return iVersionLabel;
			}

			public void setiVersionLabel(String iVersionLabel) {
				this.iVersionLabel = iVersionLabel;
			}

		}

		public static class ValueSet {
			private List<ConceptId> conceptList;

			public List<ConceptId> getConceptList() {
				return conceptList;
			}

			public void setConceptList(List<ConceptId> conceptList) {
				this.conceptList = conceptList;
			}
		}

		public static class Example {
			private String type;
			@JsonAlias("#text")
			private String text;

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

			public String getText() {
				return text;
			}

			public void setText(String text) {
				this.text = text;
			}

		}

		public static class Inherit {
			private String ref;
			private String effectiveDate;
			private String prefix;
			private String datasetId;
			private String datasetEffectiveDate;
			private String datasetStatusCode;
			private String datasetVersionLabel;
			private String iType;
			private String iStatusCode;
			private String iEffectiveDate;
			private String originalId;
			private String originalEffectiveDate;
			private String originalStatusCode;
			private String originalPrefix;
			private String refdisplay;

			public String getOriginalId() {
				return originalId;
			}

			public void setOriginalId(String originalId) {
				this.originalId = originalId;
			}

			public String getOriginalEffectiveDate() {
				return originalEffectiveDate;
			}

			public void setOriginalEffectiveDate(String originalEffectiveDate) {
				this.originalEffectiveDate = originalEffectiveDate;
			}

			public String getOriginalStatusCode() {
				return originalStatusCode;
			}

			public void setOriginalStatusCode(String originalStatusCode) {
				this.originalStatusCode = originalStatusCode;
			}

			public String getOriginalPrefix() {
				return originalPrefix;
			}

			public void setOriginalPrefix(String originalPrefix) {
				this.originalPrefix = originalPrefix;
			}

			public String getRef() {
				return ref;
			}

			public void setRef(String ref) {
				this.ref = ref;
			}

			public String getEffectiveDate() {
				return effectiveDate;
			}

			public void setEffectiveDate(String effectiveDate) {
				this.effectiveDate = effectiveDate;
			}

			public String getPrefix() {
				return prefix;
			}

			public void setPrefix(String prefix) {
				this.prefix = prefix;
			}

			public String getDatasetId() {
				return datasetId;
			}

			public void setDatasetId(String datasetId) {
				this.datasetId = datasetId;
			}

			public String getDatasetEffectiveDate() {
				return datasetEffectiveDate;
			}

			public void setDatasetEffectiveDate(String datasetEffectiveDate) {
				this.datasetEffectiveDate = datasetEffectiveDate;
			}

			public String getDatasetStatusCode() {
				return datasetStatusCode;
			}

			public void setDatasetStatusCode(String datasetStatusCode) {
				this.datasetStatusCode = datasetStatusCode;
			}

			public String getDatasetVersionLabel() {
				return datasetVersionLabel;
			}

			public void setDatasetVersionLabel(String datasetVersionLabel) {
				this.datasetVersionLabel = datasetVersionLabel;
			}

			public String getiType() {
				return iType;
			}

			public void setiType(String iType) {
				this.iType = iType;
			}

			public String getiStatusCode() {
				return iStatusCode;
			}

			public void setiStatusCode(String iStatusCode) {
				this.iStatusCode = iStatusCode;
			}

			public String getiEffectiveDate() {
				return iEffectiveDate;
			}

			public void setiEffectiveDate(String iEffectiveDate) {
				this.iEffectiveDate = iEffectiveDate;
			}

			public String getRefdisplay() {
				return refdisplay;
			}

			public void setRefdisplay(String refdisplay) {
				this.refdisplay = refdisplay;
			}

		}

		public static class ValueDomain {
			private String type;
			private List<ValueDomainProperty> property;
			private List<ConceptId> conceptList;
			private List<Example> example;

			public List<ValueDomainProperty> getProperty() {
				return property;
			}

			public void setProperty(List<ValueDomainProperty> property) {
				this.property = property;
			}

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

			public List<ConceptId> getConceptList() {
				return conceptList;
			}

			public void setConceptList(List<ConceptId> conceptList) {
				this.conceptList = conceptList;
			}

			public List<Example> getExample() {
				return example;
			}

			public void setExample(List<Example> example) {
				this.example = example;
			}
		}

		public static class ValueDomainProperty {
			private String maxLength;
			private String minLength;
			private String timeStampPrecision;
			@JsonAlias("default")
			private String defaultValue;
			private String fixed;
			private String maxInclude;

			public String getMaxLength() {
				return maxLength;
			}

			public void setMaxLength(String maxLength) {
				this.maxLength = maxLength;
			}

			public String getTimeStampPrecision() {
				return timeStampPrecision;
			}

			public void setTimeStampPrecision(String timeStampPrecision) {
				this.timeStampPrecision = timeStampPrecision;
			}

			public String getMinLength() {
				return minLength;
			}

			public void setMinLength(String minLength) {
				this.minLength = minLength;
			}

			public String getDefaultValue() {
				return defaultValue;
			}

			public void setDefaultValue(String defaultValue) {
				this.defaultValue = defaultValue;
			}

			public String getFixed() {
				return fixed;
			}

			public void setFixed(String fixed) {
				this.fixed = fixed;
			}

			public String getMaxInclude() {
				return maxInclude;
			}

			public void setMaxInclude(String maxInclude) {
				this.maxInclude = maxInclude;
			}

		}

		public static class ConceptId {
			private String id;

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

		}

		public static class DatasetConcept extends Dataset {
			private String type;
			private String minimumMultiplicity;
			private String maximumMultiplicity;
			private String conformance;
			private String isMandatory;
			private List<DatasetObject> context;
			private List<DatasetObject> comment;
			private List<Relationship> relationship;
			private List<DatasetObject> source;
			private List<DatasetObject> rationale;
			private List<DatasetObject> property;
			private List<DatasetObject> operationalization;
			private List<ValueDomain> valueDomain;
			private List<ValueSet> valueSet;

			public boolean relNameIsSameWithConceptName() {
				return this.getConceptName().equalsIgnoreCase(getRelName());
			}

			public String getRelName() {
				return this.getRelationship().get(0).getName().get(0).getText();
			}

			public String getConceptName() {
				return this.getName().get(0).getText();
			}
			
			public String getRelStatus() {
				return this.getRelationship().get(0).getiStatusCode();
			}
			
			public TTIriRef getRelStatusCodeTTIriRef() {
				switch (getRelStatus()) {
				case "draft":
					return IM.DRAFT;
				case "active":
					return IM.ACTIVE;
				case "cancelled":
					return IM.INACTIVE;
				}
				return null;
			}
			
			public boolean isFolder() {
				return type.equals("group");
			}
			
			public TTArray getTypeRefs() {
				switch (type) {
				case "group":
					return new TTArray().add(IM.FOLDER);
				case "item":
					return new TTArray().add(IM.RECORD);
				}
				return null;
			}

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

			public String getMinimumMultiplicity() {
				return minimumMultiplicity;
			}

			public void setMinimumMultiplicity(String minimumMultiplicity) {
				this.minimumMultiplicity = minimumMultiplicity;
			}

			public String getMaximumMultiplicity() {
				return maximumMultiplicity;
			}

			public void setMaximumMultiplicity(String maximumMultiplicity) {
				this.maximumMultiplicity = maximumMultiplicity;
			}

			public String getConformance() {
				return conformance;
			}

			public void setConformance(String conformance) {
				this.conformance = conformance;
			}

			public String getIsMandatory() {
				return isMandatory;
			}

			public void setIsMandatory(String isMandatory) {
				this.isMandatory = isMandatory;
			}

			public List<DatasetObject> getContext() {
				return context;
			}

			public void setContext(List<DatasetObject> context) {
				this.context = context;
			}

			public List<Relationship> getRelationship() {
				return relationship;
			}

			public void setRelationship(List<Relationship> relationship) {
				this.relationship = relationship;
			}

			public List<DatasetObject> getRationale() {
				return rationale;
			}

			public void setRationale(List<DatasetObject> rationale) {
				this.rationale = rationale;
			}

			public List<DatasetObject> getOperationalization() {
				return operationalization;
			}

			public void setOperationalization(List<DatasetObject> operationalization) {
				this.operationalization = operationalization;
			}

			public List<ValueDomain> getValueDomain() {
				return valueDomain;
			}

			public void setValueDomain(List<ValueDomain> valueDomain) {
				this.valueDomain = valueDomain;
			}

			public List<DatasetObject> getSource() {
				return source;
			}

			public void setSource(List<DatasetObject> source) {
				this.source = source;
			}

			public List<DatasetObject> getProperty() {
				return property;
			}

			public void setProperty(List<DatasetObject> property) {
				this.property = property;
			}

			public List<ValueSet> getValueSet() {
				return valueSet;
			}

			public void setValueSet(List<ValueSet> valueSet) {
				this.valueSet = valueSet;
			}

			public List<DatasetObject> getComment() {
				return comment;
			}

			public void setComment(List<DatasetObject> comment) {
				this.comment = comment;
			}

		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getEffectiveDate() {
			return effectiveDate;
		}

		public void setEffectiveDate(String effectiveDate) {
			this.effectiveDate = effectiveDate;
		}

		public String getStatusCode() {
			return statusCode;
		}

		public TTIriRef getStatusCodeTTIriRef() {
			switch (statusCode) {
			case "draft":
				return IM.DRAFT;
			case "active":
				return IM.ACTIVE;
			case "cancelled":
				return IM.INACTIVE;
			}
			return null;
		}

		public void setStatusCode(String statusCode) {
			this.statusCode = statusCode;
		}

		public String getVersionLabel() {
			return versionLabel;
		}

		public void setVersionLabel(String versionLabel) {
			this.versionLabel = versionLabel;
		}

		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}

		public String getTransactionId() {
			return transactionId;
		}

		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}

		public String getTransactionEffectiveDate() {
			return transactionEffectiveDate;
		}

		public void setTransactionEffectiveDate(String transactionEffectiveDate) {
			this.transactionEffectiveDate = transactionEffectiveDate;
		}

		public String getTransactionStatusCode() {
			return transactionStatusCode;
		}

		public void setTransactionStatusCode(String transactionStatusCode) {
			this.transactionStatusCode = transactionStatusCode;
		}

		public String getTransactionVersionLabel() {
			return transactionVersionLabel;
		}

		public void setTransactionVersionLabel(String transactionVersionLabel) {
			this.transactionVersionLabel = transactionVersionLabel;
		}

		public String getTransactionType() {
			return transactionType;
		}

		public void setTransactionType(String transactionType) {
			this.transactionType = transactionType;
		}

		public String getTransactionIddisplay() {
			return transactionIddisplay;
		}

		public void setTransactionIddisplay(String transactionIddisplay) {
			this.transactionIddisplay = transactionIddisplay;
		}

		public String getShortName() {
			return shortName;
		}

		public void setShortName(String shortName) {
			this.shortName = shortName;
		}

		public List<Implementation> getImplementation() {
			return implementation;
		}

		public void setImplementation(List<Implementation> implementation) {
			this.implementation = implementation;
		}

		public String getIddisplay() {
			return iddisplay;
		}

		public void setIddisplay(String iddisplay) {
			this.iddisplay = iddisplay;
		}

		public List<DatasetObject> getName() {
			return name;
		}

		public void setName(List<DatasetObject> name) {
			this.name = name;
		}

		public List<DatasetObject> getDesc() {
			return desc;
		}

		public void setDesc(List<DatasetObject> desc) {
			this.desc = desc;
		}

		public List<DatasetConcept> getConcept() {
			return concept;
		}

		public void setConcept(List<DatasetConcept> concept) {
			this.concept = concept;
		}

		public List<DatasetObject> getSynonym() {
			return synonym;
		}

		public void setSynonym(List<DatasetObject> synonym) {
			this.synonym = synonym;
		}

		public List<Inherit> getInherit() {
			return inherit;
		}

		public void setInherit(List<Inherit> inherit) {
			this.inherit = inherit;
		}

	}
}
