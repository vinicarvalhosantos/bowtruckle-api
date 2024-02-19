package br.com.santos.vinicius.bowtruckleapi.model.response;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
public class GenericResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -3318580974564343629L;
    private final String message;

    private final Timestamp timestamp;

    public GenericResponse(String message) {
        this.message = message;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

}
