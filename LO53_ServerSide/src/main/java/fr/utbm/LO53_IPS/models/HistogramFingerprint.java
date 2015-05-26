package fr.utbm.LO53_IPS.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HistogramFingerprint {
	
	private Integer fingerprintID;
	private Set<RSSIHistogram> histogramSamples;
	private Coordinate coordinate;
	
	public HistogramFingerprint(){}
	
	public HistogramFingerprint(Set<RSSIHistogram> Histogram_Samples, Coordinate coordinate){
		this.histogramSamples = Histogram_Samples;
		this.coordinate = coordinate;
	}
	
	public HistogramFingerprint(Coordinate coordinate){
		this.histogramSamples = new HashSet<RSSIHistogram>();
		this.coordinate = coordinate;
	}
	
	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public void setHistogramSamples(Set<RSSIHistogram> histogramSamples) {
		this.histogramSamples = histogramSamples;
	}

	public Set<RSSIHistogram> getHistogramSamples() {
		return histogramSamples;
	}

	public Integer getFingerprintID() {
		return fingerprintID;
	}

	public void setFingerprintID(Integer fingerprintID) {
		this.fingerprintID = fingerprintID;
	}
	
}
