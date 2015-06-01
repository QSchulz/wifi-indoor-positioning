package fr.utbm.LO53_IPS.models;

import java.sql.Timestamp;

public class Position {
	
	private Integer positionID;
	private Coordinate coordinate;
	private Timestamp timestamp;
	private Device device;
	
	public Position(){}
	
	public Position(Integer positionID, Coordinate coordinate, Timestamp timestamp){
		this.positionID = positionID;
		this.coordinate = coordinate;
		this.timestamp = timestamp;
		this.device = null;
	}
	
	public Position(Coordinate coordinate, Timestamp timestamp){
		this.coordinate = coordinate;
		this.timestamp = timestamp;
		this.device = null;
	}
	
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}
	
	public Integer getPositionID() {
		return positionID;
	}
	public void setPositionID(Integer positionID) {
		this.positionID = positionID;
	}
	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}	
	
	public String toJSON(){
		return "{"+
				"\"x\":"+coordinate.getX()+","+
				"\"y\":"+coordinate.getY()+","+
				"\"timestamp\":\""+timestamp+"\"" +
				"}";
	}
	
//	public Timestamp getTimestamp() {
//		return timestamp;
//	}
//	public void setTimestamp(Timestamp timestamp) {
//		this.timestamp = timestamp;
//	}
//	
//	public void setTimestamp(Date date) {
//		this.timestamp = new Timestamp(date.getTime());
//	}
	
}
