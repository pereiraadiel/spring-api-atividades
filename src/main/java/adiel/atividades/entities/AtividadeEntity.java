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

  @Column(name = "user_id")
  private Long userId;

  public Long getId() {
    return this.id;
  }

  public String getTitulo() {
    return this.titulo;
  }

  public Long getUserId(){
    return this.userId;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public Date getCreatedAt() {
    return this.createdAt;
  }

  public Date getUpdatedAt() {
    return this.updatedAt;
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

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public String toString() {
    return "AtividadeEntity [id=" + id + ", titulo=" + titulo + ", descricao=" + descricao + "]";
  }

  public AtividadeEntity() {
    this.createdAt = new Date(System.currentTimeMillis());
    this.updatedAt = new Date(System.currentTimeMillis());
  }
  
}
