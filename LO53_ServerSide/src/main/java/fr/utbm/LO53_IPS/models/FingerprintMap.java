package fr.utbm.LO53_IPS.models;

import java.util.ArrayList;

public class FingerprintMap {
	private int length; // length of the map
	private int width; // width of the map
	private ArrayList<HistogramFingerprint> map;
	
	public FingerprintMap(){}
	
	public FingerprintMap(int length, int width, ArrayList<HistogramFingerprint> map){
		this.length = length;
		this.width = width;
		this.map = map;
	}
}
