package adiel.atividades.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import adiel.atividades.entities.TipoAtividade;

@Repository
public interface TipoAtividadeRepository extends JpaRepository<TipoAtividade, Long> {
  Optional<TipoAtividade> findById(Long id);

  TipoAtividade findByTitulo(String titulo);
}