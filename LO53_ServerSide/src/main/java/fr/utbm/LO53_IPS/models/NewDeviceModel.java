package fr.utbm.LO53_IPS.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewDeviceModel {
	
	private String DeviceMACAddress;
	
	public NewDeviceModel(){}
	
	public NewDeviceModel(String DeviceMACAddress){
		this.DeviceMACAddress = DeviceMACAddress;
	}

	public String getDeviceMACAddress() {
		return DeviceMACAddress;
	}

	public void setDeviceMACAddress(String deviceMACAddress) throws Exception {
		Pattern p = Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
		Matcher m = p.matcher(deviceMACAddress);
		boolean isCorrectFormat = m.matches();
		
		if(isCorrectFormat){
			DeviceMACAddress = deviceMACAddress;
		} else {
			throw new Exception("Setting the device mac addres failed : string not a mac address");
		}
	}
	
}
