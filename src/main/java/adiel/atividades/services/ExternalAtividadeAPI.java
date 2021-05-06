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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import adiel.atividades.dtos.AtividadeDTO;
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

  public List<AtividadeEntity> getAllAtividades() throws Exception{
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

      ExternalAtividade externalAtividades[] = responseExternal.getBody();
      List<AtividadeEntity> atividades = new ArrayList<>();
      for(ExternalAtividade externalAtividade: externalAtividades) {

        AtividadeEntity atividade = new AtividadeEntity();
        atividade.setTitulo(externalAtividade.getTitle());
        atividade.setDescricao(externalAtividade.getDescription());
        atividade.setTipo(externalAtividade.getType());
        atividade.setId((long) 0);
        atividades.add(atividade);
      }

      //this.restTemplate.getForObject( externalUrl+"/todo/{id}", ExternalAtividade.class, id);
      return atividades;
    }
    throw new Exception("Atividade não encontrada");
  }

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

  public AtividadeEntity createAtividade(AtividadeDTO entity) throws Exception {
    MultiValueMap<String, String> headers = new HttpHeaders();
    headers.add("x-api-key", externalApiKey);
    System.out.println(headers.get("x-api-key").toString());

    HttpEntity<AtividadeDTO> httpEntity = new HttpEntity<AtividadeDTO>(entity, headers);
    System.out.println(httpEntity.toString());
    
    ResponseEntity<AtividadeEntity> responseEntity = 
      this.restTemplate.postForEntity(externalUrl+"/todo", httpEntity, AtividadeEntity.class);
    
    if(responseEntity == null) throw new Exception("Erro ao criar tipo de atividade");
    return responseEntity.getBody();
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
