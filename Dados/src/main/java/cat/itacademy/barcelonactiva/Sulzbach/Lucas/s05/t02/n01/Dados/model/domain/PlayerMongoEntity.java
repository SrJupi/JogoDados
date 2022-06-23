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
@Document("players")
public class PlayerMongoEntity {

    @Id
    private String id;

    private Integer userId;
    private String name;
    private String registerDate;
    private List<GameMongoEntity> gamesList = new ArrayList<>();

    public PlayerMongoEntity() {
    }

    public PlayerMongoEntity(Integer userId, String name) {
        this.userId = userId;
        this.name = name;
        this.registerDate = setRegisterDate();
    }

    private String setRegisterDate (){
        return new Timestamp(new Date().getTime()).toString();
    }

    public void addGamesList (GameMongoEntity game){
        gamesList.add(game);
    }

}
