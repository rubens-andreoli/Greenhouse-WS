package br.unip.greenhouse.model;

public class Weather {
 
    //<editor-fold defaultstate="collapsed" desc="Data">
    public class Data {
	public final float temperature;
	public final String wind_direction;
	public final float wind_velocity;
	public final float humidity;
	public final String condition;
	public final String pressure;
	public final String icon;
	public final String sensation;
	public final String date;

	public Data(float temperature, String wind_direction, float wind_velocity, 
		float humidity, String condition, String pressure, 
		String icon, String sensation, String date) {
	    this.temperature = temperature;
	    this.wind_direction = wind_direction;
	    this.wind_velocity = wind_velocity;
	    this.humidity = humidity;
	    this.condition = condition;
	    this.pressure = pressure;
	    this.icon = icon;
	    this.sensation = sensation;
	    this.date = date;
	}
    }
    //</editor-fold>
    
    public final int id;
    public final String name;
    public final String state;
    public final String country;
    public final Data data;

    public Weather(int id, String name, String state, String country, Data data) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.country = country;
        this.data = data;
    }
    
}
