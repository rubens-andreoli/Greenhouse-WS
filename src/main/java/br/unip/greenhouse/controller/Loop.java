package br.unip.greenhouse.controller;

import br.unip.greenhouse.model.Info;
import br.unip.greenhouse.model.Weather;
import java.io.IOException;
import java.sql.SQLException;

public class Loop {
    
    private final Access a;
    private final Thread t;
    private boolean running;

    public Loop() {
	a = new Access();
	t = new Thread(new Runnable(){
	    @Override
	    public void run() {
		loop();
	    }
	});
    }

    private void loop(){
	while(running){   
	    System.out.println("---------------------SERVICE LOOP---------------------");
	    System.out.println(t.getName());
	    try {
		Weather w = a.api.getWeather(a.p.getProperty("greenhouse_city"));
		Info i = new Info(a.dao.getSensors(), w.data.temperature, w.data.humidity);
		a.dao.saveInfo(i);
	    } catch (IOException | SQLException | ClassNotFoundException ex) {
		System.out.println("LOOP ERROR");
	    }

	    try {
		Thread.sleep(30000);
	    } catch (InterruptedException ex) {
		System.out.println("SLEEP ERROR");
	    }
	}
    }
    
    public void start(){
	if(running) return;
	running = true;
	t.start();
	System.out.println("started");
    }
    
    public void stop(){
	running = false;
    }


}
