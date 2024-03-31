import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

// Classe permettant d'envoyer une image au client
public class ImageResponse implements Response {
  // L'en-tête de la réponse
  private Header header;
  // Le byteArrayOutputStream de l'image
  private ByteArrayOutputStream baos;

  /**
   * Constructeur de la classe
   * 
   * @param buffer    le bufferedImage de l'image à envoyer au client
   * @param extension l'extension de l'image à envoyer au client
   */
  public ImageResponse(BufferedImage buffer, String extension) {
    try {
      this.baos = new ByteArrayOutputStream();
      ImageIO.write(buffer, extension, baos);
      this.header = new Header(extension, baos.size(), Response.OK);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Constructeur de la classe
   * 
   * @param path      le chemin vers l'image à envoyer au client
   * @param extension l'extension de l'image à envoyer au client
   */
  public ImageResponse(String path, String extension) {
    try {
      this.baos = new ByteArrayOutputStream();
      ImageIO.write(ImageIO.read(new File(path)), extension, baos);
      this.header = new Header(extension, baos.size(), Response.OK);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public void setCookie(String key, String value, int maxAge) {
    header.addCookie(key, value, maxAge);
  }

  /**
   * Envoie le contenu de l'image au client
   * 
   * @param o le flux de sortie vers le client
   */
  public void respond(OutputStream o) {
    System.out.println("Sending image to the client...");
    PrintWriter sender = new PrintWriter(o, true);
    // Envoie de l'entête HTTP
    sender.println(header);
    // Envoie du contenu de l'image
    try {
      baos.writeTo(o);
      o.flush();
      // Fermeture du PrintWriter
      sender.close();
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }
}
