package br.com.santos.vinicius.bowtruckleapi.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Data
@Entity
@Table(name = "StreamGamesList")
@EntityListeners(AuditingEntityListener.class)
public class StreamGamesEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -8256568289177036822L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", foreignKey = @ForeignKey(name = "game"), nullable = false)
    private GameEntity game;

    @Column
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedAt;

    @Column
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endedAt;

    public StreamGamesEntity() {
        this.startedAt = LocalDateTime.now();
    }
}
