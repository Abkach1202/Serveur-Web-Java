import java.io.*;
import java.util.Scanner;

// Classe permettant de répondre à une requête de fichier texte
public class TextFile implements Response {
  // Le fichier à envoyer au client
  private File textFile;
  // Le type MIME du fichier
  private String mime;

  /**
   * Constructeur de la classe
   * 
   * @param textFile le chemin vers le fichier à envoyer au client
   * @param mime     le type mime du fichier à envoyer au client
   */
  public TextFile(String textFile, String mime) {
    this.textFile = new File(textFile);
    this.mime = mime;
  }

  @Override
  public void respond(OutputStream o) {
    System.out.println("Sending a text file with " + mime + " MIME type...");
    PrintWriter sender = new PrintWriter(o, true);
    try {
      Scanner scanner = new Scanner(textFile);
      // Envoie de l'entête HTTP
      sender.println("HTTP/1.1 200 OK");
      sender.println("Content-Type: " + mime);
      sender.println("Content-Length: " + textFile.length());
      sender.println();
      // Envoie du contenu du fichier
      while (scanner.hasNextLine()) {
        sender.println(scanner.nextLine());
      }
      // Fermeture du scanner et du PrintWriter
      sender.close();
      scanner.close();
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

}
