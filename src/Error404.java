import java.io.*;

// Classe permettant de répondre à une requête de fichier introuvable
public class Error404 implements Response {
  // La seule instance du singleton
  private static Error404 instance;

  private Error404() {
    // Singleton rendant invisible le constructeur
  }

  /**
   * Getter de l'instance du singleton
   * 
   * @return L'instance du singleton. si il existe pas, il le crée
   */
  public static synchronized Error404 getInstance() {
    if (instance == null) {
      instance = new Error404();
    }
    return instance;
  }

  public void respond(OutputStream clientStream) {
    PrintWriter sender = new PrintWriter(clientStream);
    // Envoie de l'entête HTTP
    sender.println("HTTP/1.1 404 Not Found");
    sender.println("Content-Type: text/html");
    sender.println();
    // Envoie de l'erreur 404
    sender.println("<html><body><h1>404 Not Found</h1>" +
        "<p>The requested file was not found on this server.</p></body></html>");
    // Fermeture du PrintWriter
    sender.close();
  }
}