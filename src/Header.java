/**
 * Classe permettant de representer un en-tête HTTP
 */
public class Header {
  // L'en-tête de la réponse
  private StringBuilder header;

  /**
   * Retourne le nom associé à un code de réponse
   * 
   * @param code le code de réponse
   * @return le nom associé au code de réponse
   */
  private static String getCodeName(int code) {
    switch (code) {
      case Response.OK:
        return "OK";
      case Response.NOT_FOUND:
        return "Not Found";
      default:
        return "Not handled";
    }
  }

  /**
   * Retourne le type MIME associé à une extension
   * 
   * @param extension l'extension du fichier
   * @return le type MIME associé à l'extension
   */
  private static String getMimeType(String extension) {
    switch (extension) {
      case "html":
        return "text/html";
      case "css":
        return "text/css";
      case "js":
        return "text/javascript";
      case "png":
        return "image/png";
      case "jpg":
      case "jpeg":
        return "image/jpeg";
      default:
        return "text/plain";
    }
  }

  /**
   * Retourne le séparateur d'en-tête
   * 
   * @return le séparateur d'en-tête
   */
  private static String getHeaderSeparator() {
    return "\r" + System.lineSeparator();
  }

  /**
   * Constructeur de la classe
   * 
   * @param extension     l'extension du fichier de la réponse
   * @param contentLength la taille du contenu de la réponse
   * @param code          le code de réponse
   */
  public Header(String extension, long contentLength, int code) {
    this.header = new StringBuilder();
    this.header.append("HTTP/1.1 " + code + " " + getCodeName(code) + getHeaderSeparator());
    this.header.append("Content-Type: " + getMimeType(extension) + getHeaderSeparator());
    this.header.append("Content-Length: " + contentLength + getHeaderSeparator());
    this.header.append("Connection: keep-alive" + getHeaderSeparator());
    this.header.append("Cache-Control: s-maxage=300, public, max-age=0" + getHeaderSeparator());
  }

  /**
   * Ajoute un cookie à l'en-tête
   * 
   * @param key    la clé du cookie
   * @param value  la valeur du cookie
   * @param maxAge la durée de vie du cookie
   */
  public void addCookie(String key, String value, int maxAge) {
    this.header.append("Set-Cookie: " + key + "=" + value + "; Max-Age=" + maxAge + getHeaderSeparator());
  }

  @Override
  public String toString() {
    return this.header.toString();
  }
}
