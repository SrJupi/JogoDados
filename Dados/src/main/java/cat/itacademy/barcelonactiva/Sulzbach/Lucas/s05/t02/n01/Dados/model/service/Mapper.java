package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.service;

import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.GameEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.PlayerEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.GameDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.PlayerPercentageDTO;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public PlayerPercentageDTO playerToPercentageDTO (PlayerEntity player){
        float percentage = (float)player.getGamesPlayed()
                .stream()
                .filter(g -> g.getDiceOne() + g.getDiceTwo() == 7)
                .count() / (float)player.getGamesPlayed().stream().count();
        return new PlayerPercentageDTO(player.getUserId(), player.getName(), percentage);
    }

    public GameDTO getGamesDTO(GameEntity game) {
        String response = game.getDiceOne() + game.getDiceTwo() == 7
                ? "Winner, Winner, Chicken Dinner!"
                : "Loser!";
        return new GameDTO(game.getDiceOne(), game.getDiceTwo(), response);
    }
}
