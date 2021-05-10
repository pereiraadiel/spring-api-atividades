package adiel.atividades.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import adiel.atividades.dtos.AtividadeDTO;
import adiel.atividades.dtos.AtividadeResponseDTO;
import adiel.atividades.dtos.UpdateAtividadeDTO;
import adiel.atividades.dtos.UpdateAtividadeResponseDTO;
import adiel.atividades.entities.AtividadeEntity;
import adiel.atividades.entities.ExternalAtividade;

@Service
public class ExternalAtividadeAPI {
  private final RestTemplate restTemplate;

  @Value("${external.url}")
  private String externalUrl;

  @Value("${external.api.key}")
  private String externalApiKey;

  public ExternalAtividadeAPI(RestTemplateBuilder restTemplateBuilder) {
      this.restTemplate = restTemplateBuilder.build();
  }

  /** ------- INDEX ------------ */
  public List<AtividadeResponseDTO> getAllAtividades(Long userId) throws Exception{
    MultiValueMap<String, String> headers = new HttpHeaders();
    headers.add("x-api-key", externalApiKey);
    System.out.println(headers.get("x-api-key").toString());

    HttpEntity<String> httpEntity = new HttpEntity<String>("all", headers);
    System.out.println(httpEntity.toString());
    
    ResponseEntity<ExternalAtividade[]> responseExternal = 
      this.restTemplate.exchange(
        externalUrl+"/todo", 
        HttpMethod.GET,
        httpEntity,
        ExternalAtividade[].class
      );

    if(responseExternal.getStatusCode() == HttpStatus.OK){

      ExternalAtividade[] externalAtividades = responseExternal.getBody();
      List<AtividadeResponseDTO> atividades = new ArrayList<>();
      for(ExternalAtividade externalAtividade: externalAtividades) {
        AtividadeResponseDTO atividade = new AtividadeResponseDTO();
        atividade.title = externalAtividade.getTitle();
        atividade.description = externalAtividade.getDescription();
        atividade.type = externalAtividade.getType();
        atividade.id = externalAtividade.getId();
        atividade.userId = userId.toString();
        atividades.add(atividade);
      }

      return atividades;
    }
    throw new Exception("Atividade não encontrada");
  }

  /** ------- SHOW ------------ */
  public AtividadeEntity getAtividade(String id) throws Exception {
    MultiValueMap<String, String> headers = new HttpHeaders();
    headers.add("x-api-key", externalApiKey);
    System.out.println(headers.get("x-api-key").toString());
    
    HttpEntity<String> httpEntity = new HttpEntity<String>(id, headers);
    System.out.println(httpEntity.toString());
    
    ResponseEntity<ExternalAtividade> responseExternal = 
      this.restTemplate.exchange(
        externalUrl+"/todo/{id}", 
        HttpMethod.GET,
        httpEntity,
        ExternalAtividade.class, 
        id
      );

    if(responseExternal.getStatusCode() == HttpStatus.OK){

      ExternalAtividade externalAtividade = responseExternal.getBody();

      //this.restTemplate.getForObject( externalUrl+"/todo/{id}", ExternalAtividade.class, id);
      AtividadeEntity atividade = new AtividadeEntity();
      atividade.setTitulo(externalAtividade.getTitle());
      atividade.setDescricao(externalAtividade.getDescription());
      atividade.setTipo(externalAtividade.getType());
      atividade.setId((long) 0);
      return atividade;
    }
    throw new Exception("Atividade não encontrada");
    
  }

  /** ------- CREATE ------------ */
  public AtividadeResponseDTO createAtividade(AtividadeDTO entity, Long userId) throws Exception {
    MultiValueMap<String, String> headers = new HttpHeaders();
    headers.add("x-api-key", externalApiKey);
    System.out.println(headers.get("x-api-key").toString());

    HttpEntity<AtividadeDTO> httpEntity = new HttpEntity<AtividadeDTO>(entity, headers);
    System.out.println(httpEntity.toString());
    
    ResponseEntity<ExternalAtividade> responseEntity = 
      this.restTemplate.postForEntity(externalUrl+"/todo", httpEntity, ExternalAtividade.class);
    
    if(responseEntity == null) throw new Exception("Erro ao criar tipo de atividade");
    
    ExternalAtividade eAtividade = responseEntity.getBody();

    AtividadeResponseDTO aDto = new AtividadeResponseDTO();
    aDto.description = eAtividade.getDescription();
    aDto.title = eAtividade.getTitle();
    aDto.type = eAtividade.getType();
    aDto.id = eAtividade.getId().toString();
    aDto.userId = userId.toString();
    return aDto;
  } 

  /** ------- UPDATE ------------ */
  public AtividadeEntity updateAtividade(UpdateAtividadeDTO dto) throws Exception{
    MultiValueMap<String, String> headers = new HttpHeaders();
    headers.add("x-api-key", externalApiKey);
    System.out.println(headers.get("x-api-key").toString());

    HttpEntity<UpdateAtividadeDTO> httpEntity = new HttpEntity<UpdateAtividadeDTO>(dto, headers);
    System.out.println(httpEntity.toString());
    
    ResponseEntity<UpdateAtividadeResponseDTO> responseExternal = 
      this.restTemplate.exchange(
        externalUrl+"/todo", 
        HttpMethod.PUT,
        httpEntity,
        UpdateAtividadeResponseDTO.class
      );
    
    if(responseExternal.getStatusCode() != HttpStatus.OK) throw new Exception("Erro ao criar tipo de atividade");
    
    AtividadeEntity atividadeEntity = new AtividadeEntity();
    atividadeEntity.setTipo(dto.type);
    atividadeEntity.setTitulo(dto.title);
    atividadeEntity.setDescricao(dto.description);
    return atividadeEntity;
  }

  /** ------- DELETE ------------ 
   * @return */
  public void deleteAtividade(String id) throws Exception {
    MultiValueMap<String, String> headers = new HttpHeaders();
    headers.add("x-api-key", externalApiKey);
    System.out.println(headers.get("x-api-key").toString());

    HttpEntity<String> httpEntity = new HttpEntity<String>("delete", headers);
    System.out.println(httpEntity.toString());
    
    ResponseEntity<Object> responseExternal = 
      this.restTemplate.exchange(
        externalUrl+"/todo/{id}", 
        HttpMethod.DELETE,
        httpEntity,
        Object.class,
        id
      );

    if(responseExternal.getStatusCode() != HttpStatus.OK)
      throw new Exception("Atividade não encontrada");

  }

  /**
   * @return String return the externalUrl
   */
  public String getExternalUrl() {
      return externalUrl;
  }

  /**
   * @param externalUrl the externalUrl to set
   */
  public void setExternalUrl(String externalUrl) {
      this.externalUrl = externalUrl;
  }

}
