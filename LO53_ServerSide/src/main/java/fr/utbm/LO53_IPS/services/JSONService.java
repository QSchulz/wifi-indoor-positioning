package fr.utbm.LO53_IPS.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Joiner;

import fr.utbm.LO53_IPS.models.Position;

/* Check validity with http://jsonlint.com/ */
public class JSONService {
	
	public JSONService(){}
	
	public String buildPositionListJSON(List<Position> positions){
		
		String JSONPositionsString = "{\"positions\":[";
		
		List<String> JSONPositions = new ArrayList();
		
		for(Position p:positions){
			JSONPositions.add(p.toJSON());
		}
		
		JSONPositionsString += Joiner.on(",").join(JSONPositions);
		JSONPositionsString += "]}";
		return JSONPositionsString;
	}
}
