package io.github.epicer12.smart_campus_api.mappers;

import io.github.epicer12.smart_campus_api.exceptions.RoomNotEmptyException;
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
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {
    @Override
    public Response toResponse(RoomNotEmptyException e) {
        ErrorResponse error = new ErrorResponse(409, "Conflict", e.getMessage());
        return Response.status(409)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();                
    }
}
