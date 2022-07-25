package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginPlayerDTO {

    private Integer userId;
    private String password;

    public LoginPlayerDTO(){}

    public LoginPlayerDTO(Integer userId, String password) {
        this.userId = userId;
        this.password = password;
    }

}
