package fr.utbm.LO53_IPS.services;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Joiner;

import fr.utbm.LO53_IPS.models.Position;

/* Check validity with http://jsonlint.com/ */
public class JSONService {
	
	public JSONService(){}
	
	public String buildPositionListJSON(Set<Position> positions){
		
		String JSONPositionsString = "{\"positions\":[";
		
		Set<String> JSONPositions = new HashSet();
		
		for(Position p:positions){
			JSONPositions.add(p.toJSON());
		}
		
		JSONPositionsString += Joiner.on(",").join(JSONPositions);
		JSONPositionsString += "]}";
		return JSONPositionsString;
	}
	
}
