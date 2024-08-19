package org.endeavourhealth.imapi.model.fhir;

import com.fasterxml.jackson.annotation.*;

public class ValueSet {
    private String resourceType;
    private String id;
    private Meta meta;
    private Text text;
    private Extension[] extension;
    private String url;
    private Identifier[] identifier;
    private String version;
    private String name;
    private String title;
    private String status;
    private boolean experimental;
    private String publisher;
    private Contact[] contact;
    private String description;
    private Jurisdiction[] jurisdiction;
    private Compose compose;

    @JsonProperty("resourceType")
    public String getResourceType() { return resourceType; }
    @JsonProperty("resourceType")
    public void setResourceType(String value) { this.resourceType = value; }

    @JsonProperty("id")
    public String getID() { return id; }
    @JsonProperty("id")
    public void setID(String value) { this.id = value; }

    @JsonProperty("meta")
    public Meta getMeta() { return meta; }
    @JsonProperty("meta")
    public void setMeta(Meta value) { this.meta = value; }

    @JsonProperty("text")
    public Text getText() { return text; }
    @JsonProperty("text")
    public void setText(Text value) { this.text = value; }

    @JsonProperty("extension")
    public Extension[] getExtension() { return extension; }
    @JsonProperty("extension")
    public void setExtension(Extension[] value) { this.extension = value; }

    @JsonProperty("url")
    public String getURL() { return url; }
    @JsonProperty("url")
    public void setURL(String value) { this.url = value; }

    @JsonProperty("identifier")
    public Identifier[] getIdentifier() { return identifier; }
    @JsonProperty("identifier")
    public void setIdentifier(Identifier[] value) { this.identifier = value; }

    @JsonProperty("version")
    public String getVersion() { return version; }
    @JsonProperty("version")
    public void setVersion(String value) { this.version = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    @JsonProperty("title")
    public String getTitle() { return title; }
    @JsonProperty("title")
    public void setTitle(String value) { this.title = value; }

    @JsonProperty("status")
    public String getStatus() { return status; }
    @JsonProperty("status")
    public void setStatus(String value) { this.status = value; }

    @JsonProperty("experimental")
    public boolean getExperimental() { return experimental; }
    @JsonProperty("experimental")
    public void setExperimental(boolean value) { this.experimental = value; }

    @JsonProperty("publisher")
    public String getPublisher() { return publisher; }
    @JsonProperty("publisher")
    public void setPublisher(String value) { this.publisher = value; }

    @JsonProperty("contact")
    public Contact[] getContact() { return contact; }
    @JsonProperty("contact")
    public void setContact(Contact[] value) { this.contact = value; }

    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    @JsonProperty("jurisdiction")
    public Jurisdiction[] getJurisdiction() { return jurisdiction; }
    @JsonProperty("jurisdiction")
    public void setJurisdiction(Jurisdiction[] value) { this.jurisdiction = value; }

    @JsonProperty("compose")
    public Compose getCompose() { return compose; }
    @JsonProperty("compose")
    public void setCompose(Compose value) { this.compose = value; }
}