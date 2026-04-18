package io.github.epicer12.smart_campus_api.mappers;

import io.github.epicer12.smart_campus_api.models.ErrorResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Hasun skibidi man
 */

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable>{
    @Override
    public Response toResponse(Throwable e) {
        ErrorResponse error = new ErrorResponse(
                500, 
                "Internal Server Error", 
                "An Unexpected error occured"
        );
        return Response.status(500)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build(); 
    }
}
