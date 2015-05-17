package fr.utbm.LO53_IPS.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.utbm.LO53_IPS.models.Position;
import fr.utbm.LO53_IPS.services.PositionningService;
import fr.utbm.LO53_IPS.services.JSONService;

public class AndroidDeviceServlet extends HttpServlet {

	private PositionningService positionningService;
	private JSONService JSONService;
	
	public AndroidDeviceServlet(){
		this.positionningService = new PositionningService();
		this.JSONService = new JSONService();
	}
	
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		
		String MACAddress = request.getParameter("MACAddress");
		Set<Position> positions = positionningService.getAllPositions(MACAddress);
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
