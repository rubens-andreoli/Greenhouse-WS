package br.unip.greenhouse.controller;

import br.unip.greenhouse.model.Actions;
import br.unip.greenhouse.model.Info;
import br.unip.greenhouse.model.Sensors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GreenhouseDAO {
    
    private final String dbAddress;
    private final String dbUser;
    private final String dbKey;
    private final String ghFolder;

    public GreenhouseDAO(String dbAddress, String dbUser, String dbKey, String ghFolder) {
	this.dbAddress = dbAddress;
	this.dbUser = dbUser;
	this.dbKey = dbKey;
	this.ghFolder = ghFolder;
    }

    public int saveActions(Actions a, String ip) throws IOException, SQLException, ClassNotFoundException {
	Class.forName("org.postgresql.Driver");
	int result = -1;
	try (Connection con = DriverManager.getConnection(dbAddress, dbUser, dbKey)){
	    save(new File(ghFolder+Actions.FILENAME), a);
	    String SQL = "INSERT INTO tb_action ("
		    + "is_light,"
		    + "is_water,"
		    + "is_exaust,"
		    + "user_ip) "
		    + "VALUES (?,?,?,?)";
	    PreparedStatement cmd = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
	    cmd.setBoolean(1, a.light);
	    cmd.setBoolean(2, a.water);
	    cmd.setBoolean(3, a.exaust);
	    if (cmd.executeUpdate() > 0) {
		ResultSet rs = cmd.getGeneratedKeys();
		result =  rs.next() ? rs.getInt(1) : -1;
	    }
	}
	return result;
    }
    
    public int saveInfo(Info i) throws SQLException, ClassNotFoundException {
	Class.forName("org.postgresql.Driver");
	int result = -1;
	try (Connection con = DriverManager.getConnection(dbAddress, dbUser, dbKey)){
	    String SQL = "INSERT INTO tb_info ("
		    + "temperature_out,"
		    + "temperature_in,"
		    + "humidity_air_out,"
		    + "humidity_air_in,"
		    + "humidity_soil,"
		    + "soil_ph) "
		    + "VALUES (?,?,?,?,?,?)";
	    PreparedStatement cmd = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
	    cmd.setFloat(1, i.airTemperature);
	    cmd.setFloat(2, i.sensors.airTemperature);
	    cmd.setFloat(3, i.airHumidity);
	    cmd.setFloat(4, i.sensors.airHumidity);
	    cmd.setFloat(5, i.sensors.soilHumidity);
	    cmd.setFloat(6, i.sensors.soilPh);
	    if (cmd.executeUpdate() > 0) {
		ResultSet rs = cmd.getGeneratedKeys();
		result = rs.next() ? rs.getInt(1) : -1;
	    }
	}
	return result;
    }
    
    public List<Info> listInfo() throws SQLException, ClassNotFoundException {
	Class.forName("org.postgresql.Driver");
	List<Info> lista = new ArrayList<>();
	try(Connection con = DriverManager.getConnection(dbAddress, dbUser, dbKey)){
	    String SQL = "SELECT * FROM tb_info ORDER BY time";
	    PreparedStatement cmd = con.prepareStatement(SQL);
	    ResultSet rs = cmd.executeQuery();
	    while (rs.next()) {
		lista.add(new Info(
			new Sensors(
				rs.getFloat("temperature_in"), 
				rs.getFloat("humidity_air_in"), 
				rs.getFloat("humidity_soil"), 
				rs.getFloat("soil_ph")
			),
			rs.getFloat("temperature_out"),
			rs.getFloat("humidity_air_out")
		    )
		);
	    }
	}
	return lista;
    }
    
    public Actions getActions() throws IOException{
	return read(new File(ghFolder+Actions.FILENAME), Actions.class);
    }
    
    public Sensors getSensors() throws IOException{
	return read(new File(ghFolder+Sensors.FILENAME), Sensors.class);
    }
    
    private <T extends Object> T read(File file, Class<T> clazz) throws IOException{
	try(FileReader reader = new FileReader(file)){
	    return new Gson().fromJson(reader, clazz);
	}
    }
    
    private void save(File file, Object obj) throws IOException {
	try(FileWriter writer = new FileWriter(file)){
	    Gson gson = new GsonBuilder()
		    .serializeNulls()
		    .setPrettyPrinting()
		    .create();
	    gson.toJson(obj, writer);
	}
    }

    //<editor-fold defaultstate="collapsed" desc="Tests">
//    public static void main(String[] args) {
//	try {
//	    ClimatempoAPI api = new ClimatempoAPI(
//		    "http://apiadvisor.climatempo.com.br/api/v1/", 
//		    "8060c34bca8c93bab6ca23ac9a2e7da2"
//	    );
//	    GreenhouseDAO dao = new GreenhouseDAO(
//		    "jdbc:postgresql://127.0.0.1:5432/greenhouse",
//		    "postgres",
//		    "postgres123",
//		    "D:/Cloud/Unip/APS/WebService/greenhouseiot-repo/"
//	    );
//	    dao.connect();
//	    System.out.println(dao.saveActions(new Actions(true, false, false), "192.168.0.1"));
//	    Weather w = api.getWeather("3618");
//	    System.out.println(dao.saveInfo(new Info(dao.getSensors(), w.data.temperature, w.data.humidity)));
//	    System.out.println(dao.getActions());
//	    System.out.println(dao.getSensors());
//	    for(Info i : dao.listInfo()){
//		System.out.println(i);
//	    }
//	} catch (ClassNotFoundException | SQLException | IOException ex) {
//	    ex.printStackTrace();
//	}
//    }
    //</editor-fold>
}
