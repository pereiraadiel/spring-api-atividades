package adiel.atividades.exceptions;

public class NotFoundException extends Exception {
  public String message;

  public NotFoundException(String message){
    this.message = message;
  }

  public NotFoundException(){
    this.message = "Ocorreu um erro desconhecido";
  }

  @Override
  public String toString() {
    return this.message;
  }
}
