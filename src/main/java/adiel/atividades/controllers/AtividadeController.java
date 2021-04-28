package adiel.atividades.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
import adiel.atividades.entities.AtividadeEntity;
import adiel.atividades.entities.User;
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
    System.out.println("Getting all atividades");
    UsernamePasswordAuthenticationToken auth = 
      (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    MyUserPrincipal userPrincipal = (MyUserPrincipal)  auth.getPrincipal();

    System.out.println("\t\t -> auth: "+ auth.toString());
    
    List<AtividadeEntity> atividades = service.getAllAtividades(userPrincipal.getId());
    
    return new ResponseEntity<List<AtividadeEntity>> (atividades, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AtividadeEntity> getAtividade(@PathVariable("id") Long id) {
    AtividadeEntity atividade = null;
    try {
      atividade = service.getAtividadeById(id);
  
      return new ResponseEntity<AtividadeEntity> (atividade, HttpStatus.OK);
    }catch(Exception error){
      return new ResponseEntity<AtividadeEntity> (atividade, HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping
  public ResponseEntity<AtividadeEntity> createAtividade(@RequestBody() AtividadeEntity atividade) throws Exception{
    System.out.println("\n\nAtividadeE: "+ atividade.toString() + "\n\n" ) ;

    UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    MyUserPrincipal userPrincipal = (MyUserPrincipal)  auth.getPrincipal();

    System.out.println("\t\t -> auth: "+ auth.toString());
    atividade.setUserId(userPrincipal.getId());
    AtividadeEntity updatedAtividade = service.createAtividade(atividade);


    return new ResponseEntity<AtividadeEntity> (updatedAtividade, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AtividadeEntity> updateAtividade(@RequestBody() AtividadeEntity atividade, @PathVariable("id") Long id) throws Exception{
   System.out.println("\n\nAtividadeE: "+ atividade.toString() + "\n\n" ) ;

    AtividadeEntity updatedAtividade = service.updateAtividade(atividade, id);

    return new ResponseEntity<AtividadeEntity> (updatedAtividade, HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
    public HttpStatus deleteEmployeeById(@PathVariable("id") Long id) throws Exception {
        service.deleteAtividadeById(id);
        return HttpStatus.OK;
    }

}
