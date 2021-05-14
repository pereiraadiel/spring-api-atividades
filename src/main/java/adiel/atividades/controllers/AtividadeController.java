package adiel.atividades.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import adiel.atividades.dtos.AtividadeResponseDTO;
import adiel.atividades.dtos.UpdateAtividadeDTO;
import adiel.atividades.entities.AtividadeEntity;
import adiel.atividades.exceptions.NotFoundException;
import adiel.atividades.exceptions.UserAlreadyExistsException;
import adiel.atividades.services.AtividadesService;
import adiel.atividades.services.JWTService;

@RestController
@RequestMapping("/atividades")
public class AtividadeController {

  @Autowired
  AtividadesService service;

  @Autowired
  JWTService jwtService;

  private UsernamePasswordAuthenticationToken auth;
  private MyUserPrincipal userPrincipal;

  public void initializeUserPrincipal() {
    auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
    .getAuthentication();
    userPrincipal = (MyUserPrincipal) auth.getPrincipal();
  }

  @GetMapping
  public ResponseEntity<List<AtividadeResponseDTO>> getAllAtividades() {
    initializeUserPrincipal();
    List<AtividadeResponseDTO> atividades = new ArrayList<>();
    try {
      atividades = service.getAllAtividades(this.userPrincipal.getUser());
      return new ResponseEntity<List<AtividadeResponseDTO>>(atividades, HttpStatus.OK);

    } catch (NotFoundException error) {
      System.out.println(error.getMessage());
      return new ResponseEntity<List<AtividadeResponseDTO>>(atividades, HttpStatus.NOT_FOUND);

    } catch (Exception error) {
      System.out.println(error.getMessage());
      return new ResponseEntity<List<AtividadeResponseDTO>>(atividades, HttpStatus.INTERNAL_SERVER_ERROR);

    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getAtividade(@PathVariable("id") String id) {
    initializeUserPrincipal();
    AtividadeResponseDTO atividade = null;
    try {
      atividade = service.getAtividadeById(id, this.userPrincipal.getUser());
      System.out.print(atividade.toString());

      return new ResponseEntity<Object>(atividade, HttpStatus.OK);
    } catch (NotFoundException error) {
      return new ResponseEntity<Object>("Not found!", HttpStatus.NOT_FOUND);

    }catch (Exception error) {
      return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);

    }
  }

  @PostMapping
  public ResponseEntity<Object> createAtividade(@RequestBody() AtividadeDTO atividadeDTO) throws Exception {
    try {
      initializeUserPrincipal();
      AtividadeResponseDTO updatedAtividade = service.createAtividade(atividadeDTO, this.userPrincipal.getUser());
      return new ResponseEntity<Object>(updatedAtividade, HttpStatus.CREATED);

    } catch (UserAlreadyExistsException error) {
      System.out.println(error.getMessage());
      return new ResponseEntity<Object>(error.getMessage(), HttpStatus.BAD_REQUEST);
      
    } catch (Exception error) {
      System.out.println(error.getMessage());
      return new ResponseEntity<Object>(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      
    }
  }

  @PutMapping("/{id}") 
  public ResponseEntity<Object> updateAtividade(@RequestBody() UpdateAtividadeDTO dto, @PathVariable("id") String id)
      throws Exception {
    try {
      initializeUserPrincipal();
      dto.id = id;
      AtividadeResponseDTO updatedAtividade = service.updateAtividade(dto, this.userPrincipal.getUser());
      return new ResponseEntity<Object>(updatedAtividade, HttpStatus.OK);

    } catch (UserAlreadyExistsException error) {
      System.out.println(error.toString());
      return new ResponseEntity<Object>(error.getMessage(), HttpStatus.BAD_REQUEST);

    }catch (NotFoundException error) {
      System.out.println(error.toString());
      return new ResponseEntity<Object>("Not found!", HttpStatus.NOT_FOUND);

    } catch (Exception error) {
      System.out.println(error.toString());
      return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);

    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteEmployeeById(@PathVariable("id") String id) throws Exception {
    try {
      initializeUserPrincipal();
      service.deleteAtividadeById(id, this.userPrincipal.getUser());
      return new ResponseEntity<Object>(null, HttpStatus.OK);
    } catch (NotFoundException error) {
      return new ResponseEntity<Object>("Not found!", HttpStatus.NOT_FOUND);
    }catch (Exception error) {
      return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
