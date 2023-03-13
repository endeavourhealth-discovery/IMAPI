package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents container
* Information about a container usually used by specimens that holds a specimen
*/
public class Container extends IMDMBase<Container> {


	/**
	* Container constructor 
	*/
	public Container() {
		super("Container");
	}

	/**
	* Container constructor with identifier
	*/
	public Container(String id) {
		super("Container", id);
	}

	/**
	* Gets the container capacity of this container
	* @return containerCapacity
	*/
	public String getContainerCapacity() {
		return getProperty("containerCapacity");
	}


	/**
	* Changes the container capacity of this Container
	* @param containerCapacity The new container capacity to set
	* @return Container
	*/
	public Container setContainerCapacity(String containerCapacity) {
		setProperty("containerCapacity", containerCapacity);
		return this;
	}


	/**
	* Gets the container description of this container
	* @return containerDescription
	*/
	public String getContainerDescription() {
		return getProperty("containerDescription");
	}


	/**
	* Changes the container description of this Container
	* @param containerDescription The new container description to set
	* @return Container
	*/
	public Container setContainerDescription(String containerDescription) {
		setProperty("containerDescription", containerDescription);
		return this;
	}


	/**
	* Gets the container type of this container
	* @return containerType
	*/
	public String getContainerType() {
		return getProperty("containerType");
	}


	/**
	* Changes the container type of this Container
	* @param containerType The new container type to set
	* @return Container
	*/
	public Container setContainerType(String containerType) {
		setProperty("containerType", containerType);
		return this;
	}
}

