/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.epicer12.smart_campus_api.resources;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Hasun
 */

@Path("/")
public class DiscoveryResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover() {
        Map<String, Object> response = new HashMap<>();
        
        // API version info
        response.put("version", "1.0");
        response.put("name", "Smart Campus API");
        response.put("author", "Hasun Tisera");
        response.put("support", "uddika.20240397@iit.ac.lk");
        
        // Available routes
        Map<String, String> routes = new HashMap<>();
        routes.put("discovery", "/api/v1/");
        routes.put("rooms", "/api/v1/rooms");
        routes.put("room_detail", "/api/v1/rooms/{roomId}");
        routes.put("sensors", "/api/v1/sensors");
        routes.put("sensor_detail", "/api/v1/sensors/{sensorId}");
        routes.put("sensor_filter", "/api/v1/sensors?type={type}");
        routes.put("sensor_readings", "/api/v1/sensors/{sensorId}/readings");
        
        response.put("links", routes);
        
        return Response.ok(response).build();
    }
}
