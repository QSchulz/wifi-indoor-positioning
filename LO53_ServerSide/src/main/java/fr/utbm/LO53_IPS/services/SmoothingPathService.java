package fr.utbm.LO53_IPS.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;

import fr.utbm.LO53_IPS.models.Device;
import fr.utbm.LO53_IPS.models.Position;

public class SmoothingPathService {
	
	
	public void createPath(Device dev){
		if(dev.getPositions().size()==1){
			dev.setPath(null);
		}
		else if(dev.getPositions().size()==2){
			List<double[]> coordinate = separeCoordinate(dev.getPositions());
			dev.getPath().add(new PolynomialFunctionLagrangeForm(coordinate.get(0), coordinate.get(1)));
		}
		else{
			for(int i = 1; i < dev.getPositions().size()-1; ++i){
				List <Position>sub_path = dev.getPositions().subList(i-1, i+1);
				List<double[]> coordinate = separeCoordinate(sub_path);
				dev.getPath().add(new PolynomialFunctionLagrangeForm(coordinate.get(0), coordinate.get(1)));
			}			
		}
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
