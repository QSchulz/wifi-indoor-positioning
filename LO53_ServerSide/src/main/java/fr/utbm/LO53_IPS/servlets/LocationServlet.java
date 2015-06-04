package fr.utbm.LO53_IPS.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.utbm.LO53_IPS.models.Position;
import fr.utbm.LO53_IPS.services.PositionningService;

public class LocationServlet extends HttpServlet {

	private PositionningService positionningService;
	
	public LocationServlet(){
		this.positionningService = new PositionningService();
	}
	
	// This get returns the lastly computed location of a device
	// /getLocation?MACAddress=A1:B2:C3:D4:E5:F6
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		
		String MACAddress = request.getParameter("MACAddress");
		
		Position p = positionningService.getLastKnownPosition(MACAddress);
		
		response.setContentType("application/json");
		response.setCharacterEncoding( "UTF-8" );
		PrintWriter out = response.getWriter();
		out.println(p.toJSON());
    }
	
}
