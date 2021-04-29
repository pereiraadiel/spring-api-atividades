package adiel.atividades.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import adiel.atividades.entities.AtividadeEntity;
import adiel.atividades.repositories.AtividadeRepository;

 
@Service
public class AtividadesService {
     
    @Autowired
    AtividadeRepository repository;

    private String NOT_FOUND_EXCEPTION_STRING = "Atividade não encontrada";
     
    public List<AtividadeEntity> getAllAtividades(Long userId)
    {
        List<AtividadeEntity> atividades = repository.findAllByUserId(userId);
         
        if(atividades.size() > 0) {
            return atividades;
        } else {
            return new ArrayList<AtividadeEntity>();
        }
    }
     
    public AtividadeEntity getAtividadeById(Long id, Long userId) throws Exception {
        AtividadeEntity atividade = repository.findByIdAndUserId(id, userId);
        if(atividade == null) throw new Exception(this.NOT_FOUND_EXCEPTION_STRING);
        return atividade;
    }
     
    public AtividadeEntity createAtividade(AtividadeEntity entity) throws Exception {
        
        entity = repository.save(entity);        
        if(entity == null) throw new Exception("Erro ao criar atividade");
        return entity;

    } 

    public AtividadeEntity updateAtividade(AtividadeEntity entity, Long id, Long userId) throws Exception {
        
        AtividadeEntity atividade = repository.findByIdAndUserId(id, userId);
        if(atividade == null) throw new Exception(this.NOT_FOUND_EXCEPTION_STRING);
        
        atividade.setDescricao(entity.getDescricao());
        atividade.setTitulo(entity.getTitulo());
        atividade.setCreatedAt(entity.getCreatedAt());
        atividade.setUpdatedAt(new Date(System.currentTimeMillis()));

        atividade = repository.save(atividade);
            
        return atividade;

    } 
     
    public void deleteAtividadeById(Long id, Long userId) throws Exception {
        
        AtividadeEntity atividade = repository.findByIdAndUserId(id, userId);
        if(atividade == null)  throw new Exception(this.NOT_FOUND_EXCEPTION_STRING);
        repository.delete(atividade);
         
    } 
}