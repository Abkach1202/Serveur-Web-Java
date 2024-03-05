import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

// Classe permettant de gérer les sessions
public class SessionManager {
  // L'instance unique de la classe
  private static SessionManager instance = null;
  // La liste des sessions
  private static Map<String, Map<String, Object>> sessions = new HashMap<>();

  /**
   * Constructeur privé de la classe
   */
  private SessionManager() {
    // le singleton
  }

  /**
   * Permet de récupérer l'instance unique de la classe
   * 
   * @return l'instance unique de la classe
   */
  public static SessionManager getInstance() {
    if (instance == null) {
      instance = new SessionManager();
    }
    return instance;
  }

  /**
   * Crée une nouvelle session
   * 
   * @return l'identifiant de la session
   */
  public String createSession() {
    String sessionId = UUID.randomUUID().toString();
    sessions.put(sessionId, new HashMap<>());
    return sessionId;
  }

  /**
   * Permet de définir un attribut dans une session
   * 
   * @param sessionId      l'identifiant de la session
   * @param attributeName  le nom de l'attribut
   * @param attributeValue la valeur de l'attribut
   */
  public void setAttribute(String sessionId, String attributeName, Object attributeValue) {
    Map<String, Object> session = sessions.get(sessionId);
    if (session != null) {
      session.put(attributeName, attributeValue);
    }
  }

  /**
   * Permet de récupérer un attribut dans une session
   * 
   * @param sessionId     l'identifiant de la session
   * @param attributeName le nom de l'attribut
   * @return la valeur de l'attribut
   */
  public Object getAttribute(String sessionId, String attributeName) {
    Map<String, Object> session = sessions.get(sessionId);
    if (session != null) {
      return session.get(attributeName);
    }
    return null;
  }

  /**
   * Permet de supprimer une session
   * 
   * @param sessionId l'identifiant de la session
   */
  public void destroySession(String sessionId) {
    ((ScheduledExecutorService) sessions.get(sessionId).get("scheduler")).shutdown();
    sessions.remove(sessionId);
  }
}
