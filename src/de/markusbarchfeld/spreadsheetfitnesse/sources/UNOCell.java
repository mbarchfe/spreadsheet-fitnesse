package de.markusbarchfeld.spreadsheetfitnesse.sources;

public class UNOCell implements ICell {

  private int column;
  private String content;
  private String style;
  private IRow row;

  public UNOCell(IRow row, int column, String content, String style) {
    this.column = column;
    this.content = content;
    this.style = style;
    this.row = row;
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

  @Override
  public IRow getRow() {
    return row;
  }
}
