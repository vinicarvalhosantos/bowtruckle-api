package br.com.santos.vinicius.bowtruckleapi.controller.handler;

import br.com.santos.vinicius.bowtruckleapi.exception.BadRequestException;
import br.com.santos.vinicius.bowtruckleapi.exception.InternalServerErrorException;
import br.com.santos.vinicius.bowtruckleapi.exception.NotFoundException;
import br.com.santos.vinicius.bowtruckleapi.model.response.ErrorResponse;
import br.com.santos.vinicius.bowtruckleapi.model.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomExceptionHandler {

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response> handleInternalServerError(InternalServerErrorException exception, HttpServletRequest servletRequest) {
        return handleException(exception, servletRequest);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response> handleNotFound(NotFoundException exception, HttpServletRequest servletRequest) {
        return handleException(exception, servletRequest);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleBadRequest(BadRequestException exception, HttpServletRequest servletRequest) {
        return handleException(exception, servletRequest);
    }

    private ResponseEntity<Response> handleException(ResponseStatusException exception, HttpServletRequest servletRequest) {
        int status = exception.getBody().getStatus();
        ErrorResponse errorResponse = new ErrorResponse(exception.getReason(),
                status, exception.getBody().getTitle(), Long.parseLong(status + "1"), servletRequest.getServletPath());

        return ResponseEntity.status(status).body(new Response(errorResponse));
    }
}
