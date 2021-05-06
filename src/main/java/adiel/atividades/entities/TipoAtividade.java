package adiel.atividades.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_tipo_atividades")
public class TipoAtividade {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @Column(name = "titulo", nullable = false)
  private String titulo;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "updated_at")
  private Date updatedAt;

  public void setTitulo(String type) {
    this.titulo = type;
  }

  public String getTitulo(){
    return this.titulo;
  }

  public String getId(){
    return this.id;
  }

  @Override
  public String toString() {
    return "TipoAtividade [id=" + id + ", titulo=" + titulo + "]";
  }

  public TipoAtividade(){
    this.createdAt = new Date(System.currentTimeMillis());
    this.updatedAt = new Date(System.currentTimeMillis());
  }
}
