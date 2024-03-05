import java.io.*;

// Classe permettant de répondre à une requête de fichier CSS
public class CSSResponse implements Response {
  // Le fichier à envoyer au client
  private File CSSFile;
  // L'en-tête de la réponse
  private StringBuilder header;
  // l'Envoyeur de fichier texte
  private FileSender fileSender;

  /**
   * Constructeur de la classe
   * 
   * @param path le chemin vers le fichier à envoyer au client
   */
  public CSSResponse(String path) {
    this.CSSFile = new File(path);
    this.fileSender = new FileSender(CSSFile);
    this.header = new StringBuilder(
      "HTTP/1.1 200 OK" + System.lineSeparator() +
      "Content-Type: text/css" + System.lineSeparator() +
      "Content-Length: " + CSSFile.length() + System.lineSeparator() +
      "Connection: keep-alive" + System.lineSeparator() +
      "Cache-Control: s-maxage=300, public, max-age=0"
    );
  }

  @Override
  public void setCookie(String key, String value, int maxAge) {
    header.append(System.lineSeparator() + "Set-Cookie: " + key + "=" + value + "; Max-Age=" + maxAge);
  }

  @Override
  public void respond(OutputStream o) {
    System.out.println("Sending " + CSSFile.getName() + " to the client...");
    PrintWriter sender = new PrintWriter(o, true);
    // Envoie de l'entête HTTP
    sender.println(header.toString());
    sender.println();
    // Délégation de l'envoi du contenu du fichier
    fileSender.send(o);
  }

}
