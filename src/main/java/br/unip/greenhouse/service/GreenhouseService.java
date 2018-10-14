package br.unip.greenhouse.service;

import br.unip.greenhouse.controller.Access;
import br.unip.greenhouse.model.Actions;
import br.unip.greenhouse.model.Info;
import br.unip.greenhouse.model.Sensors;
import br.unip.greenhouse.model.Weather;
import com.google.gson.Gson;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/greenhouse")
public class GreenhouseService {
    
    public GreenhouseService(){
	System.out.println("---------------------SERVICE RUN---------------------");
    }

    @Path("/get/info")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfo(){
	Access access = new Access();
	try {
	    Weather weather = access.api.getWeather(access.p.getProperty("greenhouse_city"));
	    Sensors sensors = access.dao.getSensors();
	    Info info = new Info(sensors, weather.data.temperature, weather.data.humidity);
	    return new Gson().toJson(info);
	} catch (IOException ex) {
	    return ex.getMessage();
	}
    }
    
    @Path("/get/actions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getActions(){
	Access access = new Access();
	try {
	    Actions actions = access.dao.getActions();
	    return new Gson().toJson(actions);
	} catch (IOException ex) {
	    return ex.getMessage();
	}
    }
    
    @Path("/list/info")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listInfo(){
	Access access = new Access();
	try {
	    List<Info> info = access.dao.listInfo();
	    return new Gson().toJson(info);
	} catch (ClassNotFoundException | SQLException ex) {
	    return ex.getMessage();
	}
    }

    @Path("/set/actions")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean setActions(String actions){ //TODO: IP
        Access access = new Access();
	Actions a = new Gson().fromJson(actions, Actions.class);  
	try {
	    access.dao.saveActions(a, "");
	    return true;
	} catch (IOException | SQLException | ClassNotFoundException ex) {
	    return false;
	}
    }
    
//    @Path("/set/actions")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public String setActions(@QueryParam("light") boolean light, 
//	    @QueryParam("water") boolean water, 
//	    @QueryParam("exaust") boolean exaust,
//	    @QueryParam("ip") String ip){
//	try {
//	    dao.connect();
//	    Actions actions = new Actions(light, water, exaust);
//	    dao.saveActions(actions, ip);
//	    dao.disconnect();
//	    return new Gson().toJson(actions);
//	} catch (ClassNotFoundException | SQLException | IOException ex) {
//	    return ex.getMessage();
//	}
//    }
    
}
