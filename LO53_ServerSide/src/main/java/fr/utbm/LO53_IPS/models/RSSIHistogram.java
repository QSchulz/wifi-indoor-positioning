package fr.utbm.LO53_IPS.models;

import java.util.ArrayList;
import java.util.Set;

public class RSSIHistogram {
	private Integer RSSIHistogramID;
	private Device device; // if histogram comming from an AP measurement : it's linked to a device (can be null)
	private AccessPoint accessPoint;
	private HistogramFingerprint fingerprint;
	private Set<BarRSSIHistogram> value;
	
	public RSSIHistogram(){}

	public RSSIHistogram(Set<BarRSSIHistogram> value, AccessPoint accessPoint, HistogramFingerprint fingerprint){
		this.value = value;
		this.accessPoint = accessPoint;
		this.fingerprint = fingerprint;
		this.device = null;
	}
	
	public RSSIHistogram(Set<BarRSSIHistogram> value, Device device, AccessPoint accessPoint){
		this.value = value;
		this.device = device;
		this.accessPoint = accessPoint;
	}
	
	public Set<BarRSSIHistogram> getValue() {
		return value;
	}

	public void setValue(Set<BarRSSIHistogram> value) {
		this.value = value;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public AccessPoint getAccessPoint() {
		return accessPoint;
	}

	public void setAccessPoint(AccessPoint accessPoint) {
		this.accessPoint = accessPoint;
	}
	
	public Integer getRSSIHistogramID() {
		return RSSIHistogramID;
	}

	public void setRSSIHistogramID(Integer rSSIHistogramID) {
		RSSIHistogramID = rSSIHistogramID;
	}

	public HistogramFingerprint getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(HistogramFingerprint fingerprint) {
		this.fingerprint = fingerprint;
	}
}
