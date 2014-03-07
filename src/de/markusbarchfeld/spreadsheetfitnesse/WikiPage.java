package de.markusbarchfeld.spreadsheetfitnesse;

import java.util.ArrayList;
import java.util.List;

public class WikiPage {

  private String name;
  private String content;
  private List<WikiPage> subPages;

  public WikiPage() {
    subPages = new ArrayList<WikiPage>();
  }

  public String getName() {
    return name;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void addSubPage(WikiPage page) {
    subPages.add(page);
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<WikiPage> getSubPages() {
    return subPages;
  }

}
