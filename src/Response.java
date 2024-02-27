import java.io.*;

// Interface représentant une reponse à une requête http
public interface Response {

  /**
   * Elle permet d'avoir la bonne instance de l'interface Response en fonction de
   * l'existance du fichier et de son extension
   * 
   * @param fileName
   * @return
   */
  public static Response getResponse(String fileName) {
    File file = new File(fileName);
    if (!file.exists()) {
      return Error404.getInstance();
    }
    return new FileResponse(file, "html");
  }

  /**
   * Cette fonction donne une reponse au requête en écrivant au PrintWriter
   * 
   * @param p le PrintWriter par leqeuel on envoie le message
   */
  public void respond(PrintWriter p);
}
