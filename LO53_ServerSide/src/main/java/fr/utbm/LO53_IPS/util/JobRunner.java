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
import fr.utbm.LO53_IPS.models.RSSIHistogram;
import fr.utbm.LO53_IPS.services.DatabaseService;
import fr.utbm.LO53_IPS.services.JSONService;

public class JobRunner extends TimerTask{

	private final String USER_AGENT = "IPS_Server";
	private DatabaseService databaseService;
	
	public JobRunner(){
		databaseService = new DatabaseService();
	}
	
	@Override
	public void run() {
		List<String> MACAddresses = databaseService.getDevicesMACAddress();
		
		// Build devices list : all the devices that we will request
		List<AccessPoint> accessPointList = databaseService.getAccessPoints();
		
		Map<String, List<RSSIHistogram> > mapRSSIHistogram = new HashMap<String, List<RSSIHistogram>>();
		try {
			mapRSSIHistogram = getHistogramSamples(MACAddresses, accessPointList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Compute location with the requested RSSI
		Iterator it = mapRSSIHistogram.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        // compute device location
	        
	        // compute smoothed path TODO : NO ? we just do that when the user asks for it ?
	        
	        // Store the location into the database
	        
	    }
	}
	
	
	
	private Map<String, List<RSSIHistogram>> getHistogramSamples(List<String> MACAddresses, List<AccessPoint> accessPointList) {
		
		String ListMACAddressString = Joiner.on(",").join(MACAddresses);
		Map<String, List<RSSIHistogram>> mapRSSIHistogram = new HashMap<String, List<RSSIHistogram>>();
		
		for(String deviceMACAddress : MACAddresses){
			mapRSSIHistogram.put(deviceMACAddress, new ArrayList<RSSIHistogram>());
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
