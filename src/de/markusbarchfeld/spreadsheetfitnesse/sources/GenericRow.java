package de.markusbarchfeld.spreadsheetfitnesse.sources;

import java.util.Iterator;
import java.util.TreeMap;

public class GenericRow implements IRow, Comparable<GenericRow> {

  private Integer rowNumber;
  private TreeMap<Integer, ICell> map = new TreeMap<Integer, ICell>();

  public GenericRow(int rowNumber) {
    this.rowNumber = rowNumber;
  }

  @Override
  public int getRowNumber() {
    return rowNumber;
  }

  @Override
  public Iterator<ICell> getCells() {
    return map.values().iterator();
  }

  public void add(GenericCell unoCell) {
    map.put(unoCell.getColumnNumber(), unoCell);
  }

  @Override
  public int compareTo(GenericRow o) {
    return rowNumber.compareTo(o.rowNumber);
  }

}
