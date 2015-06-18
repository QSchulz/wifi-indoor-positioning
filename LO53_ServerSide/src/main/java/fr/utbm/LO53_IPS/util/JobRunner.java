package fr.utbm.LO53_IPS.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.base.Joiner;

import fr.utbm.LO53_IPS.models.AccessPoint;
import fr.utbm.LO53_IPS.models.BarRSSIHistogram;
import fr.utbm.LO53_IPS.models.Device;
import fr.utbm.LO53_IPS.models.FingerprintMap;
import fr.utbm.LO53_IPS.models.Position;
import fr.utbm.LO53_IPS.models.RSSIHistogram;
import fr.utbm.LO53_IPS.services.DatabaseService;
import fr.utbm.LO53_IPS.services.JSONService;
import fr.utbm.LO53_IPS.services.PositionningService;

public class JobRunner extends TimerTask{

	private final String USER_AGENT = "IPS_Server";
	private DatabaseService databaseService;
	private PositionningService positionningService;
	
	public JobRunner(){
		databaseService = new DatabaseService();
		positionningService = new PositionningService();
	}
	
	@Override
	public void run() {
		// Build devices list : all the devices that we will request
		List<Device> devices = databaseService.getDevicesWithPositions();
		List<AccessPoint> accessPointList = databaseService.getAccessPoints();
		
		Map<Device, List<RSSIHistogram> > mapRSSIHistogram = new HashMap<Device, List<RSSIHistogram>>();
		try {
			mapRSSIHistogram = getHistogramSamples(devices, accessPointList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Compute location with the requested RSSI
		Iterator it = mapRSSIHistogram.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        Device device = (Device) pair.getKey();
	        
	        // compute device location
	        List<RSSIHistogram> RSSIList = (List<RSSIHistogram>)pair.getValue();
	        if(RSSIList != null && RSSIList.size()>0){
	        	Position newPosition = positionningService.computePosition(RSSIList, (Device)pair.getKey());	        
	        	 // Store the location into the database
		        databaseService.saveNewPosition(device, newPosition);
	        }
	    }
	}
	
	
	
	private Map<Device, List<RSSIHistogram>> getHistogramSamples(List<Device> devices, List<AccessPoint> accessPointList) {
		
		List<String> macAddresses = new ArrayList();
		for(Device d:devices){
			macAddresses.add(d.getMACAddress());
		}
		
		String ListMACAddressString = Joiner.on(",").join(macAddresses);
		Map<Device, List<RSSIHistogram>> mapRSSIHistogram = new HashMap<Device, List<RSSIHistogram>>();
		
		for(Device d : devices){
			mapRSSIHistogram.put(d, new ArrayList<RSSIHistogram>());
		}
		
		for(AccessPoint ap : accessPointList){
			
			List<RSSIHistogram> histogramSample = new ArrayList<RSSIHistogram>();
			
			try {
				histogramSample = sendGetRSSI(ListMACAddressString, ap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// store for each device the corresponding RSSIHistogram
			for(RSSIHistogram histogram : histogramSample){
				mapRSSIHistogram.get(histogram.getDevice().getMACAddress()).add(histogram);
			}
			
		}
		
		return mapRSSIHistogram;
	}

	public List<RSSIHistogram> sendGetRSSI(String ListMACAddressString, AccessPoint accessPoint) throws Exception{
		// TODO : replace with the true AP addresses
		String url = "http://"+accessPoint.getIpAddress()+"/?mac_addrs=" + ListMACAddressString;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
		
		return getRSSIHistogramFromString(response.toString());
	}
	
	public List<RSSIHistogram> getRSSIHistogramFromString(String histogramString){
		
		List<RSSIHistogram> histogramSample = new ArrayList<RSSIHistogram>();
		
		JSONObject jObj = new JSONObject(histogramString);
		
		JSONArray histogramArray = jObj.getJSONArray("results");
		
		for(int i=0;i<histogramArray.length();++i){
			
			RSSIHistogram model = new RSSIHistogram();
			
			
			JSONObject histogramObject = histogramArray.getJSONObject(i);
			String deviceMACAddress = histogramObject.getString("mac");
			JSONArray RSSIArray = histogramObject.getJSONArray("histogram");
			Device deviceModel = new Device(deviceMACAddress, "");
			List<BarRSSIHistogram> barListModel = new ArrayList<BarRSSIHistogram>();
			
			for(int j = 0; j < RSSIArray.length(); ++j){
				JSONObject BarRSSIObject = RSSIArray.getJSONObject(j);
				double RSSIValue = BarRSSIObject.getDouble("rssi");
				int occurences = BarRSSIObject.getInt("occurences");
				
				barListModel.add(new BarRSSIHistogram(RSSIValue, occurences, model));
			}
			
			int totalOccurences = histogramObject.getInt("total_occurences");
			
			model.setDevice(deviceModel);
			model.setValue(barListModel);
			// TODO : create a totalOccurences property on the BarRSSIHistogram object
			
			histogramSample.add(model);
		}
		
		return histogramSample;
	}

}
