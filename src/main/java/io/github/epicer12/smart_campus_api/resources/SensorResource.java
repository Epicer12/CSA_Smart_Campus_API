package io.github.epicer12.smart_campus_api.resources;

import io.github.epicer12.smart_campus_api.exceptions.LinkedResourceNotFoundException;
import io.github.epicer12.smart_campus_api.models.ErrorResponse;
import io.github.epicer12.smart_campus_api.models.Room;
import io.github.epicer12.smart_campus_api.models.Sensor;
import io.github.epicer12.smart_campus_api.store.DataStore;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Hasun
 */

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {
    @POST
    public Response registerSensor(Sensor sensor, @Context UriInfo uriInfo){
        // Validate that the sensor ID is provided
        if (sensor.getId() == null || sensor.getId().isEmpty()) {
            ErrorResponse error = new ErrorResponse(400, "Bad Request", "Sensor ID is required");
            return Response.status(400).entity(error).build();
        }
        
        // Additional validation for roomId field
        if (sensor.getRoomId() == null || sensor.getRoomId().isEmpty()) {
            ErrorResponse error = new ErrorResponse(400, "Bad Request", "Room ID is required");
            return Response.status(400).entity(error).build();
        }
        
        // Validation to prevent duplicate sensors
        if (DataStore.sensors.containsKey(sensor.getId())) {
            ErrorResponse error = new ErrorResponse(409, "Conflict", "Sensor with ID " + sensor.getId() + " already exists");
            return Response.status(409).entity(error).build();
        }
        
        // Validate the room actually exists
        if (!DataStore.rooms.containsKey((sensor.getRoomId()))) {
            throw new LinkedResourceNotFoundException("Room with ID " + sensor.getRoomId() + " does not exists");
        }
        
        DataStore.sensors.put(sensor.getId(), sensor);
        
        // Adding sensor's ID to the room's sensor list
        DataStore.rooms.get(sensor.getRoomId()).getSensorIds().add(sensor.getId()); 
        
        
        return Response.status(201)
                .entity(sensor)
                .header("Location", uriInfo.getBaseUri() + "sensors/" + sensor.getId())
                .build();
    }
    
    @GET
    public Response getAllSensors(@QueryParam("type") String type, @Context UriInfo uriInfo) {
        List<Map<String, Object>> summaryList = new ArrayList<>();
        
        for (Sensor sensor : DataStore.sensors.values()) {
            if (type != null && !sensor.getType().equalsIgnoreCase(type)) {
                continue;
            }
            
            Map<String, Object> summary = new LinkedHashMap<>();
            summary.put("id", sensor.getId());
            summary.put("type", sensor.getType());
            summary.put("status", sensor.getStatus());
            summary.put("href", uriInfo.getBaseUri() + "sensors/" + sensor.getId());
            
            summaryList.add(summary);
        }
        
        return Response.ok(summaryList).build();
    }
    
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        
        // Validate parent resource exists before delegating to sub-resource
        if (!DataStore.sensors.containsKey(sensorId)) {
            throw new LinkedResourceNotFoundException("Sensor with ID " + sensorId + " does not exist");
        }
        
        // Return new sub-resource instance with sensor context
        return new SensorReadingResource(sensorId);
    }
    
    // The following endpoints are not explicitly required by the specification but are included
    // to prevent the API from being a dead end. Without DELETE, sensors cannot be removed,
    // making room deletion permanently impossible once a sensor is assigned.
    // Without PUT, there is no way to change a sensor's status after registration.
    
    /**
     * Returns full details of a specific sensor by ID.
     * Clients reach this endpoint by following the href included in the GET /sensors list response,
     * which is why the list only returns summary objects rather than full sensor data.
     */
    
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
    
    /**
    * Removes a sensor from the system by ID.
    * Also removes the sensor's ID from the parent room's sensorIds list
    * and deletes all associated readings to prevent orphaned data.
    * Returns 404 if the sensor does not exist.
    */
    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            ErrorResponse error = new ErrorResponse(404, "Not Found", "Sensor with ID " + sensorId + " was not found");
            return Response.status(404).entity(error).build();
        }

        // Remove sensor ID from the parent room's sensorIds list
        Room parentRoom = DataStore.rooms.get(sensor.getRoomId());
        if (parentRoom != null) {
            parentRoom.getSensorIds().remove(sensorId);
        }

        // Remove all readings associated with this sensor
        DataStore.sensorReadings.remove(sensorId);

        // Remove the sensor itself
        DataStore.sensors.remove(sensorId);

        return Response.status(204).build();
    }
    
    /**
    * Updates a sensor's status and currentValue.
    * This is necessary because sensors change state over time (ACTIVE, MAINTENANCE, OFFLINE)
    * and their values may need manual correction.
    * Returns 404 if the sensor does not exist.
    */
    @PUT
    @Path("/{sensorId}")
    public Response updateSensor(@PathParam("sensorId") String sensorId, Sensor updatedSensor) {
        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            ErrorResponse error = new ErrorResponse(404, "Not Found", "Sensor with ID " + sensorId + " was not found");
            return Response.status(404).entity(error).build();
        }

        if (updatedSensor.getStatus() != null) {
            sensor.setStatus(updatedSensor.getStatus());
        }

        if (updatedSensor.getType() != null) {
            sensor.setType(updatedSensor.getType());
        }

        sensor.setCurrentValue(updatedSensor.getCurrentValue());

        return Response.ok(sensor).build();
    }
}
