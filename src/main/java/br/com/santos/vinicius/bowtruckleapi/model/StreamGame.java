package br.com.santos.vinicius.bowtruckleapi.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class StreamGame {

    private String id;

    private String name;

    private String boxArtUrl;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    public StreamGame(String id, String name, String boxArtUrl, LocalDateTime startedAt) {
        this.id = id;
        this.name = name;
        this.boxArtUrl = boxArtUrl;
        this.startedAt = startedAt;
    }

    @Override
    public String toString() {
        return "StreamGame{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", boxArtUrl='" + boxArtUrl + '\'' +
                ", startedAt=" + startedAt +
                ", endedAt=" + endedAt +
                '}';
    }
}
