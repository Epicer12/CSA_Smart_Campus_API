/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.epicer12.smart_campus_api.mappers;

import io.github.epicer12.smart_campus_api.exceptions.LinkedResourceNotFoundException;
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
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException>{
    @Override
    public Response toResponse(LinkedResourceNotFoundException e) {
        ErrorResponse error = new ErrorResponse(422, "Unprocessable Entity", e.getMessage());
        return Response.status(422)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build(); 
    }
}
