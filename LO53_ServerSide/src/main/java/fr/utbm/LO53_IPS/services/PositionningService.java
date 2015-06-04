package fr.utbm.LO53_IPS.services;

import java.sql.Timestamp;
import java.util.List;
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
	
	public List<Position> getAllPositions(String macAddress){
		return databaseService.getAllPositionsFromDatabase(macAddress);
	}
	
	public Position getLastKnownPosition(String macAddress){
		return databaseService.getLastKnownPositionFromDatabase(macAddress);
	}

	public List<Position> getAllPositionsSinceTimestamp(String macAddress,
			Timestamp timestamp) {

		return databaseService.getAllPositionsFromDatabaseSinceTimestamp(macAddress, timestamp);
	}
	
}
