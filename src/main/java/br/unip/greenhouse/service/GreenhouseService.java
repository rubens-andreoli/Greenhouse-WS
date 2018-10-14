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
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

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

    @Resource
    private WebServiceContext context;
    
    @Path("/set/actions")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean setActions(String actions){
        Access access = new Access();
	Actions a = new Gson().fromJson(actions, Actions.class);
	MessageContext messageContext = context.getMessageContext();
	HttpServletRequest request = (HttpServletRequest) messageContext.get(MessageContext.SERVLET_REQUEST); 
	try {
	    access.dao.saveActions(a, request.getRemoteAddr());
	    return true;
	} catch (IOException | SQLException | ClassNotFoundException ex) {
	    return false;
	}
    }
    
}
