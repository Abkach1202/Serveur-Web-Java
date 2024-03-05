import java.io.*;
import java.util.*;

// Classe représentant un proxy pour les images
public class ImageProxy implements Response {
  // Le cache des images déjà chargées
  private static Map<String, Response> cache = new HashMap<>();
  // Le fichier image à envoyer au client
  private String path;
  // Le format de l'image
  private String format;

  /**
   * Constructeur de la classe
   * 
   * @param path   le chemin vers l'image à envoyer au client
   * @param format le format de l'image à envoyer au client
   */
  public ImageProxy(String path, String format) {
    this.path = path;
    this.format = format;
  }

  @Override
  public void respond(OutputStream o) {
    synchronized (cache) {
      if (!cache.containsKey(path)) {
        if (format == "png") {
          cache.put(path, new PNGResponse(path));
        } else if (format == "jpeg") {
          cache.put(path, new JPGResponse(path));
        }
      }
    }
    cache.get(path).respond(o);
  }
}
