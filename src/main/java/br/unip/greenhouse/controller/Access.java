package br.unip.greenhouse.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Access {
    
    private static final File CONFIG_FILE = new File("config.dat");
    
    public final Properties p;
    public final ClimatempoAPI api;
    public final GreenhouseDAO dao;
    
    public Access(){
	p = Access.readConfig();
	api = new ClimatempoAPI(
		p.getProperty("climatempo_address"), 
		p.getProperty("climatempo_key")
	);
	dao = new GreenhouseDAO(
		p.getProperty("db_address"), 
		p.getProperty("db_user"), 
		p.getProperty("db_key"), 
		p.getProperty("greenhouse_folder")
	);
    }
    
    private static Properties readConfig(){
	Properties p = new Properties();
	try(FileInputStream in = new FileInputStream(CONFIG_FILE)){
	    p.load(in);
	    return p;
	} catch (IOException ex) {
	    p = defaultConfig();
	    saveConfig(p);
	    return p;
	}
    }
    
    private static void saveConfig(Properties p){
	try(FileOutputStream out = new FileOutputStream(CONFIG_FILE)){
	    p.store(out, null);
	} catch (IOException ex) {}
    }
    
    private static Properties defaultConfig(){
	Properties p = new Properties();
	p.setProperty("db_address", "jdbc:postgresql://127.0.0.1:5432/greenhouse");
	p.setProperty("db_user", "postgres");
	p.setProperty("db_key", "postgres123");
	p.setProperty("greenhouse_city", "3618");
	p.setProperty("greenhouse_folder", "D:/Cloud/Unip/APS/WebService/greenhouseiot-repo/");
	p.setProperty("climatempo_address", "http://apiadvisor.climatempo.com.br/api/v1/");
	p.setProperty("climatempo_key", "8060c34bca8c93bab6ca23ac9a2e7da2");
	return p;
    }
    
}
