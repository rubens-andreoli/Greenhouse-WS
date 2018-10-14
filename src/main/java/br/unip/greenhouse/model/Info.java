package br.unip.greenhouse.model;

public class Info {
    
    public final Sensors sensors;
    public final float airTemperature;
    public final float airHumidity;

    public Info(Sensors sensors, float airTemperature, float airHumidity) {
	this.sensors = sensors;
	this.airTemperature = airTemperature;
	this.airHumidity = airHumidity;
    }
    
   @Override
    public String toString() {
	return "Info{" + sensors.toString() + 
		", airTemperature=" + airTemperature + 
		", airHumidity=" + airHumidity + 
		'}';
    }
    
}
