package fr.utbm.LO53_IPS.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.base.Joiner;

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
		// Get the devices RSSI from the Database
		List<String> MACAddresses = databaseService.getDevicesMACAddress();
		// Build RSSI string, format : A1:B2:C3:D4:E5:F6,A2:B3:C4:D5:E6:F7... etc. (coma separated)
		String ListMACAddressString = Joiner.on(",").join(MACAddresses);
		System.out.println("Data to send : " + ListMACAddressString);
		
		// Request RSSI
		try {
			// TODO : send it for all the APs
			sendGetRSSI(ListMACAddressString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Compute location with the requested RSSI
		
		// Store the location into the database
		
	}
	
	public List<RSSIHistogram> sendGetRSSI(String ListMACAddressString) throws Exception{
		// TODO : replace with the true AP addresses
		String url = "http://localhost:8080/LO53_IPS/getRSSI?=MACAddresses=" + ListMACAddressString;
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
