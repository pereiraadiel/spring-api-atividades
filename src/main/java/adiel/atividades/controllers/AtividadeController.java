package adiel.atividades.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import adiel.atividades.dtos.AtividadeDTO;
import adiel.atividades.dtos.AtividadeResponseDTO;
import adiel.atividades.dtos.UpdateAtividadeDTO;
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
  public ResponseEntity<List<AtividadeResponseDTO>> getAllAtividades() {
    List<AtividadeResponseDTO> atividades = new ArrayList<>();
    try {
      atividades = service.getAllAtividades();
      return new ResponseEntity<List<AtividadeResponseDTO>>(atividades, HttpStatus.OK);

    } catch (Exception error) {
      System.out.println(error.getMessage());
      return new ResponseEntity<List<AtividadeResponseDTO>>(atividades, HttpStatus.NOT_FOUND);

    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getAtividade(@PathVariable("id") String id) {
    AtividadeResponseDTO atividade = null;
    try {
      atividade = service.getAtividadeById(id);
      System.out.print(atividade.toString());

      return new ResponseEntity<Object>(atividade, HttpStatus.OK);
    } catch (Exception error) {
      String notfound = "Not found!";
      return new ResponseEntity<Object>(notfound, HttpStatus.NOT_FOUND);

    }
  }

  @PostMapping
  public ResponseEntity<Object> createAtividade(@RequestBody() AtividadeDTO atividadeDTO) throws Exception {
    try {
      AtividadeResponseDTO updatedAtividade = service.createAtividade(atividadeDTO);
      return new ResponseEntity<Object>(updatedAtividade, HttpStatus.CREATED);

    } catch (Exception error) {
      System.out.println(error.getMessage());
      return new ResponseEntity<Object>(error.getMessage(), HttpStatus.BAD_REQUEST);
      
    }
  }

  @PutMapping("/{id}") // atualizar atividade na api interna
  public ResponseEntity<Object> updateAtividade(@RequestBody() UpdateAtividadeDTO dto, @PathVariable("id") String id)
      throws Exception {
    try {
      dto.id = id;
      AtividadeResponseDTO updatedAtividade = service.updateAtividade(dto);
      return new ResponseEntity<Object>(updatedAtividade, HttpStatus.OK);

    } catch (Exception error) {
      System.out.println(error.toString());
      String notfound = "Not found!";
      return new ResponseEntity<Object>(notfound, HttpStatus.NOT_FOUND);

    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteEmployeeById(@PathVariable("id") String id) throws Exception {
    try {
      service.deleteAtividadeById(id);
      return new ResponseEntity<Object>(null, HttpStatus.OK);
    } catch (Exception error) {
      String notfound = "Not found!";
      return new ResponseEntity<Object>(notfound, HttpStatus.NOT_FOUND);
    }
  }

}
