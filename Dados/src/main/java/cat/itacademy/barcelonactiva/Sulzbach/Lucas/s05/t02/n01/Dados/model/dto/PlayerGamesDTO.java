package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlayerGamesDTO {

    private Integer userId;
    private String name;
    private Double percentage;
    private List<GameDTO> gamesList = null;

    public PlayerGamesDTO(){}

    public PlayerGamesDTO(Integer userId, String name, Double percentage, List<GameDTO> gameDTOList) {
        this.userId = userId;
        this.name = name;
        this.percentage = percentage;
        this.gamesList = gameDTOList;
    }
}
