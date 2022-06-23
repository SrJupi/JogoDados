package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameDTO {

    private Integer diceOne;
    private Integer diceTwo;
    private String result;

    public GameDTO(Integer diceOne, Integer diceTwo) {
        this.diceOne = diceOne;
        this.diceTwo = diceTwo;
        this.result = diceOne + diceTwo == 7 ? "Winner" : "Loser";
    }
}
