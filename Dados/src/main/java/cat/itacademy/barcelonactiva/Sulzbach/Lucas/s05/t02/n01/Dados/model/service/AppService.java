package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.service;

import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.GameEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.PlayerEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.AddPlayerDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.GameDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.exceptions.ResourceNotFoundException;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.GamesRepository;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GamesRepository gamesRepository;

    public List <PlayerDTO> getPlayers (){
        return playerRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
        }

    public List<GameDTO> getPlayerGames(Integer id) {
        PlayerEntity player = playerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("player", "id", String.valueOf(id)));
        return player.getGamesList().stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    @Deprecated
    public ResponseEntity<?> getAvgRanking() {
        List<PlayerEntity> playerEntityList = playerRepository.findAll();
        float games = (float) playerEntityList.stream().map(p -> p.getGamesList().size()).count();
        float wins = (float) playerEntityList.stream().map(p -> p.getGamesList().stream()
                .filter(g -> g.getDiceOne() + g.getDiceTwo() == 7).count()).count();
        return new ResponseEntity<>(wins/games, HttpStatus.OK);
    }
    @Deprecated
    public ResponseEntity<?> getLoserRanking() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Deprecated
    public ResponseEntity<?> getWinnerRanking() {
        List <PlayerEntity> playerEntityList = playerRepository.findAll();
        if (playerEntityList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Deprecated
    public ResponseEntity<?> addPlayer(AddPlayerDTO addPlayerDTO) {
        if (addPlayerDTO.getName().equals("Unknown")){
            if (playerRepository.existsByName(addPlayerDTO.getName())){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        PlayerEntity savedPlayer = playerRepository.save(new PlayerEntity(addPlayerDTO.getName()));
        return ResponseEntity
                .created(URI.create(String.format("/players/%d", savedPlayer.getUserId())))
                .build();
    }

    public GameDTO playGame(Integer id) {
        PlayerEntity player = playerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("player", "id", String.valueOf(id)));
        GameEntity game = gamesRepository.save(new GameEntity(player));
        return mapToDTO(game);
    }

    // VVV new map functions VVV
    private PlayerDTO mapToDTO(PlayerEntity player) {
        double percentage = player.getGamesList()
                .stream()
                .mapToDouble(g -> g.getDiceOne() + g.getDiceTwo() == 7 ? 1 : 0)
                .summaryStatistics().getAverage();
        return new PlayerDTO(player.getUserId(),
                player.getName(), percentage * 100);
    }

    private GameDTO mapToDTO(GameEntity game) {
        return new GameDTO(game.getGameId(), game.getDiceOne(), game.getDiceTwo());
    }
}
