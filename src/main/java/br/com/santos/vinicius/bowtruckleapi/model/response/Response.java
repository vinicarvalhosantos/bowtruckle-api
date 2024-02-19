package br.com.santos.vinicius.bowtruckleapi.model.response;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

public record Response(Object data) implements Serializable {
    @Serial
    private static final long serialVersionUID = -8087892464614852010L;

}
