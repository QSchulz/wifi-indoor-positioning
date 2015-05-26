package fr.utbm.LO53_IPS.models;

import java.util.List;
import java.util.Set;


public class Device {
	private Integer deviceID;
	private String MACAddress;
	private String name;
	private List<Position> positions = null;
	
	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}

	public Device(){
		
	}
	
	public Device(Integer deviceID, String MACAddress, String name, List<Position> positions)
	{
		this.deviceID = deviceID;
		this.MACAddress = MACAddress;
		this.name = name;
		this.positions = positions;
	}
	
	public Device(String MACAddress, String name, List<Position> positions)
	{
		this.MACAddress = MACAddress;
		this.name = name;
		this.positions = positions;
	}
	
	public Device(String MACAddress, String Name){
		this.MACAddress = MACAddress;
		this.name = Name;
	}
	
	public Integer getDeviceID(){
		return deviceID;
	}
	
	public void setDeviceID(Integer deviceID){
		this.deviceID = deviceID;
	}
	
	public String getMACAddress(){
		return MACAddress;
	}
	
	public void setMACAddress(String MACAddress){
		this.MACAddress = MACAddress;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String Name){
		this.name = Name;
	}
}
