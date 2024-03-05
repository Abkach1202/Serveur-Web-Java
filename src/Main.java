
// Classe permettant de tester le serveur

import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    try {
      Server server = new Server(8080);
      server.listen();
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }
}
