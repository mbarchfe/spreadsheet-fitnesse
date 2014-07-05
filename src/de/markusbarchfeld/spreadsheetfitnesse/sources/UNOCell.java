package de.markusbarchfeld.spreadsheetfitnesse.sources;

public class UNOCell implements ICell {

  private int column;
  private String content;
  private String style;

  public UNOCell(int column, String content, String style) {
    this.column = column;
    this.content = content;
    this.style = style;
  }

  @Override
  public int getColumnNumber() {
    return column;
  }

  @Override
  public String getContent() {
    return content;
  }

  @Override
  public String getStyle() {
    return style;
  }

  
  public String toString() {
    return "UNOCell: col=" + getColumnNumber() + ", content='" + getContent() + "', style=" + getStyle() ; 
  }
}
