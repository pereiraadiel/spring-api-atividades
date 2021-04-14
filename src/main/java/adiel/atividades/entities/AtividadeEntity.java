package adiel.atividades.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_atividades")
public class AtividadeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "titulo")
  private String titulo;

  @Column(name = "descricao")
  private String descricao;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "updated_at")
  private Date updatedAt;

  public Long getId() {
    return this.id;
  }

  public String getTitulo() {
    return this.titulo;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  @Override
  public String toString() {
    return "AtividadeEntity [id=" + id + ", titulo=" + titulo + ", descricao=" + descricao + "]";
  }

  // public AtividadeEntity() {
  //   this.id = (long) Math.random();
  //   this.createdAt = new Date(System.currentTimeMillis());
  //   this.updatedAt = new Date(System.currentTimeMillis());
  // }
  
}
