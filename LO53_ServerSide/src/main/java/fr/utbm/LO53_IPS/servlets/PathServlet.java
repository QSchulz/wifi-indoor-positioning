package fr.utbm.LO53_IPS.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.utbm.LO53_IPS.models.Position;
import fr.utbm.LO53_IPS.services.PositionningService;
import fr.utbm.LO53_IPS.services.JSONService;

public class PathServlet extends HttpServlet {

	private PositionningService positionningService;
	private JSONService JSONService;
	
	public PathServlet(){
		this.positionningService = new PositionningService();
		this.JSONService = new JSONService();
	}
	
	// This get returns to the Android device all the path or from a certain timestamp if any
	// /getPath?MACAddress=A1:B2:C3:D4:E5:F6&Timestamp=yyyy-MM-dd_HH:mm:ss.SSS (don't forget the underscore)
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		
		String MACAddress = request.getParameter("MACAddress");
		String timestampString = request.getParameter("Timestamp");
		List<Position> positions = new ArrayList<Position>();
		if(timestampString == null){
			positions = positionningService.getAllPositions(MACAddress);
		} else {
			SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
			Date parsedDate;
			try {
				parsedDate = timestampFormat.parse(timestampString);
				Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
				positions = positionningService.getAllPositionsSinceTimestamp(MACAddress, timestamp);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String JSONPositions = "";
		
		if(positions.size() > 0){
			JSONPositions = JSONService.buildPositionListJSON(positions);
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding( "UTF-8" );
		PrintWriter out = response.getWriter();
		out.println(JSONPositions);
    }
	
}
