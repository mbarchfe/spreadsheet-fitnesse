package de.markusbarchfeld.spreadsheetfitnesse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class FitnesseRest {

  private static final String DEFAULT_FITNESSE_URL = "http://localhost:8080";

  private static Log log = LogFactory.getLog(FitnesseRest.class);

  public String url;

  public FitnesseRest() {
    this(DEFAULT_FITNESSE_URL);
  }

  public FitnesseRest(String baseUrl) {
    this.url = baseUrl;
  }

  public String createQualifiedPageName(String suiteName, String subPage) {
    String result = subPage;
    if (suiteName != null && suiteName.length() > 0) {
      result = suiteName + "." + subPage;
    }
    return result;
  }

  public void save(String suitePage, String pageName, String wikiMarkup) {
    boolean pageExists = getPageContent(createQualifiedPageName(suitePage,
        pageName)) != null;
    if (!pageExists && pageName.length() > 0) {
      log.info("Creating page of type test with name '" + pageName
          + "' as child of " + suitePage);
      // the saveData responder would also create the page, however, not with
      // page type of Test
      try {
        execute(new HttpGet(createPageUrl(suitePage, "addChild") + "&pageName="
            + pageName + "&pageType=Test"));
      } catch (InvalidFileNameException e) {
        throw new RuntimeException("Invalid page name " + pageName);
      }
    }
    String pageUrl = createPageUrl(
        createQualifiedPageName(suitePage, pageName), "saveData");
    log.info("Writing page content with url " + pageUrl);
    HttpPost httpPost = new HttpPost(pageUrl);
    try {
      List<NameValuePair> x = new ArrayList<NameValuePair>();
      x.add(new BasicNameValuePair("pageContent", wikiMarkup));
      x.add(new BasicNameValuePair("editTime", Long.toString(System
          .currentTimeMillis())));
      httpPost.setEntity(new UrlEncodedFormEntity(x, "utf-8"));
    } catch (UnsupportedEncodingException e) {
      log.error("Could not save content of page " + pageName, e);
    }
    try {
      execute(httpPost);
    } catch (InvalidFileNameException e) {
      throw new RuntimeException("Invalid page name " + pageName);
    }

  }

  public String getPageContent(String pageName) {
    try {
      return execute(new HttpGet(createPageUrl(pageName, "pageData")));
    } catch (InvalidFileNameException e) {
      throw new RuntimeException("Invalid page name '" + pageName + "'");
    }
  }

  public String runSuite(String pageName) {
    try {
      return execute(new HttpGet(createPageUrl(pageName, "suite")));
    } catch (InvalidFileNameException e) {
      throw new RuntimeException("Invalid page name '" + pageName + "'");
    }

  }

  private String createPageUrl(String pageName, String responder) {
    return url + "/" + pageName + "?responder=" + responder;
  }

  private String execute(HttpUriRequest httprequest)
      throws InvalidFileNameException {
    BasicResponseHandler responseHandler = new BasicResponseHandler();
    HttpClient httpclient = new DefaultHttpClient();
    try {
      httprequest.setHeader("Content-Type", "text/html; charset=utf-8");
      if (log.isTraceEnabled()) {
        log.trace("Sending request to " + httprequest.getRequestLine());
        log.trace(httprequest.getRequestLine());
      }
      String responseBody = httpclient.execute(httprequest, responseHandler);
      if (log.isTraceEnabled()) {
        log.trace("------ Response Start ----------------------------------");
        log.trace(responseBody);
        log.trace("------ Response End ----------------------------------");
      }
      return responseBody;
    } catch (org.apache.http.client.HttpResponseException re) {
      if (re.getStatusCode() == 404) {
        return null;
      }
      String pageName = (String) httprequest.getParams().getParameter(
          "pageName");
      String exceptionMessage = "Error on page " + pageName;
      if (re.getStatusCode() == 400) {
        // Bad Request currently always significates invalid page name
        throw new InvalidFileNameException();
      }
      throw new RuntimeException(exceptionMessage);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    } finally {
      // When HttpClient instance is no longer needed,
      // shut down the connection manager to ensure
      // immediate deallocation of all system resources
      httpclient.getConnectionManager().shutdown();
    }

  }

  /**
   * 
   * @param parent
   *          empty string if root page
   * @param newSuitePage
   *          valid camel cased name for the new suite page
   * @return
   */
  public String createSuitePage(String parent, String newSuitePage) {
    boolean isParentRoot = parent == null || parent.length() == 0;
    log.info("Creating test suite " + newSuitePage + " as child of "
        + (isParentRoot ? "root" : parent));
    try {
      execute(new HttpGet(createPageUrl(parent, "addChild") + "&pageName="
          + newSuitePage + "&pageType=Suite"));
    } catch (InvalidFileNameException e) {
      throw new RuntimeException("Invalid page name '" + newSuitePage + "'");
    }
    return createQualifiedPageName(parent, newSuitePage);
  }
}