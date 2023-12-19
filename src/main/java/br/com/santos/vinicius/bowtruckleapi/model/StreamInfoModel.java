package br.com.santos.vinicius.bowtruckleapi.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StreamInfoModel {

    private String title;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private StreamGame playingGame;

    private List<StreamGame> streamGameList;


    public StreamInfoModel(String title, StreamGame playingGame, List<StreamGame> streamGameList) {
        this.title = title;
        this.startedAt = LocalDateTime.now();
        this.playingGame = playingGame;
        this.streamGameList = streamGameList;
    }

    @Override
    public String toString() {
        return "StreamInfoModel{" +
                "title='" + title + '\'' +
                ", startedAt=" + startedAt +
                ", playingGame=" + playingGame +
                ", streamGameList=" + streamGameList +
                '}';
    }
}
