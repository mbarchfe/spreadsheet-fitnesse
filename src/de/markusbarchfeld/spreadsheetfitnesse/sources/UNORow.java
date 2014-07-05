package de.markusbarchfeld.spreadsheetfitnesse.sources;

import java.util.Iterator;
import java.util.TreeMap;

public class UNORow implements IRow, Comparable<UNORow> {

  private Integer rowNumber;
  private TreeMap<Integer, ICell> map = new TreeMap<Integer, ICell>();

  public UNORow(int rowNumber) {
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

  public void add(UNOCell unoCell) {
    map.put(unoCell.getColumnNumber(), unoCell);
  }

  @Override
  public int compareTo(UNORow o) {
    return rowNumber.compareTo(o.rowNumber);
  }

}
