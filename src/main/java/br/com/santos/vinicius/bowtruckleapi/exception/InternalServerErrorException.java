package br.com.santos.vinicius.bowtruckleapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serial;

public class InternalServerErrorException extends ResponseStatusException {

    @Serial
    private static final long serialVersionUID = 6277504512951040696L;

    public InternalServerErrorException(String reason) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, reason);
    }
}
