import java.io.*;
import java.net.*;
import java.util.concurrent.*;

// Classe représentant le serveur dans le projet
public class Server {
  // Le port par defaut du serveur
  public static final int DEFAULT_PORT = 80;

  // ServerSocket pour ecouter les nouveaux clients
  private final ServerSocket listener;
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

  /**
   * Ecoute les connexions de nouveaux clients et leur crée un thread
   */
  public void listen() {
    System.out.println("Server started listening localhost::" + listener.getLocalPort() + System.lineSeparator() +
        "Waiting for clients...");
    try {
      // La boucle d'écoute
      while (true) {
        ClientHandler client;
        Socket clientSocket = listener.accept();
        System.out.println("Client connected succesfully");

        // Création et execution d'un nouveau thread
        client = new ClientHandler(clientSocket);
        pool.submit(client);
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Arrête les threads et stop le serveur
   */
  public void serverShutdown() {
    System.out.println("Arrêt du serveur...");
    System.out.println("Arrêt des threads");
    // Arret des threads
    pool.shutdown();
    try {
      // Attente de 10 secondes pour les threads un peu longs
      if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
        pool.shutdownNow();
      }
    } catch (InterruptedException ie) {
      pool.shutdownNow();
    }
  }
}