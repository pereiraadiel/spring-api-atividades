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
import adiel.atividades.dtos.AtividadeResponseDTO;
import adiel.atividades.dtos.UpdateAtividadeDTO;
import adiel.atividades.entities.AtividadeEntity;
import adiel.atividades.entities.TipoAtividade;
import adiel.atividades.entities.User;
import adiel.atividades.exceptions.NotFoundException;
import adiel.atividades.exceptions.UserAlreadyExistsException;
import adiel.atividades.repositories.AtividadeRepository;
import adiel.atividades.repositories.TipoAtividadeRepository;

 
@Service
public class AtividadesService {
     
    @Autowired
    AtividadeRepository repository;

    @Autowired
    TipoAtividadeRepository tRepository;
    
    @Autowired
    TipoAtividadeService tService;

    @Autowired
    ExternalAtividadeAPI externalApi;

    private final String NOT_FOUND_EXCEPTION_STRING = "Atividade não encontrada";

    public List<AtividadeResponseDTO> getAllAtividades(User user) throws NotFoundException{
        List<AtividadeEntity> atividades = new ArrayList<>();
        if(user.getTipo().equals("interno")){
            atividades = repository.findAllByUserId(user.getId());
        }else {
            return externalApi.getAllAtividades(user.getId());
        }
        List<AtividadeResponseDTO> responseDTO = new ArrayList<>();
        for (AtividadeEntity atividade: atividades){
            AtividadeResponseDTO  aDto = new AtividadeResponseDTO();
            aDto.description = atividade.getDescricao();
            aDto.title = atividade.getTitulo();
            aDto.type = atividade.getTipo();
            aDto.id = atividade.getId().toString();
            aDto.userId = user.getId().toString();
            responseDTO.add(aDto);
        }
        return responseDTO;
    }
     
    public AtividadeResponseDTO getAtividadeById(String id, User user) throws NotFoundException {
        AtividadeEntity atividade = null;
        if(user.getTipo().equals("interno")){
            atividade = repository.findByIdAndUserId(Long.parseLong(id), user.getId());
        }else {
            atividade = externalApi.getAtividade(id);
        }

        if(atividade == null) throw new NotFoundException(this.NOT_FOUND_EXCEPTION_STRING);
        AtividadeResponseDTO aDto = new AtividadeResponseDTO();
        aDto.description = atividade.getDescricao();
        aDto.title = atividade.getTitulo();
        aDto.type = atividade.getTipo();
        aDto.id = atividade.getId().toString();
        aDto.userId = user.getId().toString();
        return aDto;
    }
     
    public AtividadeResponseDTO createAtividade(AtividadeDTO dto, User user) throws UserAlreadyExistsException, Exception {
        TipoAtividade tipoAtividade = tRepository.findByTitulo(dto.type);
        if(tipoAtividade == null) throw new Exception("Tipo de atividade não registrado!");
        
        AtividadeEntity atividade = new AtividadeEntity();
        atividade.setDescricao(dto.description);
        atividade.setTitulo(dto.title);
        atividade.setTipo(tipoAtividade.getId());

        AtividadeResponseDTO aDto = null;
        
        if(user.getTipo().equals("interno")){
            atividade.setUserId(user.getId());
            atividade = repository.save(atividade);  
            aDto = new AtividadeResponseDTO();      
            if(atividade == null) throw new UserAlreadyExistsException("Erro ao criar atividade");
        }
        else{
            aDto = externalApi.createAtividade(dto, user.getId());
        }

        if(aDto == null) {
            throw new Exception("API indisponivel: Atividade nao pode ser criada!");
        }
        if(aDto.description == null) {
            aDto.description = atividade.getDescricao();
            aDto.title = atividade.getTitulo();
            aDto.type = atividade.getTipo();
            aDto.id = atividade.getId().toString();
            aDto.userId = user.getId().toString();
        }
        return aDto;
    } 

    public AtividadeResponseDTO updateAtividade(UpdateAtividadeDTO dto, User user) throws NotFoundException, UserAlreadyExistsException {
        TipoAtividade tipoAtividade = new TipoAtividade();
        tipoAtividade.setTitulo(dto.type);
        tService.createTipoAtividade(tipoAtividade);

        AtividadeResponseDTO responseDTO = new AtividadeResponseDTO();
        AtividadeEntity atividade = new AtividadeEntity();
        if(user.getTipo().equals("interno")){
            atividade = repository.findByIdAndUserId(Long.parseLong(dto.id), user.getId());
            if(atividade == null) throw new NotFoundException(this.NOT_FOUND_EXCEPTION_STRING);
            
            atividade.setDescricao(dto.description);
            atividade.setTitulo(dto.title);
            atividade.setTipo(dto.type);
            atividade.setUpdatedAt(new Date(System.currentTimeMillis()));
            atividade.setUserId(user.getId());
            atividade = repository.save(atividade);  
        }
        else{
            atividade = externalApi.updateAtividade(dto);
        }
        System.out.println(atividade.toString());
        responseDTO.description = atividade.getDescricao();
        responseDTO.id = dto.id;
        responseDTO.title = atividade.getTitulo();
        responseDTO.type = atividade.getTipo();
        responseDTO.userId = user.getId().toString();
        return responseDTO;
    } 
     
    public Object deleteAtividadeById(String id, User user) throws NotFoundException {
        if(user.getTipo().equals("interno")){
            AtividadeEntity atividade = repository.findByIdAndUserId(Long.parseLong(id), user.getId());
            if(atividade == null)  throw new NotFoundException(this.NOT_FOUND_EXCEPTION_STRING);
            repository.delete(atividade);
        }
        else{
            externalApi.deleteAtividade(id);
        }
        return id;
    } 
}