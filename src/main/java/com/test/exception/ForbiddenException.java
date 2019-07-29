package com.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.xml.ws.http.HTTPException;


@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class ForbiddenException extends HTTPException {

    /**
     * Constructor for the HTTPException
     *
     * @param statusCode <code>int</code> for the HTTP status code
     **/
    private String msg;
    public ForbiddenException(String msg) {
        super(401);
        this.msg = msg;
    }
}
