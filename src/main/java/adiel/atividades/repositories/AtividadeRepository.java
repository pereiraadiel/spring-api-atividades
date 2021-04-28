package adiel.atividades.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import adiel.atividades.entities.AtividadeEntity;

@Repository
public interface AtividadeRepository extends JpaRepository<AtividadeEntity, Long> {
  List<AtividadeEntity> findAllByUserId(Long userId);
}