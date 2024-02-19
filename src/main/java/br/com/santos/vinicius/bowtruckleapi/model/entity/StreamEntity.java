package br.com.santos.vinicius.bowtruckleapi.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Data
@Entity
@Table(name = "StreamDetails")
public class StreamEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -6046813733726535466L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String title;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate streamDate;

    @OneToOne
    @JoinColumn(name = "game_id")
    private GameEntity playingGame;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "stream_details_id")
    private List<StreamGamesEntity> streamGamesList;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedAt;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endedAt;

    public StreamEntity() {
        this.streamDate = LocalDate.now();
        this.startedAt = LocalDateTime.now();
    }

}
