package com.web.startup;

import javax.servlet.*;
import com.web.scheduler.MainScheduler;

public class ServerStartup implements ServletContextListener {

	MainScheduler scheduler = new MainScheduler();

	public void contextInitialized(ServletContextEvent event) {
		scheduler.startScheduler();
	}
	public void contextDestroyed(ServletContextEvent event) {
		scheduler.stopScheduler();
	}
}