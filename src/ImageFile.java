import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

// Classe permettant de répondre à une requête de fichier image
public class ImageFile implements Response {
  // Le type MIME du fichier
  private String mime;
  // Le bufferedImage de l'image
  private BufferedImage bufferedImage;

  /**
   * Constructeur de la classe
   * 
   * @param imagePath le chemin vers l'image à envoyer au client
   * @param mime      le type mime du fichier à envoyer au client
   */
  public ImageFile(String imagePath, String mime) {
    this.mime = mime;
    try {
      this.bufferedImage = ImageIO.read(new File(imagePath));
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public void respond(OutputStream o) {
    System.out.println("Sending an image with '" + mime + "' MIME type...");
    PrintWriter sender = new PrintWriter(o, true);
    try {
      // Envoie de l'entête HTTP
      sender.println("HTTP/1.1 200 OK");
      sender.println("Content-Type: " + mime);
      sender.println();
      // Envoie du contenu de l'image
      ImageIO.write(bufferedImage, mime.split("/")[1], o);
      o.flush();
      // Fermeture du PrintWriter
      sender.close();
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }
}
