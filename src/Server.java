import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

// Classe représentant le serveur dans le projet
public class Server {
  // Le port par defaut du serveur
  public static final int DEFAULT_PORT = 80;

  // ServerSocket pour ecouter les nouveaux clients
  private final ServerSocket listener;
  // Liste de clients connectés au serveur
  private final List<ClientHandler> clientList;
  // Executeur des threads liés aux client
  private final ExecutorService pool;

  /**
   * Constructeur du serveur avec le port spécifiée
   * 
   * @param port Le port d'écoute du serveur
   * @throws IOException appel au constructeur de ServerSocket
   */
  public Server(int port) throws IOException {
    this.listener = new ServerSocket(port);
    this.clientList = new ArrayList<>();
    this.pool = Executors.newWorkStealingPool();
  }

  /**
   * Constructeur du serveur sur le port par défaut (80)
   * 
   * @throws IOException appel au constructeur de ServerSocket
   */
  public Server() throws IOException {
    this(DEFAULT_PORT);
  }
}