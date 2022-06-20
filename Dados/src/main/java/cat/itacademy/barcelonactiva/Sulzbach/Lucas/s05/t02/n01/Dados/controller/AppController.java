package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.controller;

import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.GamesEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.PlayerEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.GamesRepository;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.PlayerRepository;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.service.AppService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AppController {

    @Autowired
    private GamesRepository gamesRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private AppService appService;

    @GetMapping("/players")
    public ResponseEntity<?> getPlayers (){
        return appService.getPlayers();
    }

    @GetMapping("/games")
    public ResponseEntity<?> getGames (){
        List<GamesEntity> gamesEntityList = gamesRepository.findAll();
        return new ResponseEntity<>(gamesEntityList, HttpStatus.OK);
    }

    @GetMapping("/add")
    public void addPlayers (){
        PlayerEntity player1 = new PlayerEntity("Test1");
        playerRepository.save(player1);
        PlayerEntity player2 = new PlayerEntity("Test2");
        playerRepository.save(player2);
        GamesEntity game = new GamesEntity(player1);
        gamesRepository.save(game);
        game = new GamesEntity(player1);
        gamesRepository.save(game);
        game = new GamesEntity(player2);
        gamesRepository.save(game);
        game = new GamesEntity(player2);
        gamesRepository.save(game);
        game = new GamesEntity(player2);
        gamesRepository.save(game);
    }
}
