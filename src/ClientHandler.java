import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Classe représentant un client et son thread coté serveur
 */
public class ClientHandler implements Runnable {
  // Le socket de communication avec le client
  private Socket clientSocket;
  // Les cookies envoyés par le client
  private Map<String, String> cookies;
  // Les paramètres de la requête
  private Map<String, String> params;
  // afficher les images ou non
  private boolean no_image;

  /**
   * Constructeur de la classe
   * 
   * @param clientSocket le socket permettant la communication avec le client
   * @param no_image     true si on ne veut pas afficher les images
   * @throws IOException appel aux fontions clientSocket.getInputStream() et
   *                     clientSocket.getOutputStream()
   */
  public ClientHandler(Socket clientSocket, boolean no_image) throws IOException {
    this.clientSocket = clientSocket;
    this.no_image = no_image;
    this.cookies = new HashMap<>();
    this.params = new HashMap<>();
  }

  /**
   * Elle permet de parser les cookies envoyés par le client
   * 
   * @param cookieString la chaine de caractères contenant les cookies
   */
  private void parseCookies(String cookieString) {
    for (String pair : cookieString.split("; ")) {
      String[] keyValue = pair.split("=");
      if (!cookies.containsKey(keyValue[0])) {
        cookies.put(keyValue[0], keyValue[1]);
      }
    }
  }

  /**
   * Elle permet de parser le lien et les paramètres envoyés par le client
   * 
   * @param line la chaine de caractères contenant le lien et les paramètres
   * @return une map contenant les paramètres
   */
  private String parseParams(String line) throws UnsupportedEncodingException {
    String[] parts = line.split("\\?");
    if (parts.length == 2) {
      for (String pair : parts[1].split("&")) {
        String[] keyValue = pair.split("=");
        params.put(URLDecoder.decode(keyValue[0], "UTF-8"), URLDecoder.decode(keyValue[1], "UTF-8"));
      }
    }
    if (params.containsKey("no-image")) {
      cookies.put("no-image", params.get("no-image").equals("false") ? "false" : "true");
    }
    return parts[0].substring(1);
  }

  /**
   * Elle permet de lire la requete HTTP du client
   * 
   * @return Une instance de l'interface Response qui repond à la requête
   * @throws IOException appel à la fonction BufferedReader.readLine()
   */
  private Response readRequest() throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    String line, link = null;
    // Lecture de la requête
    while ((line = reader.readLine()) != null && !line.isEmpty()) {
      // Récupération du fichier demandé
      if (line.startsWith("GET")) {
        link = parseParams(line.split(" ")[1]);
      }
      // Récupération des cookies
      if (line.startsWith("Cookie")) {
        parseCookies(line.substring(8));
      }
    }
    if (cookies.containsKey("no-image")) {
      no_image = no_image || cookies.get("no-image").equals("true");
    }
    return Response.getResponse(link, no_image, params);
  }

  /**
   * Elle permet de gérer la session du client
   * 
   * @param response la reponse à la requête
   */
  private void manageSession(Response response) {
    SessionManager sessionManager = SessionManager.getInstance();
    String sessionId;
    // Création ou Prologation de la session au niveau du serveur
    if (!cookies.containsKey("sessionId")) {
      sessionId = sessionManager.createSession();
      cookies.put("sessionId", sessionId);
    } else {
      sessionId = cookies.get("sessionId");
      sessionManager.updateSession(sessionId);
    }
    // Création ou Prologation de la session au niveau du client
    response.setCookie("sessionId", sessionId, 60 * 15);
    // Envoie du cookie no-image
    if (params.containsKey("no-image")) {
      response.setCookie("no-image", cookies.get("no-image"), 60 * 15);
    }
  }

  @Override
  public void run() {
    try {
      // Lecture de la requête du client
      Response response = readRequest();
      // Gestion de la session du client
      manageSession(response);
      // Envoie de la reponse au client
      response.respond(clientSocket.getOutputStream(), cookies);
      // Fermeture du socket
      System.out.println("Deconnexion of client..." + System.lineSeparator());
      clientSocket.close();
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }
}