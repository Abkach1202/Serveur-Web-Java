import java.io.*;

// Classe permettant de répondre à une requête de fichier JS
public class JSResponse implements Response {
  // Le fichier à envoyer au client
  private File JSFile;
  // Le corps de la réponse
  private TextBody body;

  /**
   * Constructeur de la classe
   * 
   * @param path le chemin vers le fichier à envoyer au client
   */
  public JSResponse(String path) {
    this.JSFile = new File(path);
    this.body = new TextBody(JSFile);
  }

  @Override
  public void respond(OutputStream o) {
    System.out.println("Sending " + JSFile.getName() + " to the client...");
    PrintWriter sender = new PrintWriter(o, true);
    // Envoie de l'entête HTTP
    sender.println("HTTP/1.1 200 OK");
    sender.println("Content-Type: text/javascript");
    sender.println("Content-Length: " + JSFile.length());
    sender.println("Connection: keep-alive");
    sender.println("Cache-Control: s-maxage=300, public, max-age=0");
    sender.println();
    // Délégation de l'envoi du contenu du fichier
    body.respond(o);
  }

}
