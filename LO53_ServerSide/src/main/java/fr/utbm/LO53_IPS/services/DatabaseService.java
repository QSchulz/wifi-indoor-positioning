package fr.utbm.LO53_IPS.services;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import fr.utbm.LO53_IPS.models.Coordinate;
import fr.utbm.LO53_IPS.models.Device;
import fr.utbm.LO53_IPS.models.NewDeviceModel;
import fr.utbm.LO53_IPS.util.HibernateUtil;
import fr.utbm.LO53_IPS.models.Position;

public class DatabaseService {

	/* Just a function to work with mock data */
	public void seedDeviceAndPositions() {
		System.out.println("Maven + Hibernate + MySQL");
		Session session = HibernateUtil.createSessionFactory().openSession();

		session.beginTransaction();

		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.set(2015, 05, 12, 13, 35);
		calendar2.set(2015, 05, 12, 13, 36);
		Position position1 = new Position(new Coordinate(1, 1), new Timestamp(
				calendar1.getTimeInMillis()));
		Position position2 = new Position(new Coordinate(2, 2), new Timestamp(
				calendar2.getTimeInMillis()));
		;

		Set<Position> positions = new HashSet();
		positions.add(position1);
		positions.add(position2);

		Device device = new Device("A1:B2:C3:D4:E5:F6", "TestDevice", positions);

		position1.setDevice(device);
		position2.setDevice(device);

		session.save(device);
		session.save(position1);
		session.save(position2);
		session.getTransaction().commit();

		HibernateUtil.shutdown();
	}

	public Set<Position> getAllPositionsFromDatabase(String macAddress) {

		Session session = HibernateUtil.createSessionFactory().openSession();

		String queryString =  "select position" 
							+ " from Position position join position.device device"
							+ " where device.MACAddress = '" + macAddress + "'"
							+ " order by position.Timestamp";
		
		Query query = session.createQuery(queryString);
		List list = query.list();
		Iterator it = list.iterator();
		
		Set<Position> positions = new HashSet();
		
		while (it.hasNext()) {
			Position position = (Position) it.next();
			positions.add(position);
			System.out.println(position);
		}

		HibernateUtil.shutdown();
		
		return positions;
	}

	public Position getLastKnownPositionFromDatabase(String macAddress) {
		return null;
	}
	
	public void saveUserIfNotExists(NewDeviceModel model){
		Session session = HibernateUtil.createSessionFactory().openSession();
		String queryString =  "select count(*)" 
							+ " from Device as device"
							+ " where device.MACAddress = '" + model.getDeviceMACAddress() + "'";
		Query query = session.createQuery(queryString);
		Long count = (Long) query.uniqueResult();
		if(count == 0){ // user doesn't exist, we save it to the db
			saveUser(model);
		}
		HibernateUtil.shutdown();
	}
	
	private void saveUser(NewDeviceModel model){
		Session session = HibernateUtil.createSessionFactory().openSession();
		session.beginTransaction();
		Device device = new Device(model.getDeviceMACAddress(), "");
		session.save(device);
		session.getTransaction().commit();
		HibernateUtil.shutdown();
	}

}
