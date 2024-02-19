package br.com.santos.vinicius.bowtruckleapi.model.dto;

import br.com.santos.vinicius.bowtruckleapi.model.entity.GameEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StreamTimeDTO extends GenericTimeDTO {

    @Serial
    private static final long serialVersionUID = -2461172348113110240L;

    private String title;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private GameEntity playingGame;
}
