package adiel.atividades.exceptions;

public class UserAlreadyExistsException extends Exception {
  public String message;

  public UserAlreadyExistsException(String usuario){
    this.message = "Entidade já registrada: "+ usuario;
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return message;
  }

}
