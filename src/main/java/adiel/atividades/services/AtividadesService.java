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
        List<AtividadeEntity> atividades = repository.findAllByUserId(userId);
        for (int i=0; i<atividades.size(); i++){
            System.out.println(atividades.get(i).toString());
            if(atividades.get(i).getId().equals(id)){
                return atividades.get(i);
            }
        }
        
        throw new Exception(this.NOT_FOUND_EXCEPTION_STRING);
    }
     
    public AtividadeEntity createAtividade(AtividadeEntity entity) throws Exception 
    {
        
        System.out.println("\n\nAtividade: "+ entity.toString() + "\n\n" ) ;
        entity = repository.save(entity);
            
        return entity;
    } 

    public AtividadeEntity updateAtividade(AtividadeEntity entity, Long id, Long userId) throws Exception {

        List<AtividadeEntity> atividades = repository.findAllByUserId(userId);
        for (int i=0; i<atividades.size(); i++){
            System.out.println(atividades.get(i).toString());
            if(atividades.get(i).getId().equals(id)){
                AtividadeEntity atividade = atividades.get(i);
                atividade.setDescricao(entity.getDescricao());
                atividade.setTitulo(entity.getTitulo());
                atividade.setCreatedAt(entity.getCreatedAt());
                atividade.setUpdatedAt(new Date(System.currentTimeMillis()));
        
                atividade = repository.save(atividade);
                    
                return atividade;
            }
        }
        
        throw new Exception(this.NOT_FOUND_EXCEPTION_STRING);

    } 
     
    public void deleteAtividadeById(Long id, Long userId) throws Exception {
        List<AtividadeEntity> atividades = repository.findAllByUserId(userId);

        for (int i=0; i<atividades.size(); i++){
            if(atividades.get(i).getId().equals(id)){
                repository.deleteById(id);
                return;
            }
        }
         
        throw new Exception(this.NOT_FOUND_EXCEPTION_STRING);
        
    } 
}