package adiel.atividades.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import adiel.atividades.MyUserPrincipal;
import adiel.atividades.dtos.AtividadeDTO;
import adiel.atividades.entities.AtividadeEntity;
import adiel.atividades.entities.TipoAtividade;
import adiel.atividades.entities.User;
import adiel.atividades.repositories.AtividadeRepository;

 
@Service
public class AtividadesService {
     
    @Autowired
    AtividadeRepository repository;
    
    @Autowired
    TipoAtividadeService tService;

    @Autowired
    ExternalAtividadeAPI externalApi;

    private final String NOT_FOUND_EXCEPTION_STRING = "Atividade não encontrada";
     
    public List<AtividadeEntity> getAllAtividades(){
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
            .getAuthentication();
        MyUserPrincipal userPrincipal = (MyUserPrincipal) auth.getPrincipal();
        List<AtividadeEntity> atividades = new ArrayList<AtividadeEntity>();
        
        User user = userPrincipal.getUser();
        if(user.getTipo().equals("interno")){
            atividades = repository.findAllByUserId(user.getId());
        }else {
            // atividades = externalApi.getAllAtividades();
        }
        return atividades;
    }
     
    public AtividadeEntity getAtividadeById(String id) throws Exception {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
            .getAuthentication();
        MyUserPrincipal userPrincipal = (MyUserPrincipal) auth.getPrincipal();
        AtividadeEntity atividade = null;
        
        User user = userPrincipal.getUser();
        if(user.getTipo().equals("interno")){
            atividade = repository.findByIdAndUserId(Long.parseLong(id), user.getId());
        }else {
            atividade = externalApi.getAtividade(id);
        }

        if(atividade == null) throw new Exception(this.NOT_FOUND_EXCEPTION_STRING);
        return atividade;
    }
     
    public AtividadeEntity createAtividade(AtividadeDTO dto) throws Exception {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
          .getAuthentication();
        MyUserPrincipal userPrincipal = (MyUserPrincipal) auth.getPrincipal();
      
        TipoAtividade tipoAtividade = new TipoAtividade();
        tipoAtividade.setTitulo(dto.type);
        tService.createTipoAtividade(tipoAtividade);
        
        AtividadeEntity atividade = new AtividadeEntity();
        atividade.setDescricao(dto.description);
        atividade.setTitulo(dto.title);
        atividade.setTipo(tipoAtividade.getId().toString());
        
        User user = userPrincipal.getUser();
        if(user.getTipo().equals("interno")){
            atividade.setUserId(user.getId());
            atividade = repository.save(atividade);        
            if(atividade == null) throw new Exception("Erro ao criar atividade");
            return atividade;
        }
        else{
            return externalApi.createAtividade(dto);
        }
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