package adiel.atividades.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import adiel.atividades.entities.AtividadeEntity;
import adiel.atividades.entities.User;
import adiel.atividades.repositories.AtividadeRepository;

public class AtividadeDetailsService {
  
  @Autowired
  private AtividadeRepository atividadeRepo;  

  public List<AtividadeEntity> findAllByUser(Long userId){
      List<AtividadeEntity> atividades = atividadeRepo.findAll();
      List<AtividadeEntity> userAtividades = new ArrayList();
      
      for ( int i=0; i< atividades.size(); i++) {
        AtividadeEntity atividade = atividades.get(i);
        if ( atividade.getUserId() == userId ){
          userAtividades.add(atividade);
        }
      }

      return userAtividades;
  }
}
