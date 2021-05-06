package adiel.atividades.controllers;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import adiel.atividades.MyUserPrincipal;
import adiel.atividades.dtos.AtividadeDTO;
import adiel.atividades.entities.AtividadeEntity;
import adiel.atividades.services.AtividadesService;
import adiel.atividades.services.JWTService;

@RestController
@RequestMapping("/atividades")
public class AtividadeController {

  @Autowired
  AtividadesService service;

  @Autowired
  JWTService jwtService;

  @GetMapping
  public ResponseEntity<List<AtividadeEntity>> getAllAtividades() {
    List<AtividadeEntity> atividades = service.getAllAtividades();

    return new ResponseEntity<List<AtividadeEntity>>(atividades, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AtividadeEntity> getAtividade(@PathVariable("id") String id) {
    AtividadeEntity atividade = null;

    try {
      atividade = service.getAtividadeById(id);
      System.out.print(atividade.toString());

      return new ResponseEntity<AtividadeEntity>(atividade, HttpStatus.OK);
    } catch (Exception error) {
      return new ResponseEntity<AtividadeEntity>(atividade, HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping
  public ResponseEntity<Object> createAtividade(@RequestBody() AtividadeDTO atividadeDTO) throws Exception {
    try {
      AtividadeEntity updatedAtividade = service.createAtividade(atividadeDTO);
      return new ResponseEntity<Object>(updatedAtividade, HttpStatus.CREATED);

    }catch(Exception error){
      System.out.println(error.getMessage());
      return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> updateAtividade(@RequestBody()  AtividadeDTO atividadeDTO,
      @PathVariable("id") Long id) throws Exception {
    
    AtividadeEntity atividade = new AtividadeEntity();
    atividade.setDescricao(atividadeDTO.description);
    atividade.setTitulo(atividadeDTO.title);
    atividade.setTipo(atividadeDTO.type);

    UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
        .getAuthentication();
    MyUserPrincipal userPrincipal = (MyUserPrincipal) auth.getPrincipal();

    try {
      
      AtividadeEntity updatedAtividade = service.updateAtividade(atividade, id, userPrincipal.getId());
      return new ResponseEntity<Object>(updatedAtividade, HttpStatus.CREATED);
      
    }catch(Exception error){
      
      return new ResponseEntity<Object>(null, HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteEmployeeById(@PathVariable("id") Long id) throws Exception {

    UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
        .getAuthentication();
    MyUserPrincipal userPrincipal = (MyUserPrincipal) auth.getPrincipal();
    try {
      service.deleteAtividadeById(id, userPrincipal.getId());
      return new ResponseEntity<Object>(null, HttpStatus.OK);
    } catch (Exception error) {
      return new ResponseEntity<Object>(null, HttpStatus.NOT_FOUND);
    }
  }

}
