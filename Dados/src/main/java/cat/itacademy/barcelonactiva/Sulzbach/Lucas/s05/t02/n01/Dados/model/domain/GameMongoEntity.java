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
public class GameMongoEntity {

    private Timestamp gameTime;
    private Integer diceOne;
    private Integer diceTwo;

    public GameMongoEntity() {
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
