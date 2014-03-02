package de.markusbarchfeld.spreadsheetfitnesse.token;

public class AddedTableCell extends TableCell {

  private String content;

  public AddedTableCell(String content) {
    super(null, null);
    this.content = content;
  }

  @Override
  public String getStringValue() {
    return content;
  }

}
