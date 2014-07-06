package de.markusbarchfeld.spreadsheetfitnesse.sources;

public interface ICell {
  IRow getRow();
  int getColumnNumber(); // order in row
  String getContent();
  String getStyle();
}
