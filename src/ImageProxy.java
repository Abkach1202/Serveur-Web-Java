import java.io.*;
import java.util.*;

// Classe représentant un proxy pour les images
public class ImageProxy implements Response {
  // Le cache des images déjà chargées
  private static Map<String, ImageFile> cache = new HashMap<>();
  // Le fichier image à envoyer au client
  private String imagePath;
  // Le type MIME du fichier
  private String mime;

  /**
   * Constructeur de la classe
   * 
   * @param imagePath le chemin vers l'image à envoyer au client
   * @param mime      le type mime du fichier à envoyer au client
   */
  public ImageProxy(String imagePath, String mime) {
    this.imagePath = imagePath;
    this.mime = mime;
  }

  @Override
  public void respond(OutputStream o) {
    synchronized (cache) {
      if (!cache.containsKey(imagePath)) {
        cache.put(imagePath, new ImageFile(imagePath, mime));
      }
    }
    cache.get(imagePath).respond(o);
  }
}
