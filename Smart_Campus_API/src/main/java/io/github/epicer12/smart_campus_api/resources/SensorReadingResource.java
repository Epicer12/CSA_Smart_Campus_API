package io.github.epicer12.smart_campus_api.resources;

import io.github.epicer12.smart_campus_api.exceptions.SensorUnavailableException;
import io.github.epicer12.smart_campus_api.models.Sensor;
import io.github.epicer12.smart_campus_api.models.SensorReading;
import io.github.epicer12.smart_campus_api.store.DataStore;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Hasun
 */

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }
    
    @GET
    public Response getReadings() {
        List<SensorReading> readings = DataStore.sensorReadings.get(sensorId);
        
        if (readings == null) {
            return Response.ok(new ArrayList<>()).build();
        }
        
        return Response.ok(readings).build();
    }
    
    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        
        // Business rule: Sensors under maintenance cannot accept new readings
        if ("MAINTENANCE".equals(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor " + sensorId + " is currently in MAINTENANCE and cannot accept readings");
        }
        
        // Auto generates the reading ID, capture the timestamp and updates parent sensor's currentValue field
        reading.setId(UUID.randomUUID().toString()); 
        reading.setTimestamp(System.currentTimeMillis());
        sensor.setCurrentValue(reading.getValue());
        
        List<SensorReading> readings = DataStore.sensorReadings.get(sensorId);
        if (readings == null) {
            readings = new ArrayList<>();
            DataStore.sensorReadings.put(sensorId, readings);
        }
        
        readings.add(reading);
        
        return Response.status(201)
                .entity(reading)
                .build();
    }
}
