package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
public class AddPlayerDTO {

    private String name;
    private String password;
    private Set<String> roles = new HashSet<>();

    public AddPlayerDTO(){}

    public AddPlayerDTO(String name, String password) {
        this.name = name;
        this.password = password;
    }

}
