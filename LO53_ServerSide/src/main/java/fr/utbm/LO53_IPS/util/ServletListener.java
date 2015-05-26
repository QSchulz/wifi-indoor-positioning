package fr.utbm.LO53_IPS.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServletListener implements ServletContextListener {

	 private ServletContext context = null;
	
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("Application is undeploying");
		this.context = null;
	}

	public void contextInitialized(ServletContextEvent arg0) {
		this.context = arg0.getServletContext();
		TimerTaskScheduler task = new TimerTaskScheduler();
		task.schedule();
		System.out.println("Application is deploying");
	}

}
