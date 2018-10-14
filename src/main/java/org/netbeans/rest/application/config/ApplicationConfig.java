/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import br.unip.greenhouse.controller.Loop;
import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Amanda
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    private static Loop loop;

    public ApplicationConfig() {
    	System.out.println("---------------------SERVICE STARTED---------------------");
	if(loop != null) loop.stop();
	loop = new Loop();
	loop.start();
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(br.unip.greenhouse.service.GreenhouseService.class);
    }
    
}
