import java.io.*;
import java.nio.file.*;

// Interface représentant une reponse à une requête http
public interface Response {
  /**
   * Elle permet d'avoir le type MIME du fichier passée en paramètre en fonction
   * de son nom
   * 
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
      case "gif":
        return "image/gif";
      case "png":
        return "image/png";
      case "jpeg":
      case "jpg":
        return "image/jpeg";
      case "bmp":
        return "image/bmp";
      case "webp":
        return "image/webp";
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
    fileName = "html/" + fileName;
    Path file = Paths.get(fileName);
    if (!Files.exists(file) && !Files.isDirectory(file)) {
      return Error404.getInstance();
    }
    String mimeType = getMimeType(fileName);
    if (mimeType.startsWith("text")) {
      return new TextFile(fileName, mimeType);
    }
    return new ImageProxy(fileName, mimeType);
  }

  /**
   * Cette fonction donne une reponse au requête en écrivant au PrintWriter
   * 
   * @param o l'outputStream par leqeuel on envoie le message
   */
  public void respond(OutputStream o);
}
