package br.unip.greenhouse.controller;

import br.unip.greenhouse.model.Weather;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class ClimatempoAPI {
    
    //<editor-fold defaultstate="collapsed" desc="City">
    private class City {
	public final int id;
	public final String name;
	public final String state;
	public final String country;

	public City(int id, String name, String state, String country) {
	    this.id = id;
	    this.name = name;
	    this.state = state;
	    this.country = country;
	}
    }
    //</editor-fold>
    
    private final String url;
    private final String key;
    private final String code;
   
    public ClimatempoAPI(String url, String key, int code){
	this.url = url;
	this.key = key;
	this.code = String.valueOf(code);
    }
    
    public ClimatempoAPI(String url, String key, String city){
	this.url = url;
	this.key = key;
	this.code = String.valueOf(getCode(city));
    }

    public ClimatempoAPI(Configuration c){
	url = c.getAPIAddress();
	key = c.getAPIKey();
	code = c.getGHCity();
    }
    
    public Weather getWeather() throws IOException {
        String json = get(url+"weather/locale/"+code+"/current?token="+key);
	return new Gson().fromJson(json, Weather.class);
    }
    
    private int getCode(String city){
	city = city.replace(" ", "%20");
	int code = 0;
	try {
	    String json = get(url+"locale/city?name="+city+"&token="+key);
	    ArrayList<City> cities = new Gson().fromJson(json,
		    new TypeToken<ArrayList<City>>(){}.getType()
	    );
	    if(!cities.isEmpty()) code = cities.get(0).id;
	} catch (IOException ex) {}
	return code;
    }

    private String get(String operation) throws IOException {
        HttpClient cli = HttpClientBuilder.create().build();
	HttpGet requisicao = new HttpGet(operation);
	requisicao.addHeader("Accept", "application/json");
	HttpResponse resposta = cli.execute(requisicao);
	return EntityUtils.toString(resposta.getEntity());
    }    
    
    //<editor-fold defaultstate="collapsed" desc="Tests">
//    public static void main(String[] args) {   
//	try {
//	    ClimatempoAPI api = new ClimatempoAPI(
//		    "http://apiadvisor.climatempo.com.br/api/v1/", 
//		    "8060c34bca8c93bab6ca23ac9a2e7da2",
//		    3618
//	    );
//	    
//	    int cityCode = api.getCode("Ribeirão Preto");
//	    System.out.println(cityCode+"\n");
//	    
//	    Weather weather = api.getWeather();
//	    
//	    System.out.println(weather.name);
//	    System.out.println(weather.data.humidity);
//	    System.out.println(weather.data.temperature);
//	} catch (IOException ex) {
//	    ex.printStackTrace();
//	}
//    }
    //</editor-fold>
    
}
