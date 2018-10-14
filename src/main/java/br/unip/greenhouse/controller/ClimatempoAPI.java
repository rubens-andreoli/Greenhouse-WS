package br.unip.greenhouse.controller;

import br.unip.greenhouse.model.City;
import br.unip.greenhouse.model.Weather;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class ClimatempoAPI {
    
    private final String url;
    private final String key;
   
    public ClimatempoAPI(String url, String key){
	this.url = url;
	this.key = key;
    }
    
    public List<City> getCity(String city) throws IOException {
        city = city.replace(" ", "%20");
	String json = get(url+"locale/city?name="+city+"&token="+key);
	ArrayList<City> cities = new Gson().fromJson(json,
                new TypeToken<ArrayList<City>>(){}.getType()
        );
	return cities;
    }
    public Weather getWeather(String cityCode) throws IOException {
        String json = get(url+"weather/locale/"+cityCode+"/current?token="+key);
	return new Gson().fromJson(json, Weather.class);
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
//		    "8060c34bca8c93bab6ca23ac9a2e7da2");
//	    
//	    List<City> cidades = api.getCity("Ribeirão Preto");
//	    
//	    if(cidades.size()==0)
//		System.out.println("NULL ARRAY");
//	    
//	    for(City c : cidades){
//		System.out.println("Id: " + c.id);
//	    }
//	    
//	    Weather weather = api.getWeather("3618");
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
