package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository;

import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.PlayerMongoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerMongoRepository extends MongoRepository<PlayerMongoEntity, String> {


    boolean existsByName(String name);

    Optional<PlayerMongoEntity> findByName(String name);

    Optional<PlayerMongoEntity> findByUserId(Integer id);

    Optional<PlayerMongoEntity> findTopByOrderByUserIdDesc();
}
