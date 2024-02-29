import java.io.*;
import java.util.*;

// Classe représentant un proxy pour les images
public class ImageProxy implements Response {
  // Le cache des images déjà chargées
  private static Map<String, ImageFile> cache = new HashMap<>();
  // Le fichier image à envoyer au client
  private File imageFile;
  // Le type MIME du fichier
  private String mime;

  /**
   * Constructeur de la classe
   * 
   * @param imageFile le fichier image à envoyer au client
   * @param mime      le type mime du fichier à envoyer au client
   */
  public ImageProxy(File imageFile, String mime) {
    this.imageFile = imageFile;
    this.mime = mime;
  }

  @Override
  public void respond(OutputStream o) {
    synchronized (cache) {
      if (!cache.containsKey(imageFile.getPath())) {
        cache.put(imageFile.getPath(), new ImageFile(imageFile, mime));
      }
    }
    cache.get(imageFile.getPath()).respond(o);
  }

}
