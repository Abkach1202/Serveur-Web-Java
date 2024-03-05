import java.io.*;
import java.util.Scanner;

// Classe permettant d'envoyer un fichier texte au client
public class FileSender {
  // Le fichier à envoyer au client
  private Scanner scanner;

  /**
   * Constructeur de la classe
   * 
   * @param file le fichier à envoyer au client
   */
  public FileSender(File file) {
    try {
      this.scanner = new Scanner(file);
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Envoie le contenu du fichier au client
   * 
   * @param o le flux de sortie vers le client
   */
  public void send(OutputStream o) {
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
