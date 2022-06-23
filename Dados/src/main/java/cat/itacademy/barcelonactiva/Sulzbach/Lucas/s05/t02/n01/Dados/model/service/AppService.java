package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.service;

import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.GameMongoEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.PlayerMongoEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.GameDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.exceptions.ResourceAlreadyExistException;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.exceptions.ResourceNotFoundException;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.PlayerMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppService {

    @Autowired
    private PlayerMongoRepository playerRepository;

    public List<PlayerDTO> getPlayers() {
        return playerRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public PlayerDTO getPlayerGames(Integer id) {
        PlayerMongoEntity player = playerRepository
                .findByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("player", "id", String.valueOf(id)));
        return fullMapToDTO(player);
    }

    public ResponseEntity<Float> getAvgRanking() {
        List<PlayerMongoEntity> playerEntityList = playerRepository.findAll();
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
        List<PlayerMongoEntity> playerList = playerRepository.findAll();
        return playerList
                .stream()
                .map(this::mapToDTO)
                .min(Comparator.comparing(PlayerDTO::getPercentage, Comparator.nullsLast(Comparator.naturalOrder())))
                .orElseThrow(ResourceNotFoundException::new);
    }

    public PlayerDTO getWinnerRanking() {
        List<PlayerMongoEntity> playerList = playerRepository.findAll();
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
        Optional <PlayerMongoEntity> optPlayer = playerRepository.findTopByOrderByUserIdDesc();
        int userId;
        if (optPlayer.isEmpty()){
            userId = 1;
        }else{
            userId = optPlayer.get().getUserId() + 1;
        }
        PlayerMongoEntity player = playerRepository.save(mapToEntity(userId, addPlayerDTO));
        return mapToDTO(player);
    }

    public GameDTO playGame(Integer id) {
        PlayerMongoEntity player = playerRepository
                .findByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("player", "id", String.valueOf(id)));
        GameMongoEntity game = new GameMongoEntity();
        player.addGamesList(game);
        playerRepository.save(player);
        return mapToDTO(game);
    }

    public ResponseEntity<PlayerDTO> updatePlayer(PlayerDTO playerDTO) {
        //Check if update name exist in database,
        //if so throw error
        if (playerRepository.existsByName(playerDTO.getName())
                && !playerDTO.getName().equals("Unknown")){
            throw new ResourceAlreadyExistException("player", "name", playerDTO.getName());
        }

        //find player by id
        //if not exist create new player it with addPlayer function
        //if exist update player
        Optional<PlayerMongoEntity> optPlayer = playerRepository.findByUserId(playerDTO.getUserId());
        if (optPlayer.isEmpty()) {
            PlayerDTO responseDTO = addPlayer(playerDTO);
            return ResponseEntity
                    .created(URI.create(String.format("/players/%d", responseDTO.getUserId())))
                    .body(responseDTO);
        } else {
            PlayerMongoEntity player = optPlayer.get();
            String name = player.getName() == null ? "Unknown" : playerDTO.getName();
            player.setName(name);
            player = playerRepository.save(player);
            return ResponseEntity.ok(mapToDTO(player));
        }
    }

    public void deletePlayerGames(Integer id) {
        PlayerMongoEntity player = playerRepository
                .findByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("player", "id", String.valueOf(id)));
        player.setGamesList(new ArrayList<>());
        playerRepository.save(player);
    }


    // VVV new map functions VVV
    private PlayerDTO mapToDTO(PlayerMongoEntity player) {

        Double percentage = player.getGamesList().size() == 0 ? null : player.getGamesList()
                .stream()
                .mapToDouble(g -> g.getDiceOne() + g.getDiceTwo() == 7 ? 1 : 0)
                .summaryStatistics().getAverage() * 100;
        return new PlayerDTO(player.getUserId(),
                player.getName(), percentage);
    }

    private PlayerDTO fullMapToDTO(PlayerMongoEntity player) {
        Double percentage = player.getGamesList().size() == 0 ? null : player.getGamesList()
                .stream()
                .mapToDouble(g -> g.getDiceOne() + g.getDiceTwo() == 7 ? 1 : 0)
                .summaryStatistics().getAverage() * 100;
        return new PlayerDTO(player.getUserId(), player.getName(), percentage,
                player.getGamesList().stream().map(g -> mapToDTO(g)).collect(Collectors.toList()));
    }

    private GameDTO mapToDTO(GameMongoEntity game) {
        return new GameDTO(game.getDiceOne(), game.getDiceTwo());
    }

    private PlayerMongoEntity mapToEntity(Integer userId, PlayerDTO addPlayerDTO) {
        return new PlayerMongoEntity(userId, addPlayerDTO.getName());
    }
}
