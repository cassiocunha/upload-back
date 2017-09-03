/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.monkeyhand.upload.models.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author cassio
 */
@Provider
public class DefaultExceptionHandler implements ExceptionMapper<Exception> {
    
    private final String defaultMessage = "Unexpected error";
    
    @Override
    public Response toResponse(Exception exception) {        
        ExceptionErrorMessage error = new ExceptionErrorMessage();
        error.setCode(500);
        if (exception.getMessage() == null) {
            error.setMessage(defaultMessage);
        } else {
            error.setMessage(exception.getMessage());
        }
        return Response.serverError().entity(error).type(MediaType.APPLICATION_JSON).build();
    }
}
