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
	
	public String buildDeviceMACAddressesJSON(List<String> MACAddresses){
		
		String JSONMACAddressesString = "{\"MACAddresses\":[";
		List<String> quotedMACAddresses = surroundStringListWithQuotes(MACAddresses);
		JSONMACAddressesString += Joiner.on(",").join(quotedMACAddresses);
		JSONMACAddressesString += "]}";
		return JSONMACAddressesString;
	}
	
	private List<String> surroundStringListWithQuotes(List<String> notQuotedStrings){
		
		List<String> quotedStrings = new ArrayList<String>();
		
		for(String s : notQuotedStrings){
			quotedStrings.add(surroundWithQuotes(s));
		}
		
		return quotedStrings;
	}
	
	private String surroundWithQuotes(String string){
		return "\""+string+"\"";
	}
	
}
