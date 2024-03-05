import java.io.*;

// Classe permettant de répondre à une requête de fichier HTML
public class HTMLResponse implements Response {
  // Le fichier à envoyer au client
  private File HTMLFile;
  // Le corps de la réponse
  private TextBody body;

  /**
   * Constructeur de la classe
   * 
   * @param path le chemin vers le fichier à envoyer au client
   */
  public HTMLResponse(String path) {
    this.HTMLFile = new File(path);
    this.body = new TextBody(HTMLFile);
  }

  @Override
  public void respond(OutputStream o) {
    System.out.println("Sending " + HTMLFile.getName() + " to the client...");
    PrintWriter sender = new PrintWriter(o, true);
    // Envoie de l'entête HTTP
    sender.println("HTTP/1.1 200 OK");
    sender.println("Content-Type: text/html");
    sender.println("Content-Length: " + HTMLFile.length());
    sender.println("Connection: keep-alive");
    sender.println("Cache-Control: s-maxage=300, public, max-age=0");
    sender.println();
    // Délégation de l'envoi du contenu du fichier
    body.respond(o);
  }

}
