package br.com.santos.vinicius.bowtruckleapi.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serial;

@Getter
public class ErrorResponse extends GenericResponse {

    @Serial
    private static final long serialVersionUID = 7998718069252951357L;

    private final int status;

    private final String error;

    @JsonProperty(value = "error_code")
    private Long errorCode;

    private final String path;

    public ErrorResponse(String message, int status, String error, Long errorCode, String path) {
        super(message);
        this.status = status;
        this.error = error;
        this.errorCode = errorCode;
        this.path = path;
    }
}
