package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.service;

import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.GameEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.PlayerEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.GameDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.PlayerPercentageDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.GamesRepository;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GamesRepository gamesRepository;

    @Autowired
    private Mapper mapper;

    public ResponseEntity <?> getPlayers (){
        List<PlayerEntity> playerEntityList = playerRepository.findAll();
        if (playerEntityList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<PlayerPercentageDTO> playerPercentageDTOList = playerEntityList.stream()
                .map(mapper::playerToPercentageDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(playerPercentageDTOList, HttpStatus.OK);
    }

    public ResponseEntity<?> getPlayerGames(Integer id) {
        Optional <PlayerEntity> optionalPlayer = playerRepository.findById(id);
        if (optionalPlayer.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List <GameDTO> gameDTOList = optionalPlayer.get()
                .getGamesPlayed().stream()
                .map(mapper::getGamesDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(gameDTOList, HttpStatus.OK);
    }

    public ResponseEntity<?> getAvgRanking() {
        List<PlayerEntity> playerEntityList = playerRepository.findAll();
        if (playerEntityList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        float games = (float) playerEntityList.stream().map(p -> p.getGamesPlayed().stream().count()).count();
        float wins = (float) playerEntityList.stream().map(p -> p.getGamesPlayed().stream()
                .filter(g -> g.getDiceOne() + g.getDiceTwo() == 7).count()).count();
        return new ResponseEntity<>(wins/games, HttpStatus.OK);
    }

    public ResponseEntity<?> getLoserRanking() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
