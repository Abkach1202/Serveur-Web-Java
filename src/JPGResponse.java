import java.io.*;

// Classe permettant de répondre à une requête de fichier image
public class JPGResponse implements Response {
  // L'image à envoyer au client
  private File JPGFile;
  // L'en-tête de la réponse
  private StringBuilder header;
  // l'Envoyeur d'image
  private ImageSender imageSender;

  /**
   * Constructeur de la classe
   * 
   * @param path le chemin vers l'image à envoyer au client
   */
  public JPGResponse(String path) {
    this.JPGFile = new File(path);
    this.imageSender = new ImageSender(JPGFile, "jpeg");
    this.header = new StringBuilder(
      "HTTP/1.1 200 OK" + System.lineSeparator() +
      "Content-Type: image/jpeg" + System.lineSeparator() +
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
    System.out.println("Sending " + JPGFile.getName() + " to the client...");
    PrintWriter sender = new PrintWriter(o, true);
    // Envoie de l'entête HTTP
    sender.println(header.toString());
    sender.println();
    // Délégation de l'envoi du contenu de l'image
    imageSender.send(o);
  }
}
