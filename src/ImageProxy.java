import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

// Classe représentant un proxy pour les images
public class ImageProxy implements Response {
  // Le cache des images déjà chargées pour eviter tout un nouveau chargement
  private static Map<String, Response> cache = new HashMap<>();
  static {
    int width = 600, height = 600;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = image.createGraphics();
    // Dessin du croix
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, width, height);
    g2d.setColor(Color.RED);
    g2d.setStroke(new BasicStroke(5)); // Épaisseur de la ligne
    g2d.drawLine(0, 0, width, height); // Ligne horizontale
    g2d.drawLine(width, 0, 0, height); // Ligne verticale
    g2d.dispose();
    // Libérer les ressources du contexte graphique
    cache.put("no-image", new ImageResponse(image, "png"));
  }

  // Le fichier image à envoyer au client
  private String path;
  // L'extension de l'image
  private String extension;
  // afficher les images ou non
  private boolean no_image;

  /**
   * Constructeur de la classe
   * 
   * @param path      le chemin vers l'image à envoyer au client
   * @param extension le extension de l'image à envoyer au client
   * @param no_image  true si on ne veut pas afficher les images
   */
  public ImageProxy(String path, String extension, boolean no_image) {
    this.path = path;
    this.extension = extension;
    this.no_image = no_image;
  }

  @Override
  public void setCookie(String key, String value, int maxAge) {
    synchronized (cache) {
      if (!cache.containsKey(path)) {
        cache.put(path, new ImageResponse(path, extension));
      }
    }
    cache.get(path).setCookie(key, value, maxAge);
  }

  @Override
  public void respond(OutputStream o, Map<String, String> cookies) {
    if (no_image) {
      cache.get("no-image").respond(o, cookies);
      return;
    }
    synchronized (cache) {
      if (!cache.containsKey(path)) {
        cache.put(path, new ImageResponse(path, extension));
      }
    }
    cache.get(path).respond(o, cookies);
  }
}
