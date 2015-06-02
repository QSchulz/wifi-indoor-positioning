package fr.utbm.LO53_IPS.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FingerprintMap {
	private int length; // length of the map
	private int width; // width of the map
	private List<HistogramFingerprint> map = new ArrayList<HistogramFingerprint>();
	
	public FingerprintMap(){}
	
	public FingerprintMap(int length, int width, ArrayList<HistogramFingerprint> map){
		this.length = length;
		this.width = width;
		this.map = map;
	}
	
	public List<HistogramFingerprint> getMap() {
		return map;
	}

	public void setMap(List<HistogramFingerprint> map) {
		this.map = map;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}