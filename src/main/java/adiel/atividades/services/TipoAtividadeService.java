package adiel.atividades.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

import adiel.atividades.entities.TipoAtividade;
import adiel.atividades.repositories.TipoAtividadeRepository;

@Service
public class TipoAtividadeService {

  @Autowired
  TipoAtividadeRepository repository;
  
  public TipoAtividade getTipoAtividadeById(Long id){
    Optional<TipoAtividade> tipoAtividade = repository.findById(id);
    TipoAtividade tAtividade = null;
    if(tipoAtividade.isPresent()){
      tAtividade = tipoAtividade.get();
    }

    return tAtividade;
  }

  public TipoAtividade createTipoAtividade(TipoAtividade entity) throws Exception {
    try {
      entity = repository.save(entity);        
    }
    catch(Exception error){
      throw new Exception("Algo deu errado ao criar o tipo de atividade");
    }
    if(entity == null) throw new Exception("Erro ao criar tipo de atividade");
    return entity;
  } 

}
