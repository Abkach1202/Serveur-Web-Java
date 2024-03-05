import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

// Classe permettant d'envoyer une image au client
public class ImageSender {
  // Le bufferedImage de l'image
  private BufferedImage bufferedImage;
  // Le format de l'image
  private String format;

  /**
   * Constructeur de la classe
   * 
   * @param image  l'image à envoyer au client
   * @param format le format de l'image à envoyer au client
   */
  public ImageSender(File image, String format) {
    try {
      this.bufferedImage = ImageIO.read(image);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
    this.format = format;
  }

  /**
   * Envoie le contenu de l'image au client
   * 
   * @param o le flux de sortie vers le client
   */
  public void send(OutputStream o) {
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
