/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.monkeyhand.upload.models.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 *
 * @author cassio
 */
public class BussinesExceptionHandler implements ExceptionMapper<BussinesException> {

    @Override
    public Response toResponse(BussinesException exception) {
        ExceptionErrorMessage message = new ExceptionErrorMessage();
        message.setCode(400);
        message.setMessage(exception.getMessage());
        return Response.status(message.getCode()).type(MediaType.APPLICATION_JSON).entity(message).build();
    }
}
