package fr.utbm.LO53_IPS.services;

import java.util.Set;

import fr.utbm.LO53_IPS.models.Position;

public class PositionningService {

	private DatabaseService databaseService;
	
	public PositionningService(){
		databaseService = new DatabaseService();
	}
	
	public PositionningService(DatabaseService databaseService){
		this.databaseService = databaseService;
	}
	
	public Position computePosition(){
		return null;
	}
	
	public Set<Position> getAllPositions(String macAddress){
		return databaseService.getAllPositionsFromDatabase(macAddress);
	}
	
	public Position getLastKnownPosition(String macAddress){
		return databaseService.getLastKnownPositionFromDatabase(macAddress);
	}
	
}
