/**
 * Handles content-type mismatches on endpoints annotated with @Consumes(APPLICATION_JSON).
 * Jersey automatically throws NotSupportedException when a client sends a request with
 * a non-JSON Content-Type. Without this mapper, that exception falls through to the
 * GlobalExceptionMapper and returns a generic 500. This intercepts it first and returns
 * a proper 415 Unsupported Media Type response.
 */

package io.github.epicer12.smart_campus_api.mappers;

import io.github.epicer12.smart_campus_api.models.ErrorResponse;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotSupportedExceptionMapper implements ExceptionMapper<NotSupportedException> {
    @Override
    public Response toResponse(NotSupportedException e) {
        ErrorResponse error = new ErrorResponse(
            415,
            "Unsupported Media Type",
            "This endpoint only accepts application/json. Please set Content-Type: application/json"
        );
        return Response.status(415)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}