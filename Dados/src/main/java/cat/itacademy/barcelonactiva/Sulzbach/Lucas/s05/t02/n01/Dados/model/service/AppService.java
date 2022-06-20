package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.service;

import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.GamesEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.PlayerEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.PlayerPercentageDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.GamesRepository;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GamesRepository gamesRepository;

    public ResponseEntity <?> getPlayers (){
        List<PlayerEntity> playerEntityList = playerRepository.findAll();
        if (playerEntityList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<PlayerPercentageDTO> playerPercentageDTOList = new ArrayList<>();
        Integer wins = 0;
        Integer games = 0;
        for (PlayerEntity player :
                playerEntityList) {
            for (GamesEntity game :
                    player.getGamesPlayed()) {
                games += 1;
                if (game.getIsWin()) wins +=1;
            }
            playerPercentageDTOList.add(new PlayerPercentageDTO(player.getUserId(), player.getName(),
                    (float) (wins / games)));
        }
        return new ResponseEntity<>(playerPercentageDTOList, HttpStatus.OK);
    }

}
