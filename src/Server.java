import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * Classe représentant le serveur dans le projet
 */
public class Server {
  /**
   * Le port par défaut du serveur
   */
  public static final int DEFAULT_PORT = 80;

  // ServerSocket pour ecouter les nouveaux clients
  private final ServerSocket listener;
  // Executeur des threads liés aux client
  private final ExecutorService pool;
  // Boolean pour savoir si on affiche les images
  private final boolean no_image;

  /**
   * Constructeur du serveur avec le port spécifiée
   * 
   * @param port     Le port d'écoute du serveur
   * @param no_image true si on ne veut pas afficher les images
   * @throws IOException appel au constructeur de ServerSocket
   */
  public Server(int port, boolean no_image) throws IOException {
    this.listener = new ServerSocket(port);
    this.pool = Executors.newWorkStealingPool();
    this.no_image = no_image;
  }

  /**
   * Constructeur du serveur sur le port par défaut (80)
   * 
   * @param no_image true si on ne veut pas afficher les images
   * @throws IOException appel au constructeur de ServerSocket
   */
  public Server(boolean no_image) throws IOException {
    this(DEFAULT_PORT, no_image);
  }

  /**
   * Constructeur du serveur sur le port par défaut (80)
   * 
   * @param port Le port d'écoute du serveur
   * @throws IOException appel au constructeur de ServerSocket
   */
  public Server(int port) throws IOException {
    this(port, false);
  }

  /**
   * Constructeur du serveur sur le port par défaut (80)
   * 
   * @throws IOException appel au constructeur de ServerSocket
   */
  public Server() throws IOException {
    this(DEFAULT_PORT, false);
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
        client = new ClientHandler(clientSocket, no_image);
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
    System.out.println("Server shutting down...");
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