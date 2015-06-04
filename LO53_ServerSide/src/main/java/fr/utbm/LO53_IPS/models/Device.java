package fr.utbm.LO53_IPS.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;

public class Device {
	private Integer deviceID;
	private String MACAddress;
	private String name;
	private List<Position> positions = null;
	private List<PolynomialFunctionLagrangeForm> path = null;

	public Device(){
		
	}
	
	public Device(Integer deviceID, String MACAddress, String name, List<Position> positions)
	{
		this.deviceID = deviceID;
		this.MACAddress = MACAddress;
		this.name = name;
		this.positions = positions;
		this.path = createPath();
	}
	
	public Device(String MACAddress, String name, List<Position> positions)
	{
		this.MACAddress = MACAddress;
		this.name = name;
		this.positions = positions;
		this.path = createPath();
	}
	
	public Device(String MACAddress, String Name){
		this.MACAddress = MACAddress;
		this.name = Name;
		this.path = createPath();
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
	
	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}
	
	public List<PolynomialFunctionLagrangeForm> createPath(){
		if(this.positions.size()==1){
			return null;
		}
		else if(this.positions.size()==2){
			List<double[]> coordinate = separeCoordinate(positions);
			path.add(new PolynomialFunctionLagrangeForm(coordinate.get(0), coordinate.get(1)));
		}
		else{
			for(int i = 1; i < this.positions.size()-1; ++i){
				List <Position>sub_path = this.positions.subList(i-1, i+1);
				List<double[]> coordinate = separeCoordinate(sub_path);
				path.add(new PolynomialFunctionLagrangeForm(coordinate.get(0), coordinate.get(1)));
			}			
		}
		
		return path;
	}
	
	public List<PolynomialFunctionLagrangeForm> getPath() {
		return path;
	}

	public void setPath(List<PolynomialFunctionLagrangeForm> path) {
		this.path = path;
	}

	public List<double[]> separeCoordinate(List<Position> list){
		double[] X = null;
		double[] Y = null;
		for(int i=0;i<list.size();++i){
			X[i] = list.get(i).getCoordinate().getX();
			Y[i] = list.get(i).getCoordinate().getY();
		}
		List<double[]> coordinate = new ArrayList<double[]>();
		coordinate.add(X);
		coordinate.add(Y);
		return coordinate;
	}
}
