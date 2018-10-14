package br.unip.greenhouse.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    
    private static final File CONFIG_FILE = new File("config.properties"); //file in GF domain
    
    private final Properties p;

    public Configuration() {
	p = Configuration.readConfig();
    }
    
    private static Properties readConfig(){
	Properties p = new Properties();
	try(FileInputStream in = new FileInputStream(CONFIG_FILE)){
//	    System.out.println(CONFIG_FILE.getAbsolutePath());
	    p.load(in);
	    return p;
	} catch (IOException ex) {
	    p.setProperty("db_address", "jdbc:postgresql://127.0.0.1:5432/greenhouse");
	    p.setProperty("db_user", "postgres");
	    p.setProperty("db_key", "postgres123");
	    p.setProperty("greenhouse_city", "3618");
	    p.setProperty("greenhouse_folder", "D:/Cloud/Unip/APS/WebService/greenhouseiot-repo/");
	    p.setProperty("climatempo_address", "http://apiadvisor.climatempo.com.br/api/v1/");
	    p.setProperty("climatempo_key", "8060c34bca8c93bab6ca23ac9a2e7da2");
	    saveConfig(p);
	    return p;
	}
    }
    
    private static void saveConfig(Properties p){
	try(FileOutputStream out = new FileOutputStream(CONFIG_FILE)){
	    p.store(out, null);
	} catch (IOException ex) {}
    }
    
    public String getDBAddress(){return p.getProperty("db_address");}
    public String getDBUser(){return p.getProperty("db_user");}
    public String getDBKey(){return p.getProperty("db_key");}
    public String getGHCity(){return p.getProperty("greenhouse_city");}
    public String getGHFolder(){return p.getProperty("greenhouse_folder");}
    public String getAPIAddress(){return p.getProperty("climatempo_address");}
    public String getAPIKey(){return p.getProperty("climatempo_key");}
    
}
