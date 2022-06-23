package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlayerDTO {

    private Integer userId;
    private String name;
    private Double percentage;
    private List<GameDTO> gamesList = null;

    public PlayerDTO(Integer userId, String name, Double percentage) {
        this.userId = userId;
        this.name = name;
        this.percentage = percentage;
    }

    public PlayerDTO(Integer userId, String name, Double percentage, List<GameDTO> gameDTOList) {
        this.userId = userId;
        this.name = name;
        this.percentage = percentage;
        this.gamesList = gameDTOList;
    }
}
