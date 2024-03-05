import java.io.*;

// Classe permettant de répondre à une requête de fichier introuvable
public class Error404 implements Response {
  // L'en-tête de la réponse
  private StringBuilder header;

  /**
   * Constructeur de la classe
   */
  public Error404() {
    this.header = new StringBuilder(
      "HTTP/1.1 404 Not Found" + System.lineSeparator() +
      "Content-Type: text/html" + System.lineSeparator() +
      "Connection: keep-alive"
    );
  }

  @Override
  public void setCookie(String key, String value, int maxAge) {
    header.append(System.lineSeparator() + "Set-Cookie: " + key + "=" + value + "; Max-Age=" + maxAge);
  }

  @Override
  public void respond(OutputStream clientStream) {
    System.out.println("Sending an 404 error...");
    PrintWriter sender = new PrintWriter(clientStream);
    // Envoie de l'entête HTTP
    sender.println(header.toString());
    sender.println();
    // Envoie de l'erreur 404
    sender.println(
      "<html><body><h1>404 Not Found</h1>" +
      "<p>The requested file was not found on this server.</p></body></html>"
    );
    // Fermeture du PrintWriter
    sender.close();
  }
}