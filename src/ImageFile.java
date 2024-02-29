import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

// Classe permettant de répondre à une requête de fichier image
public class ImageFile implements Response {
  // Le fichier image à envoyer au client
  private File imageFile;
  // Le type MIME du fichier
  private String mime;
  // Le bufferedImage de l'image
  private BufferedImage imageSender;

  /**
   * Constructeur de la classe
   * 
   * @param imageFile le fichier image à envoyer au client
   * @param mime      le type mime du fichier à envoyer au client
   */
  public ImageFile(File imageFile, String mime) throws IOException {
    this.imageFile = imageFile;
    this.mime = mime;
    this.imageSender = ImageIO.read(imageFile);
  }

  @Override
  public void respond(OutputStream o) {
    System.out.println("Sending " + imageFile.getPath() + "...");
    PrintWriter sender = new PrintWriter(o, true);
    try {
      // Envoie de l'entête HTTP
      sender.println("HTTP/1.1 200 OK");
      sender.println("Content-Type: " + mime);
      sender.println("Content-Length: " + imageFile.length());
      sender.println();
      // Envoie du contenu de l'image
      ImageIO.write(imageSender, mime.split("/")[1], o);
      // Fermeture du PrintWriter
      sender.close();
    } catch (IOException e) {
      System.err.println(e.getMessage() + System.lineSeparator() + e.getStackTrace());
    }
  }
}
