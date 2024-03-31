import java.io.*;
import java.nio.file.*;
import java.util.Map;

/**
 * Interface représentant une reponse à une requête http
 */
public interface Response {
  /**
   * Le code de retour pour OK
   */
  public static final int OK = 200;
  /**
   * Le code de retour pour NOT FOUND
   */
  public static final int NOT_FOUND = 404;

  /**
   * Le chemin par défaut des fichiers
   */
  public static final String DEFAULT_SOURCE = "html" + File.separator;

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
   * Elle permet de récupérer l'extension d'un fichier
   * 
   * @param path le chemin vers le fichier
   * @return l'extension du fichier
   */
  public static String getExtension(String path) {
    return path.substring(path.lastIndexOf('.') + 1);
  }

  /**
   * Elle permet d'avoir la bonne instance de l'interface Response en fonction de
   * l'existance du fichier et de son extension
   * 
   * @param path     le chemin vers le fichier dont on veut avoir la reponse
   * @param no_image true si on ne veut pas afficher les images
   * @param params   les paramètres de la requête
   * @return Une instance de l'interface Response qui repond à la requête
   */
  public static Response getResponse(String path, boolean no_image, Map<String, String> params) {
    // Si le fichier n'existe pas
    if (path == null || isfileNotFound(DEFAULT_SOURCE + path)) {
      return new NotFoundResponse();
    }
    if (path == "") {
      return new TextResponse(DEFAULT_SOURCE + "index.html", "html");
    }
    String extension = getExtension(path);
    // Si c'est une image
    if (extension.equals("png") || extension.equals("jpg")) {
      return new ImageProxy(DEFAULT_SOURCE + path, extension, no_image);
    }
    // Si c'est un template freeMarker
    if (extension.equals("dlb") || extension.equals("ftl")) {
      return new FreemarkerResponse(path, params);
    }
    // Si c'est un fichier texte
    return new TextResponse(DEFAULT_SOURCE + path, extension);
  }

  /**
   * Elle permet de savoir si le cookie existe
   * 
   * @param key    le nom du cookie
   * @param value  la valeur du cookie
   * @param maxAge la durée de vie du cookie
   */
  public void setCookie(String key, String value, int maxAge);

  /**
   * Cette fonction donne une reponse au requête en écrivant au PrintWriter
   * 
   * @param o       l'outputStream par leqeuel on envoie le message
   * @param cookies les cookies de la requête
   */
  public void respond(OutputStream o, Map<String, String> cookies);
}
