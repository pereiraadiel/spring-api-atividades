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
     
    public List<AtividadeEntity> getAllAtividades(Long userId)
    {
        List<AtividadeEntity> atividades = repository.findAllByUserId(userId);
         
        if(atividades.size() > 0) {
            return atividades;
        } else {
            return new ArrayList<AtividadeEntity>();
        }
    }
     
    public AtividadeEntity getAtividadeById(Long id) throws Exception 
    {
        Optional<AtividadeEntity> atividade = repository.findById(id);
         
        if(atividade.isPresent()) {
            return atividade.get();
        } else {
            throw new Exception("Atividade nao encontrada");
        }
    }
     
    public AtividadeEntity createAtividade(AtividadeEntity entity) throws Exception 
    {
        
        System.out.println("\n\nAtividade: "+ entity.toString() + "\n\n" ) ;
        entity = repository.save(entity);
            
        return entity;
    } 

    public AtividadeEntity updateAtividade(AtividadeEntity entity, Long id) throws Exception 
    {
        Optional<AtividadeEntity> atividade = repository.findById(entity.getId());
         
        AtividadeEntity newEntity = atividade.get();
        newEntity.setId(entity.getId());
        newEntity.setDescricao(entity.getDescricao());
        newEntity.setTitulo(entity.getTitulo());
        newEntity.setCreatedAt(entity.getCreatedAt());
        newEntity.setUpdatedAt(new Date(System.currentTimeMillis()));

        newEntity = repository.save(newEntity);
            
        return newEntity;
    } 
     
    public void deleteAtividadeById(Long id) throws Exception 
    {
        Optional<AtividadeEntity> atividade = repository.findById(id);
         
        if(atividade.isPresent()) 
        {
            repository.deleteById(id);
        } else {
            throw new Exception("Atividade nao encontrada");
        }
    } 
}