package br.com.santos.vinicius.bowtruckleapi.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Games")
public class GameEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1082468562988743295L;

    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private String boxArtUrl;
}
