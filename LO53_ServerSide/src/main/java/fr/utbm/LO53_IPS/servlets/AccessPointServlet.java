package fr.utbm.LO53_IPS.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		
		String macAddress = getNewDeviceMACAddress(request);
		if(!macAddress.isEmpty()){
			dbService.saveUserIfNotExists(macAddress);
		}
    }
	
	private String getNewDeviceMACAddress(HttpServletRequest request){
		NewDeviceModel model = new NewDeviceModel();
		String deviceMACAddress = "";
		try {

			// TODO: take the same name as the apram sent by the AP
			deviceMACAddress = request.getParameter("DeviceMACAddress");
			
			Pattern p = Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
			Matcher m = p.matcher(deviceMACAddress);
			boolean isCorrectFormat = m.matches();
			
			if(isCorrectFormat){
				return deviceMACAddress;
			} else {
				throw new Exception("Setting the device mac addres failed : string not a mac address");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return deviceMACAddress;
	}
	
}
