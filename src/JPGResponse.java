import java.io.*;

// Classe permettant de répondre à une requête de fichier image
public class JPGResponse implements Response {
  // L'image à envoyer au client
  private File JPGFile;
  // Le corps de la réponse
  private ImageBody body;

  /**
   * Constructeur de la classe
   * 
   * @param path le chemin vers l'image à envoyer au client
   */
  public JPGResponse(String path) {
    this.JPGFile = new File(path);
    this.body = new ImageBody(JPGFile, "jpeg");
  }

  @Override
  public void respond(OutputStream o) {
    System.out.println("Sending " + JPGFile.getName() + " to the client...");
    PrintWriter sender = new PrintWriter(o, true);
    // Envoie de l'entête HTTP
    sender.println("HTTP/1.1 200 OK");
    sender.println("Content-Type: image/jpeg");
    sender.println("Connection: keep-alive");
    sender.println("Cache-Control: s-maxage=300, public, max-age=0");
    sender.println();
    // Délégation de l'envoi du contenu de l'image
    body.respond(o);
  }
}
