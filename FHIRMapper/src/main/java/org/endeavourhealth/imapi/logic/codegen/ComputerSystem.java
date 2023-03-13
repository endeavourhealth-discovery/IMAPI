package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents computer system
* A named computer system considered a type of device
*/
public class ComputerSystem extends IMDMBase<ComputerSystem> {


	/**
	* Computer system constructor 
	*/
	public ComputerSystem() {
		super("ComputerSystem");
	}

	/**
	* Computer system constructor with identifier
	*/
	public ComputerSystem(String id) {
		super("ComputerSystem", id);
	}

	/**
	* Gets the device Name of this computer system
	* @return deviceName
	*/
	public String getDeviceName() {
		return getProperty("deviceName");
	}


	/**
	* Changes the device Name of this ComputerSystem
	* @param deviceName The new device Name to set
	* @return ComputerSystem
	*/
	public ComputerSystem setDeviceName(String deviceName) {
		setProperty("deviceName", deviceName);
		return this;
	}


	/**
	* Gets the name of this computer system
	* @return name
	*/
	public String getName() {
		return getProperty("name");
	}


	/**
	* Changes the name of this ComputerSystem
	* @param name The new name to set
	* @return ComputerSystem
	*/
	public ComputerSystem setName(String name) {
		setProperty("name", name);
		return this;
	}


	/**
	* Gets the device type of this computer system
	* @return deviceType
	*/
	public String getDeviceType() {
		return getProperty("deviceType");
	}


	/**
	* Changes the device type of this ComputerSystem
	* @param deviceType The new device type to set
	* @return ComputerSystem
	*/
	public ComputerSystem setDeviceType(String deviceType) {
		setProperty("deviceType", deviceType);
		return this;
	}


	/**
	* Gets the version of this computer system
	* @return version
	*/
	public String getVersion() {
		return getProperty("version");
	}


	/**
	* Changes the version of this ComputerSystem
	* @param version The new version to set
	* @return ComputerSystem
	*/
	public ComputerSystem setVersion(String version) {
		setProperty("version", version);
		return this;
	}


	/**
	* Gets the manufacturer of this computer system
	* @return manufacturer
	*/
	public Organisation getManufacturer() {
		return getProperty("manufacturer");
	}


	/**
	* Changes the manufacturer of this ComputerSystem
	* @param manufacturer The new manufacturer to set
	* @return ComputerSystem
	*/
	public ComputerSystem setManufacturer(Organisation manufacturer) {
		setProperty("manufacturer", manufacturer);
		return this;
	}


	/**
	* Gets the device version of this computer system
	* @return deviceVersion
	*/
	public String getDeviceVersion() {
		return getProperty("deviceVersion");
	}


	/**
	* Changes the device version of this ComputerSystem
	* @param deviceVersion The new device version to set
	* @return ComputerSystem
	*/
	public ComputerSystem setDeviceVersion(String deviceVersion) {
		setProperty("deviceVersion", deviceVersion);
		return this;
	}


	/**
	* Gets the serial number of this computer system
	* @return serialNumber
	*/
	public String getSerialNumber() {
		return getProperty("serialNumber");
	}


	/**
	* Changes the serial number of this ComputerSystem
	* @param serialNumber The new serial number to set
	* @return ComputerSystem
	*/
	public ComputerSystem setSerialNumber(String serialNumber) {
		setProperty("serialNumber", serialNumber);
		return this;
	}


	/**
	* Gets the uDI human readable of this computer system
	* @return udiHumanReadable
	*/
	public String getUdiHumanReadable() {
		return getProperty("udiHumanReadable");
	}


	/**
	* Changes the uDI human readable of this ComputerSystem
	* @param udiHumanReadable The new uDI human readable to set
	* @return ComputerSystem
	*/
	public ComputerSystem setUdiHumanReadable(String udiHumanReadable) {
		setProperty("udiHumanReadable", udiHumanReadable);
		return this;
	}


	/**
	* Gets the uDI machine readable of this computer system
	* @return udiMachineReadable
	*/
	public String getUdiMachineReadable() {
		return getProperty("udiMachineReadable");
	}


	/**
	* Changes the uDI machine readable of this ComputerSystem
	* @param udiMachineReadable The new uDI machine readable to set
	* @return ComputerSystem
	*/
	public ComputerSystem setUdiMachineReadable(String udiMachineReadable) {
		setProperty("udiMachineReadable", udiMachineReadable);
		return this;
	}
}

