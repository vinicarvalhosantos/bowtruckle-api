package br.com.santos.vinicius.bowtruckleapi.model.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameTimeDTO extends GenericTimeDTO {

    @Serial
    private static final long serialVersionUID = 8315567981237668520L;

    private String name;

}
