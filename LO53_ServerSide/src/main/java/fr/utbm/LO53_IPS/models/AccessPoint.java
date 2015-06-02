package fr.utbm.LO53_IPS.models;


public class AccessPoint {
	private Integer AccessPointID;
	private String MACAddress;
	private String ipAddress;
	private Coordinate coordinate;

	public AccessPoint(){
		this.setAccessPointID(0);
		this.setMACAddress(new String());
		this.setCoordinate(new Coordinate());
	}
	
	public AccessPoint(Integer AccessPointID, String MACAddress, Coordinate coordinate){
		this.AccessPointID = AccessPointID;
		this.MACAddress = MACAddress;
		this.coordinate = coordinate;
	}

	public AccessPoint(String MACAddress, Coordinate coordinate) {
		this.MACAddress = MACAddress;
		this.coordinate = coordinate;
	}

	public Integer getAccessPointID() {
		return AccessPointID;
	}

	public void setAccessPointID(Integer accessPointID) {
		AccessPointID = accessPointID;
	}

	public String getMACAddress() {
		return MACAddress;
	}

	public void setMACAddress(String mACAddress) {
		MACAddress = mACAddress;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
