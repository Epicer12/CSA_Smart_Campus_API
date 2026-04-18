/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.epicer12.smart_campus_api.resources;

import io.github.epicer12.smart_campus_api.exceptions.LinkedResourceNotFoundException;
import io.github.epicer12.smart_campus_api.models.ErrorResponse;
import io.github.epicer12.smart_campus_api.models.Sensor;
import io.github.epicer12.smart_campus_api.store.DataStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Hasun
 */

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {
    @POST
    public Response registerSensor(Sensor sensor){
        if (sensor.getId() == null || sensor.getId().isEmpty()) {
            ErrorResponse error = new ErrorResponse(400, "Bad Request", "Sensor ID is required");
            return Response.status(400).entity(error).build();
        }
        
        if (DataStore.sensors.containsKey(sensor.getId())) {
            ErrorResponse error = new ErrorResponse(409, "Conflict", "Sensor with ID " + sensor.getId() + " already exists");
            return Response.status(409).entity(error).build();
        }
        
        if (!DataStore.rooms.containsKey((sensor.getRoomId()))) {
            throw new LinkedResourceNotFoundException("Room with ID " + sensor.getRoomId() + " does not exists");
        }
        
        DataStore.sensors.put(sensor.getId(), sensor);
        
        DataStore.rooms.get(sensor.getRoomId()).getSensorIds().add(sensor.getId()); 
        
        return Response.status(201)
                .entity(sensor)
                .header("Location", "/api/v1/sensors/" + sensor.getId())
                .build();
    }
    
    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Map<String, Object>> summaryList = new ArrayList<>();
        
        for (Sensor sensor : DataStore.sensors.values()) {
            if (type != null && !sensor.getType().equalsIgnoreCase(type)) {
                continue;
            }
            
            Map<String, Object> summary = new HashMap<>();
            summary.put("id", sensor.getId());
            summary.put("type", sensor.getType());
            summary.put("status", sensor.getStatus());
            summary.put("href", "/api/v1/sensors/" + sensor.getId());
            
            summaryList.add(summary);
        }
        
        return Response.ok(summaryList).build();
    }
    
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        if (!DataStore.sensors.containsKey(sensorId)) {
            throw new LinkedResourceNotFoundException("Sensor with ID " + sensorId + " does not exist");
        }
        
        return new SensorReadingResource(sensorId);
    }
    
    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {

        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            ErrorResponse error = new ErrorResponse(404, "Not Found", "Sensor with ID " + sensorId + " was not found");
            return Response.status(404).entity(error).build();
        }

        return Response.ok(sensor).build();
}
}
