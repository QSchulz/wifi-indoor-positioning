package fr.utbm.LO53_IPS.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.TimerTask;

import fr.utbm.LO53_IPS.services.DatabaseService;
import fr.utbm.LO53_IPS.services.JSONService;

public class JobRunner extends TimerTask{

	private final String USER_AGENT = "IPS_Server";
	private DatabaseService databaseService;
	private JSONService jsonService;
	
	public JobRunner(){
		databaseService = new DatabaseService();
		jsonService = new JSONService();
	}
	
	@Override
	public void run() {
		// Get the devices RSSI from the Database
		List<String> MACAddresses = databaseService.getDevicesMACAddress();
		// Build JSON RSSI string
		String JSONMACAddresses = jsonService.buildDeviceMACAddressesJSON(MACAddresses);
		System.out.println("Data to send : " + JSONMACAddresses);
		// Request RSSI
		try {
			sendPost();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Compute location with the requested RSSI
		
		// Store the location into the database
		
	}
	
	public void sendPost() throws Exception{
		String url = "http://jsonplaceholder.typicode.com/posts";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 
		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
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
	}

}
