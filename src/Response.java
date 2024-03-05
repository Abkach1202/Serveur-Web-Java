import java.io.*;
import java.nio.file.*;

// Interface représentant une reponse à une requête http
public interface Response {
  /**
   * Elle permet de savoir si le fichier n'existe pas
   * 
   * @param path le chemin vers le fichier
   * @return true si le fichier n'existe pas, false sinon
   */
  private static boolean isfileNotFound(String path) {
    Path file = Paths.get(path);
    return !Files.exists(file) && !Files.isDirectory(file);
  }

  /**
   * Elle permet d'avoir la bonne instance de l'interface Response en fonction de
   * l'existance du fichier et de son extension
   * 
   * @param path le chemin vers le fichier dont on veut avoir la reponse
   * @return Une instance de l'interface Response qui repond à la requête
   */
  public static Response getResponse(String path) {
    if (path == "") {
      path = "index.html";
    }
    path = "html/" + path;
    if (isfileNotFound(path)) {
      return Error404.getInstance();
    }
    String extension = path.substring(path.lastIndexOf('.') + 1);
    // Renvoie la bonne instance en fonction de l'extension
    switch (extension) {
      case "html":
        return new HTMLResponse(path);
      case "css":
        return new CSSResponse(path);
      case "js":
        return new JSResponse(path);
      case "png":
        return new ImageProxy(path, "png");
      case "jpg":
        return new ImageProxy(path, "jpeg");
      default:
        return new UnknownResponse(path);
    }
  }

  /**
   * Cette fonction donne une reponse au requête en écrivant au PrintWriter
   * 
   * @param o l'outputStream par leqeuel on envoie le message
   */
  public void respond(OutputStream o);
}
