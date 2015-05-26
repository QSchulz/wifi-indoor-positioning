package fr.utbm.LO53_IPS.models;

public class BarRSSIHistogram {
	private Integer barRSSIHistogramID;
	private Double value;
	private Integer occurences;
	private RSSIHistogram rssiHistogram;
	
	public BarRSSIHistogram(){}
	
	public BarRSSIHistogram(double value, int occurences, RSSIHistogram rssiHistogram){
		this.value = value;
		this.occurences = occurences;
		this.rssiHistogram = rssiHistogram;
	}
	
	public BarRSSIHistogram(Integer barRSSIHistogramID, double value, int occurences){
		this.value = value;
		this.occurences = occurences;
		this.barRSSIHistogramID = barRSSIHistogramID;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getOccurences() {
		return occurences;
	}

	public void setOccurences(int occurences) {
		this.occurences = occurences;
	}

	public Integer getBarRSSIHistogramID() {
		return barRSSIHistogramID;
	}

	public void setBarRSSIHistogramID(Integer barRSSIHistogramID) {
		this.barRSSIHistogramID = barRSSIHistogramID;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public void setOccurences(Integer occurences) {
		this.occurences = occurences;
	}

	public RSSIHistogram getRssiHistogram() {
		return rssiHistogram;
	}

	public void setRssiHistogram(RSSIHistogram rssiHistogram) {
		this.rssiHistogram = rssiHistogram;
	}
}
