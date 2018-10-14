package br.unip.greenhouse.application;

import br.unip.greenhouse.controller.ClimatempoAPI;
import br.unip.greenhouse.controller.Configuration;
import br.unip.greenhouse.controller.GreenhouseDAO;
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
		Configuration conf = new Configuration();
		ClimatempoAPI api = new ClimatempoAPI(conf);
		GreenhouseDAO dao = new GreenhouseDAO(conf);
		try {
		    Weather weather = api.getWeather();
		    Sensors sensors = dao.getSensors();
		    Info info = new Info(sensors, weather.data.temperature, weather.data.humidity);
		    dao.saveInfo(info);
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
