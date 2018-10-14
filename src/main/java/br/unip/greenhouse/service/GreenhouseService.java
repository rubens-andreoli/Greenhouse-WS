package br.unip.greenhouse.service;

import br.unip.greenhouse.controller.ClimatempoAPI;
import br.unip.greenhouse.controller.Configuration;
import br.unip.greenhouse.controller.GreenhouseDAO;
import br.unip.greenhouse.model.Actions;
import br.unip.greenhouse.model.Info;
import br.unip.greenhouse.model.Sensors;
import br.unip.greenhouse.model.Weather;
import com.google.gson.Gson;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
	Configuration conf = new Configuration();
	ClimatempoAPI api = new ClimatempoAPI(conf);
	GreenhouseDAO dao = new GreenhouseDAO(conf);
	try {
	    Weather weather = api.getWeather();
	    Sensors sensors = dao.getSensors();
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
	GreenhouseDAO dao = new GreenhouseDAO(new Configuration());
	try {
	    Actions actions = dao.getActions();
	    return new Gson().toJson(actions);
	} catch (IOException ex) {
	    return ex.getMessage();
	}
    }
    
    @Path("/list/info")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listInfo(){
	GreenhouseDAO dao = new GreenhouseDAO(new Configuration());
	try {
	    List<Info> info = dao.listInfo();
	    return new Gson().toJson(info);
	} catch (ClassNotFoundException | SQLException ex) {
	    return ex.getMessage();
	}
    }
    
    @Path("/list/info/{start}:{end}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listInfo(@PathParam("start") String start, @PathParam("end") String end){
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	GreenhouseDAO dao = new GreenhouseDAO(new Configuration());
	try {
	    Date startDate = (Date)formatter.parse(start);
	    Date endDate = (Date)formatter.parse(end);
	    List<Info> info = dao.listInfo(startDate, endDate);
	    return new Gson().toJson(info);
	} catch (ClassNotFoundException | SQLException | ParseException ex) {
	    return ex.getMessage();
	}
    }

    @Resource
    private WebServiceContext context;
    
    @Path("/set/actions")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean setActions(String actions){
        GreenhouseDAO dao = new GreenhouseDAO(new Configuration());
	Actions a = new Gson().fromJson(actions, Actions.class);
	MessageContext messageContext = context.getMessageContext();
	HttpServletRequest request = (HttpServletRequest) messageContext.get(MessageContext.SERVLET_REQUEST); 
	try {
	    dao.saveActions(a, request.getRemoteAddr());
	    return true;
	} catch (IOException | SQLException | ClassNotFoundException ex) {
	    return false;
	}
    }
    
}
