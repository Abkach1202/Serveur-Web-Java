import java.io.*;

// Classe permettant de répondre à une requête de fichier introuvable
public class NotFoundResponse implements Response {
  // L'en-tête de la réponse
  private Header header;

  /**
   * Constructeur de la classe
   */
  public NotFoundResponse() {
    this.header = new Header("html", 103, Response.NOT_FOUND);
  }

  @Override
  public void setCookie(String key, String value, int maxAge) {
    header.addCookie(key, value, maxAge);
  }

  @Override
  public void respond(OutputStream clientStream) {
    System.out.println("Sending an 404 error...");
    PrintWriter sender = new PrintWriter(clientStream);
    // Envoie de l'entête HTTP
    sender.print(header);
    // Envoie de l'erreur 404
    sender.print("<html><body><h1>404 Not Found</h1>" +
        "<p>The requested file was not found on this server.</p></body></html>");
    // Fermeture du PrintWriter
    sender.close();
  }
}