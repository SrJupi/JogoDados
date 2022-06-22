package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.service;

import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.GameEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.PlayerEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.GameDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.exceptions.ResourceAlreadyExistException;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.exceptions.ResourceNotFoundException;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.GamesRepository;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
        float games = (float) playerEntityList
                .stream()
                .map(p -> p
                        .getGamesList()
                        .size())
                .count();
        float wins = (float) playerEntityList
                .stream()
                .map(p -> p.getGamesList()
                .stream()
                .filter(g -> g.getDiceOne() + g.getDiceTwo() == 7)
                        .count())
                .count();
        return new ResponseEntity<>(wins/games, HttpStatus.OK);
    }

    public PlayerDTO getLoserRanking() {
        List<PlayerEntity> playerList = playerRepository.findAll();
        PlayerDTO lowestPlayer = playerList
                .stream()
                .map(player -> mapToDTO(player))
                .min(Comparator.comparing(PlayerDTO::getPercentage))
                .orElseThrow(() -> new ResourceNotFoundException());
        return lowestPlayer;
    }

    public PlayerDTO getWinnerRanking() {
        List<PlayerEntity> playerList = playerRepository.findAll();
        PlayerDTO highestPlayer = playerList
                .stream()
                .map(player -> mapToDTO(player))
                .max(Comparator.comparing(PlayerDTO::getPercentage))
                .orElseThrow(() -> new ResourceNotFoundException());
        return highestPlayer;
    }

    public PlayerDTO addPlayer(PlayerDTO addPlayerDTO) {
        if (addPlayerDTO.getName() == null) {
            addPlayerDTO.setName("Unknown");
        } else if (playerRepository.existsByName(addPlayerDTO.getName())){
            throw new ResourceAlreadyExistException("player", "name", addPlayerDTO.getName());
        }
        PlayerEntity player = playerRepository.save(mapToEntity(addPlayerDTO));
        return mapToDTO(player);
    }

    public GameDTO playGame(Integer id) {
        PlayerEntity player = playerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("player", "id", String.valueOf(id)));
        GameEntity game = gamesRepository.save(new GameEntity(player));
        return mapToDTO(game);
    }



    public ResponseEntity<PlayerDTO> updatePlayer(PlayerDTO playerDTO) {
        //Check if update name exist in database,
        //if so throw error
        if (playerRepository.existsByName(playerDTO.getName())){
            throw new ResourceAlreadyExistException("player", "name", playerDTO.getName());
        }

        //find player by id
        //if not exist create new player it with addPlayer function
        //if exist update player
        Optional <PlayerEntity> optPlayer = playerRepository.findById(playerDTO.getUserId());
        if (optPlayer.isEmpty()){
            PlayerDTO responseDTO = addPlayer(playerDTO);
            return ResponseEntity
                    .created(URI.create(String.format("/players/%d", responseDTO.getUserId())))
                    .body(responseDTO);
        }else{
            PlayerEntity player = optPlayer.get();
            player.setName(playerDTO.getName());
            player = playerRepository.save(player);
            return ResponseEntity.ok(mapToDTO(player));
        }
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

    private PlayerEntity mapToEntity(PlayerDTO addPlayerDTO) {
        return new PlayerEntity(addPlayerDTO.getName());
    }
}
