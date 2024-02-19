package br.com.santos.vinicius.bowtruckleapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serial;

public class BadRequestException extends ResponseStatusException {

    @Serial
    private static final long serialVersionUID = -7665082607213742865L;

    public BadRequestException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
