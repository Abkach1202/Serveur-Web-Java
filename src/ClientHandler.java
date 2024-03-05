import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

// Classe représentant un client et son thread coté serveur
public class ClientHandler implements Runnable {
  // Le socket de communication avec le client
  private final Socket clientSocket;
  // Les cookies envoyés par le client
  private Map<String, String> cookies;

  /**
   * Constructeur de la classe
   * 
   * @param clientSocket le socket permettant la communication avec le client
   * @throws IOException appel aux fontions clientSocket.getInputStream() et
   *                     clientSocket.getOutputStream()
   */
  public ClientHandler(Socket clientSocket) throws IOException {
    this.clientSocket = clientSocket;
    this.cookies = new HashMap<>();
  }

  /**
   * Elle permet de parser les cookies envoyés par le client
   * 
   * @param cookieString la chaine de caractères contenant les cookies
   */
  private void parseCookies(String cookieString) {
    for (String pair : cookieString.split("; ")) {
      String[] keyValue = pair.split("=");
      cookies.put(keyValue[0], keyValue[1]);
    }
  }

  /**
   * Elle permet de lire la requete HTTP du client
   * 
   * @return Une instance de l'interface Response qui repond à la requête
   * @throws IOException appel à la fonction BufferedReader.readLine()
   */
  private Response readRequest() throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    String line, requestedFile = null;
    // Lecture de la requête
    while ((line = reader.readLine()) != null && !line.isEmpty()) {
      // Récupération du fichier demandé
      if (line.startsWith("GET")) {
        requestedFile = line.split(" ")[1];
        requestedFile = requestedFile.substring(1);
      }
      // Récupération des cookies
      if (line.startsWith("Cookie")) {
        parseCookies(line.substring(8));
      }
    }
    return Response.getResponse(requestedFile);
  }

  /**
   * Elle permet de gérer la session du client
   * 
   * @param response la reponse à la requête
   */
  private void manageSession(Response response) {
    SessionManager sessionManager = SessionManager.getInstance();
    
    // Création d'une nouvelle session s'il n'y en a pas une
    if (cookies.containsKey("sessionId")) {
      String sessionId = sessionManager.createSession();
      sessionManager.setAttribute(sessionId, "scheduler", Executors.newSingleThreadScheduledExecutor());
      cookies.put("sessionId", sessionId);
    }
    // Création ou Prologation de la session au niveau du client
    response.setCookie("sessionId", cookies.get("sessionId"), 60 * 15);

    // On annule la destruction de la session pour la prolonger
    ScheduledFuture<?> sessionDestroyer = (ScheduledFuture<?>) sessionManager
        .getAttribute(cookies.get("sessionId"), "sessionDestroyer");
    if (sessionDestroyer != null) {
      sessionDestroyer.cancel(true);
    }

    // Prologation de la session au niveau du serveur
    ScheduledExecutorService scheduler = (ScheduledExecutorService) sessionManager
        .getAttribute(cookies.get("sessionId"), "scheduler");
    sessionDestroyer = scheduler.schedule(() -> {
      sessionManager.destroySession(cookies.get("sessionId"));
    }, 15, TimeUnit.MINUTES);
    sessionManager.setAttribute(cookies.get("sessionId"), "sessionDestroyer", sessionDestroyer);
  }

  @Override
  public void run() {
    try {
      // Lecture de la requête du client
      Response response = readRequest();
      // Gestion de la session du client
      manageSession(response);
      // Envoie de la reponse au client
      response.respond(clientSocket.getOutputStream());
      // Fermeture du socket
      System.out.println("Deconnexion of client.." + System.lineSeparator());
      clientSocket.close();
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }
}