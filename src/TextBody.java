import java.io.*;
import java.util.Scanner;

// Classe permettant de répondre à une requête de fichier texte
public class TextBody implements Response {
  // Le fichier à envoyer au client
  private Scanner scanner;

  /**
   * Constructeur de la classe
   * 
   * @param file le fichier à envoyer au client
   */
  public TextBody(File file) {
    try {
      this.scanner = new Scanner(file);
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public void respond(OutputStream o) {
    PrintWriter sender = new PrintWriter(o, true);
    // Envoie du contenu du fichier
    while (scanner.hasNextLine()) {
      sender.println(scanner.nextLine());
    }
    // Fermeture du scanner et du PrintWriter
    sender.close();
    scanner.close();
  }

}
