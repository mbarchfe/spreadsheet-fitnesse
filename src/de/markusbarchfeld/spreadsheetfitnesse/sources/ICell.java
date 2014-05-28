package de.markusbarchfeld.spreadsheetfitnesse.sources;

public interface ICell {
  int getColumnNumber(); // order in row
  String getContent();
  String getStyle();
}
