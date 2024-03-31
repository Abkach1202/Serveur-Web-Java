import java.io.*;
import java.util.*;
import freemarker.template.*;

/**
 * Classe représentant une réponse à une requête http sur un template Freemarker
 */
public class FreemarkerResponse implements Response {
  // La configuration de Freemarker
  private static Configuration cfg;
  static {
    cfg = new Configuration(Configuration.VERSION_2_3_31);
    try {
      cfg.setDirectoryForTemplateLoading(new File("html"));
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  // L'en-tête de la réponse
  private Header header;
  // Le template Freemarker
  private Template template;
  // Les données à envoyer
  private Map<String, String> datas;

  /**
   * Constructeur de la classe
   * 
   * @param path  le chemin vers le fichier à envoyer au client
   * @param datas les données à envoyer
   */
  public FreemarkerResponse(String path, Map<String, String> datas) {
    this.header = new Header("html", -1, Response.OK);
    this.datas = datas;
    try {
      this.template = cfg.getTemplate(path);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Retourne les bons données à envoyer
   * 
   * @return les données à envoyer
   */
  private Map<String, String> getRigthDatas(String sessionId) {
    if (datas.containsKey("prenom")) {
      return datas;
    }
    SessionManager sessionManager = SessionManager.getInstance();
    Map<String, String> data = sessionManager.getDatas(sessionId);
    if (data.containsKey("prenom")) {
      return data;
    }
    data = new HashMap<>();
    data.put("prenom", "Abdoulaye");
    data.put("nom", "KATCHALA MELE");
    data.put("sport_prefere", "football");
    data.put("niveau", "intermediaire");
    return data;
  }

  @Override
  public void setCookie(String key, String value, int maxAge) {
    header.addCookie(key, value, maxAge);
  }

  @Override
  public void respond(OutputStream o, Map<String, String> cookies) {
    System.out.println("Sending template to the client...");
    Map<String, Map<String, String>> data = new HashMap<>();
    PrintWriter sender = new PrintWriter(o, true);
    // Envoie de l'entête HTTP
    sender.println(header);
    // Recherche des données à envoyer
    data.put("param", getRigthDatas(cookies.get("sessionId")));
    // Envoie du contenu du template
    try {
      template.process(data, sender);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

}
