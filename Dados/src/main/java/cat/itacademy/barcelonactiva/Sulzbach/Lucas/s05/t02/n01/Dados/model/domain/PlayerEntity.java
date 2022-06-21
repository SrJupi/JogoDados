package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "players")
public class PlayerEntity {

    @Id
    @SequenceGenerator(name="players_seq", sequenceName="player_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "player_seq")
    private Integer userId;
    private String name;
    private Timestamp registerDate;
    private Integer gamesWon;
    private Integer gamesPlayed;

    @JsonManagedReference
    @OneToMany(mappedBy="player")
    Set <GameEntity> gamesList = new HashSet<>();

    public PlayerEntity() {
    }

    public PlayerEntity(String name) {
        this.name = name;
        this.registerDate = setRegisterDate();
        this.gamesWon = 0;
        this.gamesPlayed = 0;
    }

    public void addGamePlayed (){
        this.gamesPlayed += 1;
    }

    public void addGameWon (){
        this.gamesWon += 1;
    }

    private Timestamp setRegisterDate (){
        return new Timestamp(new Date().getTime());
    }
}
