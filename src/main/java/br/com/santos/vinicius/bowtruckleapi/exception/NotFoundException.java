package br.com.santos.vinicius.bowtruckleapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serial;

public class NotFoundException extends ResponseStatusException {

    @Serial
    private static final long serialVersionUID = -9080266830991489163L;

    public NotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}
