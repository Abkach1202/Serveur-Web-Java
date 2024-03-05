import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

// Classe permettant de répondre à une requête de fichier image
public class ImageBody implements Response {
  // Le bufferedImage de l'image
  private BufferedImage bufferedImage;
  // Le format de l'image
  private String format;

  /**
   * Constructeur de la classe
   * 
   * @param image l'image à envoyer au client
   * @param mime  le type mime du fichier à envoyer au client
   */
  public ImageBody(File image, String format) {
    try {
      this.bufferedImage = ImageIO.read(image);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
    this.format = format;
  }

  @Override
  public void respond(OutputStream o) {
    try {
      // Envoie du contenu de l'image
      ImageIO.write(bufferedImage, format, o);
      o.flush();
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }
}
