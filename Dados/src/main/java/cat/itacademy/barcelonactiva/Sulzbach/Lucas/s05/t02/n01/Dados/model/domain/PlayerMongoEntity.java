package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document("player")
public class PlayerMongoEntity {

    @Id
    private String id;

    private Integer userId;
    private String name;
    private Timestamp registerDate;
    private List<GameMongoEntity> gamesList = new ArrayList<>();

    public PlayerMongoEntity() {
    }

    public PlayerMongoEntity(String name) {
        this.name = name;
        this.registerDate = setRegisterDate();
    }

    private Timestamp setRegisterDate (){
        return new Timestamp(new Date().getTime());
    }

}
