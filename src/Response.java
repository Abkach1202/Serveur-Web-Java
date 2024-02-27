import java.io.*;

// Interface représentant une reponse à une requête http
public interface Response {
  /**
   * Elle permet d'avoir le type MIME du fichier passée en paramètre en fonction de son nom
   * @param fileName Le nom du fichier dont on veut avoir le type MIME
   * @return Le type MIME du fichier en fonction de son extension
   */
  private static String getMimeType(String fileName) {
    int index = fileName.lastIndexOf('.');
    String extension = fileName.substring(index + 1);

    // Renvoie le mime en fonction du nom de fichier
    switch (extension) {
      case "html":
        return "text/html";
      case "css":
        return "text/css";
      case "js":
        return "text/javascript";
      default:
        return "text/plain";
    }
  }
  
  /**
   * Elle permet d'avoir la bonne instance de l'interface Response en fonction de
   * l'existance du fichier et de son extension
   * 
   * @param fileName
   * @return
   */
  public static Response getResponse(String fileName) {
    if (fileName == "") {
      fileName = "index.html";
    }
    File file = new File("html/" + fileName);
    if (!file.exists()) {
      return Error404.getInstance();
    }
    return new TextFile(file, getMimeType(fileName));
  }

  /**
   * Cette fonction donne une reponse au requête en écrivant au PrintWriter
   * 
   * @param p le PrintWriter par leqeuel on envoie le message
   */
  public void respond(PrintWriter p);
}
