package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto;

import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.PlayerEntity;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class GameDTO {


    private Integer gameId;
    private Integer diceOne;
    private Integer diceTwo;

    public GameDTO(Integer gameId, Integer diceOne, Integer diceTwo) {
        this.gameId = gameId;
        this.diceOne = diceOne;
        this.diceTwo = diceTwo;
    }
}
