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

    public List<PlayerDTO> getPlayers() {
        return playerRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<GameDTO> getPlayerGames(Integer id) {
        PlayerEntity player = playerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("player", "id", String.valueOf(id)));
        return player.getGamesList().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ResponseEntity<Float> getAvgRanking() {
        List<PlayerEntity> playerEntityList = playerRepository.findAll();
        float games = (float) playerEntityList
                .stream()
                .mapToInt(p -> p
                        .getGamesList()
                        .size())
                .sum();
        float wins = (float) playerEntityList
                .stream()
                .mapToLong(p -> p.getGamesList()
                        .stream()
                        .filter(g -> g.getDiceOne() + g.getDiceTwo() == 7)
                        .count())
                .sum();
        return ResponseEntity.ok(wins / games);
    }

    public PlayerDTO getLoserRanking() {
        List<PlayerEntity> playerList = playerRepository.findAll();
        return playerList
                .stream()
                .map(this::mapToDTO)
                .min(Comparator.comparing(PlayerDTO::getPercentage, Comparator.nullsLast(Comparator.naturalOrder())))
                .orElseThrow(ResourceNotFoundException::new);
    }

    public PlayerDTO getWinnerRanking() {
        List<PlayerEntity> playerList = playerRepository.findAll();
        return playerList
                .stream()
                .map(this::mapToDTO)
                .max(Comparator.comparing(PlayerDTO::getPercentage, Comparator.nullsFirst(Comparator.naturalOrder())))
                .orElseThrow(ResourceNotFoundException::new);
    }

    public PlayerDTO addPlayer(PlayerDTO addPlayerDTO) {
        if (addPlayerDTO.getName() == null) {
            addPlayerDTO.setName("Unknown");
        } else if (playerRepository.existsByName(addPlayerDTO.getName())) {
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
        if (playerRepository.existsByName(playerDTO.getName())) {
            throw new ResourceAlreadyExistException("player", "name", playerDTO.getName());
        }

        //find player by id
        //if not exist create new player it with addPlayer function
        //if exist update player
        Optional<PlayerEntity> optPlayer = playerRepository.findById(playerDTO.getUserId());
        if (optPlayer.isEmpty()) {
            PlayerDTO responseDTO = addPlayer(playerDTO);
            return ResponseEntity
                    .created(URI.create(String.format("/players/%d", responseDTO.getUserId())))
                    .body(responseDTO);
        } else {
            PlayerEntity player = optPlayer.get();
            player.setName(playerDTO.getName());
            player = playerRepository.save(player);
            return ResponseEntity.ok(mapToDTO(player));
        }
    }

    public void deletePlayerGames(Integer id) {
        PlayerEntity player = playerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("player", "id", String.valueOf(id)));
        player.getGamesList()
                .stream()
                .forEach(g -> gamesRepository.deleteById(g.getGameId()));
    }

    // VVV new map functions VVV

    private PlayerDTO mapToDTO(PlayerEntity player) {

        Double percentage = player.getGamesList().size() == 0 ? null : player.getGamesList()
                .stream()
                .mapToDouble(g -> g.getDiceOne() + g.getDiceTwo() == 7 ? 1 : 0)
                .summaryStatistics().getAverage() * 100;
        return new PlayerDTO(player.getUserId(),
                player.getName(), percentage);
    }

    private GameDTO mapToDTO(GameEntity game) {
        return new GameDTO(game.getGameId(), game.getDiceOne(), game.getDiceTwo());
    }

    private PlayerEntity mapToEntity(PlayerDTO addPlayerDTO) {
        return new PlayerEntity(addPlayerDTO.getName());
    }
}
