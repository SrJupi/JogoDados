package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.service;

import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.GameEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.PlayerEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.GameDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.PlayerPercentageDTO;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public PlayerPercentageDTO playerToPercentageDTO (PlayerEntity player){
        return new PlayerPercentageDTO(player.getUserId(), player.getName(),
                (float)player.getGamesWon()/(float)player.getGamesPlayed());
    }

    public GameDTO getGamesDTO(GameEntity game) {
        String response = game.getDiceOne() + game.getDiceTwo() == 7
                ? "Winner, Winner, Chicken Dinner!"
                : "Loser!";
        return new GameDTO(game.getDiceOne(), game.getDiceTwo(), response);
    }
}
