import java.io.*;
import java.util.Scanner;

// Classe permettant de répondre à une requête de fichier texte
public class TextFile implements Response {
  // Le fichier à envoyer au client
  private File file;
  // Le type MIME du fichier
  private String mime;

  /**
   * Constructeur de la classe
   * 
   * @param file le fichier à envoyer au serveur
   * @param mime le type mime du fichier à envoyer au serveur
   */
  public TextFile(File file, String mime) {
    this.file = file;
    this.mime = mime;
  }

  @Override
  public void respond(PrintWriter p) {
    System.out.println("Sending " + file.getPath() + "...");
    
    try {
      Scanner scanner = new Scanner(file);

      // Envoie de l'entête HTTP
      p.println("HTTP/1.1 200 OK");
      p.println("Content-Type: " + mime);
      p.println("Content-Length: " + file.length());
      p.println();

      // Envoie du contenu du fichier
      while (scanner.hasNextLine()) {
        p.println(scanner.nextLine());
      }

      // Fermeture du scanner
      scanner.close();
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage() + System.lineSeparator() + e.getStackTrace());
    }
  }

}
