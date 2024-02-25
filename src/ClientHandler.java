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
   * @throws IOException appel aux fontions clientSocket.getInputStream() et clientSocket.getOutputStream()
   */
  public ClientHandler(Socket clientSocket) throws IOException {
    this.clientSocket = clientSocket;
    this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    this.sender = new PrintWriter(clientSocket.getOutputStream());
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'run'");
  }

}