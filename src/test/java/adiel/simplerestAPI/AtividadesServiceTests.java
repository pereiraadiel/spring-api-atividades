package adiel.simplerestAPI;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import adiel.atividades.services.AtividadesService;
import adiel.atividades.services.ExternalAtividadeAPI;
import adiel.atividades.services.TipoAtividadeService;

// @ExtendWith(MockitoException.class)
public class AtividadesServiceTests {
  @Mock
  private AtividadeRepository repository;

  @Mock
  private TipoAtividadeRepository tRepository;

  @Mock
  private TipoAtividadeService tService;

  @Mock
  private ExternalAtividadeAPI eAtividadeAPI;

  @InjectMocks
  private AtividadesService service;

  private ExpectedException expectException;

  public AtividadesServiceTests() {
    expectException = ExpectedException.none();
  }

  @Before
  public void initMocks() {
    MockitoAnnotations.openMocks(this);
  }


  // ===============================
  // TESTES USUÁRIO INTERNO
  // ===============================
  @Test
  public void deveCriarUmaAtividadeNoBanco() throws UserAlreadyExistsException, Exception {
    TipoAtividade tipoAtividade = new TipoAtividade();
    tipoAtividade.setTitulo("Profissional");
    when(tRepository.findByTitulo(anyString())).thenReturn(tipoAtividade);

    AtividadeEntity atividade = new AtividadeEntity();
    atividade.setId((long) 1);
    atividade.setDescricao("descricao");
    atividade.setTitulo("titulo");
    atividade.setTipo("Profissional");
    atividade.setUserId((long) 1);
    AtividadeEntity expectedResponse = atividade;
    when(repository.save(any())).thenReturn(expectedResponse);

    User user = new User();
    user.setId((long) 1);
    user.setTipo("interno");
    user.setUsername("username");
    user.setPassword("password");

    AtividadeDTO dto = new AtividadeDTO();
    dto.description = atividade.getDescricao();
    dto.title = atividade.getTitulo();
    dto.type = atividade.getTipo();

    AtividadeResponseDTO response = service.createAtividade(dto, user);
    assertEquals(expectedResponse.getDescricao(), response.description);
    assertEquals(expectedResponse.getTitulo(), response.title);
    assertEquals(expectedResponse.getTipo(), response.type);
  }

  @Test
  public void deveAtualizarUmaAtividadeNoBanco() throws NotFoundException, UserAlreadyExistsException {
    AtividadeEntity atividade = new AtividadeEntity();
    atividade.setId((long) 1);
    atividade.setDescricao("descricao");
    atividade.setTitulo("titulo");
    atividade.setTipo("Profissional");
    atividade.setUserId((long) 1);
    AtividadeEntity expectedResponse = atividade;
    when(repository.save(any())).thenReturn(expectedResponse);
    when(repository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(expectedResponse);

    User user = new User();
    user.setId(atividade.getUserId());
    user.setTipo("interno");
    user.setUsername("username");
    user.setPassword("password");

    UpdateAtividadeDTO dto = new UpdateAtividadeDTO();
    dto.id = "1";
    dto.title="titulo";
    dto.type="Pessoal";
    dto.description="Descricao Nova";
    AtividadeResponseDTO response = service.updateAtividade(dto, user);
    assertEquals(dto.description, response.description);
    assertEquals(dto.title, response.title);
    assertEquals(dto.type, response.type);
  }

  @Test
  public void deveDeletarUmaAtividadeDoBanco() throws NotFoundException {
    AtividadeEntity atividade = new AtividadeEntity();
    atividade.setId((long) 1);
    atividade.setDescricao("descrição");
    atividade.setTitulo("titulo");
    atividade.setTipo("Profissional");
    atividade.setUserId((long) 1);
    doNothing().when(repository).delete(atividade);
    when(repository.findByIdAndUserId(any(), any())).thenReturn(atividade);
    
    User user = new User();
    user.setId(atividade.getUserId());
    user.setTipo("interno");
    user.setUsername("username");
    user.setPassword("password");
    
    // expectException.expect(NotFoundException.class);
    service.deleteAtividadeById(atividade.getId().toString(), user);

  }

  @Test
  public void deveRetornarUmaAtividadePeloSeuIdDoBanco() throws NotFoundException{
    AtividadeEntity atividade = new AtividadeEntity();
    atividade.setId((long) 1);
    atividade.setDescricao("descrição");
    atividade.setTitulo("titulo");
    atividade.setTipo("Profissional");
    atividade.setUserId((long) 1);
    when(repository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(atividade);
    
    User user = new User();
    user.setId(atividade.getUserId());
    user.setTipo("interno");
    user.setUsername("username");
    user.setPassword("password");

    expectException.expect(NotFoundException.class);
    service.getAtividadeById(atividade.getId().toString(), user);
  }

  @Test
  public void deveRetornarTodasAtividadesDoUsuarioDoBanco() throws NotFoundException{
    AtividadeEntity atividade = new AtividadeEntity();
    atividade.setId((long) 1);
    atividade.setDescricao("descrição");
    atividade.setTitulo("titulo");
    atividade.setTipo("Profissional");
    atividade.setUserId((long) 1);

    List<AtividadeEntity> atividades = new ArrayList<>();
    atividades.add(atividade);

    when(repository.findAllByUserId(atividade.getUserId())).thenReturn(atividades);
    
    User user = new User();
    user.setId(atividade.getUserId());
    user.setTipo("interno");
    user.setUsername("username");
    user.setPassword("password");

    service.getAllAtividades(user);
  }

  // ===============================
  // TESTES USUÁRIO EXTERNO
  // ==============================
  
  @Test
  public void deveCriarUmaAtividadeNaAPI() throws UserAlreadyExistsException, Exception {
    TipoAtividade tipoAtividade = new TipoAtividade();
    tipoAtividade.setTitulo("Profissional");
    when(tRepository.findByTitulo(anyString())).thenReturn(tipoAtividade);
  
    AtividadeEntity atividade = new AtividadeEntity();
    atividade.setId((long) 1);
    atividade.setDescricao("descricao");
    atividade.setTitulo("titulo");
    atividade.setTipo("Profissional");
    atividade.setUserId((long) 1);
    AtividadeEntity expectedResponse = atividade;

    AtividadeDTO aDto = new AtividadeDTO();
    aDto.description = atividade.getDescricao();
    aDto.title = atividade.getTitulo();
    aDto.type = atividade.getTipo();

    AtividadeResponseDTO response = new AtividadeResponseDTO();
    response.description = aDto.description;
    response.title = aDto.title;
    response.type = aDto.type;

    when(repository.save(any())).thenReturn(expectedResponse);
    when(eAtividadeAPI.createAtividade(any(), anyLong())).thenReturn(response);
  
    User user = new User();
    user.setId(atividade.getUserId());
    user.setTipo("externo");
    user.setUsername("username");
    user.setPassword("password");
  
    AtividadeDTO dto = new AtividadeDTO();
    dto.description = atividade.getDescricao();
    dto.title = atividade.getTitulo();
    dto.type = atividade.getTipo();
  
    response = service.createAtividade(dto, user);
    assertEquals(dto.description, response.description);
    assertEquals(dto.title, response.title);
    assertEquals(dto.type, response.type);
  }

  @Test
  public void deveAtualizarUmaAtividadeNaAPI() throws NotFoundException, UserAlreadyExistsException {
    AtividadeEntity atividade = new AtividadeEntity();
    atividade.setId((long) 1);
    atividade.setDescricao("descricao");
    atividade.setTitulo("titulo");
    atividade.setTipo("Profissional");
    atividade.setUserId((long) 1);
    AtividadeEntity expectedResponse = atividade;
    expectedResponse.setDescricao("Descricao Nova");
    expectedResponse.setTipo("Pessoal");

    AtividadeResponseDTO response = new AtividadeResponseDTO();

    when(repository.save(any())).thenReturn(expectedResponse);
    when(repository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(expectedResponse);
    when(eAtividadeAPI.updateAtividade(any())).thenReturn(atividade);
  
    User user = new User();
    user.setId(atividade.getUserId());
    user.setTipo("externo");
    user.setUsername("username");
    user.setPassword("password");

    UpdateAtividadeDTO dto = new UpdateAtividadeDTO();
    dto.id = "1";
    dto.title="titulo";
    dto.type="Pessoal";
    dto.description="Descricao Nova";
    response = service.updateAtividade(dto, user);
    assertEquals(dto.description, response.description);
    assertEquals(dto.title, response.title);
    assertEquals(dto.type, response.type);
  }

  @Test
  public void deveDeletarUmaAtividadeDaAPI() throws NotFoundException {
    AtividadeEntity atividade = new AtividadeEntity();
    atividade.setId((long) 1);
    atividade.setDescricao("descrição");
    atividade.setTitulo("titulo");
    atividade.setTipo("Profissional");
    atividade.setUserId((long) 1);
    doNothing().when(repository).delete(atividade);
    when(repository.findByIdAndUserId(any(), any())).thenReturn(atividade);
    
    User user = new User();
    user.setId(atividade.getUserId());
    user.setTipo("externo");
    user.setUsername("username");
    user.setPassword("password");
    
    // expectException.expect(NotFoundException.class);
    service.deleteAtividadeById(atividade.getId().toString(), user);

  }

  @Test
  public void deveRetornarUmaAtividadePeloSeuIdDaAPI() throws NotFoundException{
    AtividadeEntity atividade = new AtividadeEntity();
    atividade.setId((long) 1);
    atividade.setDescricao("descrição");
    atividade.setTitulo("titulo");
    atividade.setTipo("Profissional");
    atividade.setUserId((long) 1);
    when(repository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(atividade);
    when(eAtividadeAPI.getAtividade(anyString())).thenReturn(atividade);
  
    User user = new User();
    user.setId(atividade.getUserId());
    user.setTipo("externo");
    user.setUsername("username");
    user.setPassword("password");

    expectException.expect(NotFoundException.class);
    service.getAtividadeById(atividade.getId().toString(), user);
  }

  @Test
  public void deveRetornarTodasAtividadesDoUsuarioDaAPI() throws NotFoundException{
    AtividadeEntity atividade = new AtividadeEntity();
    atividade.setId((long) 1);
    atividade.setDescricao("descrição");
    atividade.setTitulo("titulo");
    atividade.setTipo("Profissional");
    atividade.setUserId((long) 1);

    List<AtividadeEntity> atividades = new ArrayList<>();
    atividades.add(atividade);

    when(repository.findAllByUserId(atividade.getUserId())).thenReturn(atividades);
    
    User user = new User();
    user.setId(atividade.getUserId());
    user.setTipo("externo");
    user.setUsername("username");
    user.setPassword("password");

    service.getAllAtividades(user);
  }

}

