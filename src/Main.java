// Classe permettant de tester le serveur
public class Main {
  public static void main(String[] args) {
    try {
      int port = 8080;
      boolean no_image = false;
      if (args.length == 1) {
        if (args[0].equals("--no-image")) {
          no_image = true;
        } else {
          port = Integer.parseInt(args[0]);
        }
      }
      else if (args.length == 2) {
        port = Integer.parseInt(args[0]);
        no_image = args[1].equals("--no-image");
      } else if (args.length > 0) {
        throw new IllegalArgumentException("Invalid number of arguments");
      }
      Server server = new Server(port, no_image);
      server.listen();
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      System.err.println("Usage: java Main [port] [--no-image]");
    }
  }
}
