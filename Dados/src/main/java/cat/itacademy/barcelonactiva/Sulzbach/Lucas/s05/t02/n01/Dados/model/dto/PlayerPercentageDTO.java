package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerPercentageDTO {

    private Integer userId;
    private String name;
    private Float percentage;

    public PlayerPercentageDTO(Integer userId, String name, Float percentage) {
        this.userId = userId;
        this.name = name;
        this.percentage = percentage;
    }
}
