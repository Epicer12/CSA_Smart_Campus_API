package io.github.epicer12.smart_campus_api.mappers;

import io.github.epicer12.smart_campus_api.exceptions.SensorUnavailableException;
import io.github.epicer12.smart_campus_api.models.ErrorResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Hasun
 */

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException>{
    @Override
    public Response toResponse(SensorUnavailableException e) {
        ErrorResponse error = new ErrorResponse(403, "Forbidden", e.getMessage());
        return Response.status(403)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build(); 
    }
}
