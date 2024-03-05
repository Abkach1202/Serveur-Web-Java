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
    if (path == null || isfileNotFound("html/" + path)) {
      return new Error404();
    }
    String extension = path.substring(path.lastIndexOf('.') + 1);
    // Renvoie la bonne instance en fonction de l'extension
    switch (extension) {
      case "html":
        return new HTMLResponse("html/" + path);
      case "css":
        return new CSSResponse("html/" + path);
      case "js":
        return new JSResponse("html/" + path);
      case "png":
        return new ImageProxy("html/" + path, "png");
      case "jpg":
        return new ImageProxy("html/" + path, "jpeg");
      default:
        return new UnknownResponse("html/" + path);
    }
  }

  /**
   * Elle permet de savoir si le cookie existe
   * 
   * @param key le nom du cookie
   * @param value la valeur du cookie
   * @param maxAge la durée de vie du cookie
   */
  public void setCookie(String key, String value, int maxAge);

  /**
   * Cette fonction donne une reponse au requête en écrivant au PrintWriter
   * 
   * @param o l'outputStream par leqeuel on envoie le message
   */
  public void respond(OutputStream o);
}
