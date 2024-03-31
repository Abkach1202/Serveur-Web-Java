import java.util.*;
import java.util.concurrent.*;

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
  public static synchronized SessionManager getInstance() {
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
    setAttribute(sessionId, "scheduler", Executors.newSingleThreadScheduledExecutor());
    updateSession(sessionId);
    return sessionId;
  }

  public Map<String, String> getDatas(String sessionId) {
    Map<String, String> convertedData = new HashMap<>();
    for (String key : sessions.get(sessionId).keySet()) {
      convertedData.put(key, String.valueOf(sessions.get(sessionId).get(key)));
    }
    return convertedData;
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
   * Permet de mettre à jour une session
   * 
   * @param sessionId l'identifiant de la session
   */
  public void updateSession(String sessionId) {
    // Création du scheduler si la session n'existe pas coté serveur
    if (sessions.get(sessionId) == null) {
      sessions.put(sessionId, new HashMap<>());
      setAttribute(sessionId, "scheduler", Executors.newSingleThreadScheduledExecutor());
    }
    // Annulation du scheduledFuture destructeur de la session
    ScheduledFuture<?> destroyer = (ScheduledFuture<?>) getAttribute(sessionId, "destroyer");
    if (destroyer != null) {
      destroyer.cancel(true);
    }
    // Remplacement du scheduledFuture par un nouveau destructeur afin de prolonger
    ScheduledExecutorService scheduler = (ScheduledExecutorService) getAttribute(sessionId, "scheduler");
    destroyer = scheduler.schedule(() -> destroySession(sessionId), 15, TimeUnit.MINUTES);
    setAttribute(sessionId, "destroyer", destroyer);
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
