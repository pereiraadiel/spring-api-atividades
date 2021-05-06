package adiel.atividades.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;

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

  public AtividadeEntity getAtividade(String id) {
    ExternalAtividade externalAtividade = 
      this.restTemplate.getForObject( externalUrl+"/todo/{id}", ExternalAtividade.class, id);
    AtividadeEntity atividade = new AtividadeEntity();
    atividade.setTitulo(externalAtividade.title);
    atividade.setDescricao(externalAtividade.description);
    atividade.setTipo(externalAtividade.type);
    atividade.setId((long) 0);
    return atividade;
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
