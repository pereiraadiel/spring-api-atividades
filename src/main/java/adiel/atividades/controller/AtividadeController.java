package adiel.atividades.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import adiel.atividades.entities.AtividadeEntity;
import adiel.atividades.services.AtividadesService;

@RestController
@RequestMapping("/atividades")
public class AtividadeController {

  @Autowired
  AtividadesService service;
  
  @GetMapping
  public ResponseEntity<List<AtividadeEntity>> getAllAtividades() {
    List<AtividadeEntity> atividades = service.getAllAtividades();
    
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
  public ResponseEntity<AtividadeEntity> createAtividade(AtividadeEntity atividade) throws Exception{
   System.out.println("\n\nAtividadeE: "+ atividade.toString() + "\n\n" ) ;

    AtividadeEntity updatedAtividade = service.createAtividade(atividade);

    return new ResponseEntity<AtividadeEntity> (updatedAtividade, HttpStatus.CREATED);
  }

  @PostMapping("/{id}")
  public ResponseEntity<AtividadeEntity> updateAtividade(AtividadeEntity atividade, @PathVariable("id") Long id) throws Exception{
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
