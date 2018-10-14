package org.netbeans.rest.applicationConfig;

import br.unip.greenhouse.controller.Access;
import br.unip.greenhouse.model.Info;
import br.unip.greenhouse.model.Sensors;
import br.unip.greenhouse.model.Weather;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationListener implements ServletContextListener {

    private static final TimeUnit TIME = TimeUnit.MINUTES;
    private static final int DELAY = 1;
    private static final int INTERVAL = 30;
    
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> logger;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
	System.out.println("---------------------SERVICE STARTED---------------------");
	scheduler = Executors.newScheduledThreadPool(1);
	Runnable command = new Runnable(){
	    @Override
	    public void run() {
		System.out.println("---------------------SERVICE LOOP "
			+ "["+Thread.currentThread().getName()+"] "
			+ "---------------------");
		Access access = new Access();
		try {
		    Weather weather = access.api.getWeather(access.p.getProperty("greenhouse_city"));
		    Sensors sensors = access.dao.getSensors();
		    Info info = new Info(sensors, weather.data.temperature, weather.data.humidity);
		    access.dao.saveInfo(info);
		} catch (IOException | SQLException | ClassNotFoundException ex) {
		    System.out.println("LOOP ERROR");
		}
	    }
	};
	logger = scheduler.scheduleAtFixedRate(command, DELAY, INTERVAL, TIME);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
	System.out.println("---------------------SERVICE ENDED---------------------");
	logger.cancel(true);
    }
    
}
