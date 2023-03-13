package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents device
* In the context of the health data model a device is normally used to relate to the entry of a record or in relation to its use in a procedure or operation<p>Devices are categorised and full defined via the information model relationships. Thus each device represents an instance of a kind of device defined in the information model
*/
public class Device extends IMDMBase<Device> {


	/**
	* Device constructor 
	*/
	public Device() {
		super("Device");
	}

	/**
	* Device constructor with identifier
	*/
	public Device(String id) {
		super("Device", id);
	}

	/**
	* Gets the device Name of this device
	* @return deviceName
	*/
	public String getDeviceName() {
		return getProperty("deviceName");
	}


	/**
	* Changes the device Name of this Device
	* @param deviceName The new device Name to set
	* @return Device
	*/
	public Device setDeviceName(String deviceName) {
		setProperty("deviceName", deviceName);
		return this;
	}


	/**
	* Gets the device type of this device
	* @return deviceType
	*/
	public String getDeviceType() {
		return getProperty("deviceType");
	}


	/**
	* Changes the device type of this Device
	* @param deviceType The new device type to set
	* @return Device
	*/
	public Device setDeviceType(String deviceType) {
		setProperty("deviceType", deviceType);
		return this;
	}


	/**
	* Gets the manufacturer of this device
	* @return manufacturer
	*/
	public Organisation getManufacturer() {
		return getProperty("manufacturer");
	}


	/**
	* Changes the manufacturer of this Device
	* @param manufacturer The new manufacturer to set
	* @return Device
	*/
	public Device setManufacturer(Organisation manufacturer) {
		setProperty("manufacturer", manufacturer);
		return this;
	}


	/**
	* Gets the device version of this device
	* @return deviceVersion
	*/
	public String getDeviceVersion() {
		return getProperty("deviceVersion");
	}


	/**
	* Changes the device version of this Device
	* @param deviceVersion The new device version to set
	* @return Device
	*/
	public Device setDeviceVersion(String deviceVersion) {
		setProperty("deviceVersion", deviceVersion);
		return this;
	}


	/**
	* Gets the serial number of this device
	* @return serialNumber
	*/
	public String getSerialNumber() {
		return getProperty("serialNumber");
	}


	/**
	* Changes the serial number of this Device
	* @param serialNumber The new serial number to set
	* @return Device
	*/
	public Device setSerialNumber(String serialNumber) {
		setProperty("serialNumber", serialNumber);
		return this;
	}


	/**
	* Gets the uDI human readable of this device
	* @return udiHumanReadable
	*/
	public String getUdiHumanReadable() {
		return getProperty("udiHumanReadable");
	}


	/**
	* Changes the uDI human readable of this Device
	* @param udiHumanReadable The new uDI human readable to set
	* @return Device
	*/
	public Device setUdiHumanReadable(String udiHumanReadable) {
		setProperty("udiHumanReadable", udiHumanReadable);
		return this;
	}


	/**
	* Gets the uDI machine readable of this device
	* @return udiMachineReadable
	*/
	public String getUdiMachineReadable() {
		return getProperty("udiMachineReadable");
	}


	/**
	* Changes the uDI machine readable of this Device
	* @param udiMachineReadable The new uDI machine readable to set
	* @return Device
	*/
	public Device setUdiMachineReadable(String udiMachineReadable) {
		setProperty("udiMachineReadable", udiMachineReadable);
		return this;
	}
}

