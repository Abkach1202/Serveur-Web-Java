import java.net.*;
import java.io.*;

// Classe représentant un client et son thread coté serveur
public class ClientHandler implements Runnable {
  // Le socket de communication avec le client
  private final Socket clientSocket;
  // Le flux d'entrée pour recevoir les messages du client
  private final BufferedReader reader;
  // Le flux de sortie pour envoyer les messages au client
  private final PrintWriter sender;

  /**
   * Constructeur de la classe
   * 
   * @param clientSocket le socket permettant la communication avec le client
   * @throws IOException appel aux fontions clientSocket.getInputStream() et
   *                     clientSocket.getOutputStream()
   */
  public ClientHandler(Socket clientSocket) throws IOException {
    this.clientSocket = clientSocket;
    this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    this.sender = new PrintWriter(clientSocket.getOutputStream(), true);
  }

  /**
   * Elle permet de lire la requete HTTP du client
   * 
   * @return Une instance de l'interface Response qui repond à la requête
   * @throws IOException appel à la fonction BufferedReader.readLine()
   */
  public Response readRequest() throws IOException {
    String requestedFile, line;

    // Lecture de la ligne GET
    do {
      line = reader.readLine();
    } while (!line.startsWith("GET") && line != null);
    
    // Si on a pas une requête Get
    if (line == null) {
      return Response.getResponse("notFoundFile");
    }
    
    // Extraction du nom de fichier
    requestedFile = line.split(" ")[1];
    requestedFile = requestedFile.substring(1);
    return Response.getResponse(requestedFile);
  }

  @Override
  public void run() {
    try {
      // Lecture de la requête du client
      Response response = readRequest();

      // Envoie de la reponse au client
      response.respond(sender);

      // Fermeture des flux et du socket
      System.out.println("Deconnexion of client.." + System.lineSeparator() + System.lineSeparator());
      reader.close();
      sender.close();
      clientSocket.close();
    } catch (IOException e) {
      System.err.println(e.getMessage() + System.lineSeparator() + e.getStackTrace());
    }
  }
}