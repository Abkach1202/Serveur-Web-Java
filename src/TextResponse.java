import java.io.*;
import java.util.Map;
import java.util.Scanner;

/**
 * Classe permettant d'envoyer un fichier texte au client
 */
public class TextResponse implements Response {
  // L'en-tête de la réponse
  private Header header;
  // Le fichier à envoyer au client
  private File file;

  /**
   * Constructeur de la classe
   * 
   * @param path le chemin vers le fichier à envoyer au client
   * @param extension l'extension du fichier
   */
  public TextResponse(String path, String extension) {
    this.file = new File(path);
    this.header = new Header(extension, this.file.length(), Response.OK);
  }

  @Override
  public void setCookie(String key, String value, int maxAge) {
    header.addCookie(key, value, maxAge);
  }

  /**
   * Envoie le contenu du fichier au client
   * 
   * @param o le flux de sortie vers le client
   */
  public void respond(OutputStream o, Map<String, String> cookies) {
    PrintWriter sender = new PrintWriter(o, true);
    try (Scanner scanner = new Scanner(this.file)) {
      // Envoie de l'entête HTTP
      sender.println(header);
      // Envoie du contenu du fichier
      while (scanner.hasNextLine()) {
        sender.println(scanner.nextLine());
      }
      // Fermeture du scanner et du PrintWriter
      sender.close();
      scanner.close();
    } catch (FileNotFoundException e) {
      System.err.println("File not found: " + this.file.getName());
    }
  }
}
