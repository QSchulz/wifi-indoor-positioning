package fr.utbm.LO53_IPS.services;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import fr.utbm.LO53_IPS.models.AccessPoint;
import fr.utbm.LO53_IPS.models.BarRSSIHistogram;
import fr.utbm.LO53_IPS.models.Coordinate;
import fr.utbm.LO53_IPS.models.Device;
import fr.utbm.LO53_IPS.models.FingerprintMap;
import fr.utbm.LO53_IPS.models.HistogramFingerprint;
import fr.utbm.LO53_IPS.models.NewDeviceModel;
import fr.utbm.LO53_IPS.models.Position;
import fr.utbm.LO53_IPS.models.RSSIHistogram;
import fr.utbm.LO53_IPS.util.HibernateUtil;

public class DatabaseService {

	/* some functions to work with mock data and test mappings */
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

		List<Position> positions = new ArrayList();
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

	public void seedFingerprint(){
		System.out.println("Maven + Hibernate + MySQL");
		Session session = HibernateUtil.createSessionFactory().openSession();
		session.beginTransaction();

		AccessPoint accessPoint = new AccessPoint("A1:B2:C3:D4:F6:E5", new Coordinate(3, 4));
		
		
		HistogramFingerprint fingerprint = new HistogramFingerprint(new Coordinate(2, 2));
		RSSIHistogram RSSIHistogram = new RSSIHistogram(accessPoint, fingerprint);
		fingerprint.getHistogramSamples().add(RSSIHistogram);
		
		BarRSSIHistogram bar1 = new BarRSSIHistogram(40.3, 4, RSSIHistogram);
		BarRSSIHistogram bar2 = new BarRSSIHistogram(35, 2, RSSIHistogram);
		BarRSSIHistogram bar3 = new BarRSSIHistogram(45.5, 3, RSSIHistogram);
		List<BarRSSIHistogram> barList = new ArrayList<BarRSSIHistogram>();
		barList.add(bar1);
		barList.add(bar2);
		barList.add(bar3);
		
		RSSIHistogram.setValue(barList);
		
		session.save(accessPoint);
		session.save(fingerprint);
		session.getTransaction().commit();

		HibernateUtil.shutdown();
	}
	
	public List<Position> getAllPositionsFromDatabase(String macAddress) {

		Session session = HibernateUtil.createSessionFactory().openSession();

		String queryString =  "select position" 
							+ " from Position position join position.device device"
							+ " where device.MACAddress = '" + macAddress + "'"
							+ " order by position.timestamp asc";
		
		Query query = session.createQuery(queryString);
		List<Position> list = query.list();
		Iterator<Position> it = list.iterator();
		
		while (it.hasNext()) {
			Position position = (Position) it.next();
			System.out.println(position);
		}

		HibernateUtil.shutdown();
		
		return list;
	}

	public Position getLastKnownPositionFromDatabase(String macAddress) {
		Session session = HibernateUtil.createSessionFactory().openSession();

		String queryString =  "select position" 
							+ " from Position position join position.device device"
							+ " where device.MACAddress = '" + macAddress + "'"
							+ " order by position.timestamp desc";
		
		Query query = session.createQuery(queryString);
		List<Position> list = query.list();

		HibernateUtil.shutdown();
		
		return list.get(0);
	}
	
	public void saveUserIfNotExists(String macAddress){
		Session session = HibernateUtil.createSessionFactory().openSession();
		String queryString =  "select count(*)" 
							+ " from Device as device"
							+ " where device.MACAddress = '" + macAddress + "'";
		Query query = session.createQuery(queryString);
		Long count = (Long) query.uniqueResult();
		if(count == 0){ // user doesn't exist, we save it to the db
			saveUser(new NewDeviceModel(macAddress));
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

	public List<Device> getDevicesWithPositions(){
		
		Session session = HibernateUtil.createSessionFactory().openSession();

		String queryString =  "from Device device";
		
		Query query = session.createQuery(queryString);
		List<Device> list = query.list();
		Iterator<Device> it = list.iterator();
		
		List<Device> devices = new ArrayList<Device>();
		
		while (it.hasNext()) {
			Device device = it.next();
			devices.add(device);
		}

		HibernateUtil.shutdown();
		
		return devices;
		
	}


	public FingerprintMap getMap() {
		
		Session session = HibernateUtil.createSessionFactory().openSession();

		String queryString =  "from HistogramFingerprint fingerprint";
		
		FingerprintMap map = new FingerprintMap();
		
		Query query = session.createQuery(queryString);
		List<HistogramFingerprint> list = query.list();
		Iterator<HistogramFingerprint> it = list.iterator();
		
		List<HistogramFingerprint> fingerprints = new ArrayList<HistogramFingerprint>();
		
		while (it.hasNext()) {
			HistogramFingerprint fingerprint = it.next();
			fingerprints.add(fingerprint);
		}

		map.setMap(fingerprints);
		
		HibernateUtil.shutdown();
		
		return map;
	}
	public List<AccessPoint> getAccessPoints() {
		Session session = HibernateUtil.createSessionFactory().openSession();

		String queryString =  "from AccessPoint ap"
							+ " where ap.ipAddress <> ''";
		
		Query query = session.createQuery(queryString);
		List<AccessPoint> list = query.list();
		Iterator<AccessPoint> it = list.iterator();
		
		List<AccessPoint> accessPointList = new ArrayList<AccessPoint>();
		
		while (it.hasNext()) {
			AccessPoint ap = it.next();
			accessPointList.add(ap);
			System.out.println("Access point extracted: MAC:" + ap.getMACAddress() + " IP:" + ap.getIpAddress());
		}

		HibernateUtil.shutdown();
		
		return accessPointList;
	}

	public List<Position> getAllPositionsFromDatabaseSinceTimestamp(
			String macAddress, Timestamp timestamp) {

		Session session = HibernateUtil.createSessionFactory().openSession();

		String queryString =  "select position" 
							+ " from Position position join position.device device"
							+ " where device.MACAddress = '" + macAddress + "'"
							+ " and position.timestamp >= '"+timestamp+"'"
							+ " order by position.timestamp asc";
		
		Query query = session.createQuery(queryString);
		List<Position> list = query.list();
		Iterator<Position> it = list.iterator();
		
		while (it.hasNext()) {
			Position position = (Position) it.next();
			System.out.println(position);
		}

		HibernateUtil.shutdown();
		
		return list;
	}

	public void saveNewPosition(Device device, Position newPosition) {
		
		Session session = HibernateUtil.createSessionFactory().openSession();
		session.beginTransaction();

		Calendar calendar = Calendar.getInstance();
		newPosition.setTimestamp(new Timestamp(calendar.getTimeInMillis()));
		newPosition.setDevice(device);

		session.save(newPosition);
		session.getTransaction().commit();

		HibernateUtil.shutdown();
		
	}
}
