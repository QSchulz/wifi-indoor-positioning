import java.util.Set;

import org.hibernate.Session;

import fr.utbm.LO53_IPS.models.Device;
import fr.utbm.LO53_IPS.models.Position;
import fr.utbm.LO53_IPS.services.DatabaseService;
import fr.utbm.LO53_IPS.util.HibernateUtil;



public class App {
	public static void main( String[] args )
    {
		DatabaseService dbService = new DatabaseService();
		//dbService.seedDeviceAndPositions();
		//dbService.seedFingerprint();
		dbService.getMap();
		//Set<Position> positions = dbService.getAllPositionsFromDatabase("A1:B2:C3:D4:E5:F6");
		//System.out.println(positions);
    }
}
