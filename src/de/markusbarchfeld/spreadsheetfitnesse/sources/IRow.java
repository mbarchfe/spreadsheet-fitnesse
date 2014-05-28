package de.markusbarchfeld.spreadsheetfitnesse.sources;

import java.util.Iterator;

public interface IRow {
  int getRowNumber();
  Iterator<ICell> getCells();
 
}
