package io.github.epicer12.smart_campus_api.resources;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Hasun
 */

@Path("/")
public class DiscoveryResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover(@Context UriInfo uriInfo) {
        String base = uriInfo.getBaseUri().toString();
        
        Map<String, Object> response = new LinkedHashMap<>();
        
        // API version info
        response.put("version", "1.0");
        response.put("name", "Smart Campus API");
        response.put("developer", "Hasun Tisera");
        response.put("support", "uddika.20240397@iit.ac.lk");
        
        // Available routes
        Map<String, String> routes = new LinkedHashMap<>();
        routes.put("discovery", base);
        routes.put("rooms", base + "rooms");
        routes.put("room_detail", base + "rooms/{roomId}");
        routes.put("sensors", base + "sensors");
        routes.put("sensor_detail", base + "sensors/{sensorId}");
        routes.put("sensor_filter", base + "sensors?type={type}");
        routes.put("sensor_readings", base +"sensors/{sensorId}/readings");
        
        response.put("links", routes);
        
        return Response.ok(response).build();
    }
}
