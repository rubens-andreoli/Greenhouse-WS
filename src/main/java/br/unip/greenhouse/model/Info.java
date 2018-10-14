package br.unip.greenhouse.model;

import java.sql.Date;

public class Info {
    
    public final Sensors sensors;
    public final float airTemperature;
    public final float airHumidity;
    public final Date date;

    public Info(Sensors sensors, float airTemperature, float airHumidity, Date date) {
	this.sensors = sensors;
	this.airTemperature = airTemperature;
	this.airHumidity = airHumidity;
	if(date == null){
	    this.date = new Date(new java.util.Date().getTime());
	}else{
	    this.date = date;
	}
    }
    
    public Info(Sensors sensors, float airTemperature, float airHumidity){
	this(sensors, airTemperature, airHumidity, null);
    }
    
   @Override
    public String toString() {
	return "Info{" + sensors.toString() + 
		", airTemperature=" + airTemperature + 
		", airHumidity=" + airHumidity +
		", date=" + date +
		'}';
    }
    
}
