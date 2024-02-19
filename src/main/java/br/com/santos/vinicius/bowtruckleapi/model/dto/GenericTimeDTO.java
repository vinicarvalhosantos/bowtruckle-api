package br.com.santos.vinicius.bowtruckleapi.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class GenericTimeDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2662836463829807592L;

    private Long id;

    private long hours;

    private long minutes;

    private long seconds;

    private String formattedMessage;

    public GameTimeDTO toGameTimeDto(){
        GameTimeDTO gameTimeDTO = new GameTimeDTO();
        gameTimeDTO.setHours(this.hours);
        gameTimeDTO.setMinutes(this.minutes);
        gameTimeDTO.setSeconds(this.seconds);
        gameTimeDTO.setFormattedMessage(this.formattedMessage);

        return gameTimeDTO;
    }

    public StreamTimeDTO toStreamTimeDto(){
        StreamTimeDTO streamTimeDTO = new StreamTimeDTO();
        streamTimeDTO.setHours(this.hours);
        streamTimeDTO.setMinutes(this.minutes);
        streamTimeDTO.setSeconds(this.seconds);
        streamTimeDTO.setFormattedMessage(this.formattedMessage);

        return streamTimeDTO;
    }
}
