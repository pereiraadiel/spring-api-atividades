package adiel.atividades.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ExternalAtividade {
  private String id;
  private String title;
  private String description;
  private String type;
}
