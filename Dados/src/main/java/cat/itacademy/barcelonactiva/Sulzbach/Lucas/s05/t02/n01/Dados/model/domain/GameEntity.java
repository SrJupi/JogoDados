package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

@Getter
@Setter
@Entity
@Table(name = "games")
public class GameEntity {

    @Id
    @SequenceGenerator(name="games_seq", sequenceName="game_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "games_seq")
    private Integer gameId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="FK_UserId", nullable = false)
    private PlayerEntity player;

    private Timestamp gameTime;
    private Integer diceOne;
    private Integer diceTwo;

    public GameEntity() {
    }

    public GameEntity(PlayerEntity playerEntity) {
        this.player = playerEntity;
        this.gameTime = setGameTime();
        this.diceOne = rollDice();
        this.diceTwo = rollDice();
    }

    private Timestamp setGameTime(){
        return new Timestamp(new Date().getTime());
    }

    private Integer rollDice(){
        return new Random().nextInt(6) + 1;
    }


}
