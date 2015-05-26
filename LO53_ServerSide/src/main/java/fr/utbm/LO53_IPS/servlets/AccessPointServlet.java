package fr.utbm.LO53_IPS.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import fr.utbm.LO53_IPS.models.NewDeviceModel;
import fr.utbm.LO53_IPS.services.DatabaseService;

public class AccessPointServlet extends HttpServlet {

	private DatabaseService dbService;
	
	public AccessPointServlet(){
		dbService = new DatabaseService();
	}
	
	/* This post functions is so that the AP can post the MAC address of a user he newly detected.
	 * The data must be a json looking like this: {"DeviceMACAddress":"A1:B2:C3:D4:E5:F6"}
	 * TODO : implement it so that we can 
	 */
	//saveDevice
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		
		NewDeviceModel model = getNewDeviceModel(request);
		if(model.getDeviceMACAddress() != null){
			dbService.saveUserIfNotExists(model);
		}
    }
	
	private NewDeviceModel getNewDeviceModel(HttpServletRequest request){
		NewDeviceModel model = new NewDeviceModel();
		try {
			StringBuilder sb = new StringBuilder();
		    BufferedReader br;
			
				br = request.getReader();
			
				
		    String str;
		    while( (str = br.readLine()) != null ){
		        sb.append(str);
		    }   
			
			JSONObject jObj = new JSONObject(sb.toString());
			Iterator<String> it = jObj.keys();
			
			while(it.hasNext())
			{
			    String key = it.next();
			    
			    if(key.equals("DeviceMACAddress")){
			    	model.setDeviceMACAddress((String) jObj.get(key));
			    }
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return model;
	}
	
}
