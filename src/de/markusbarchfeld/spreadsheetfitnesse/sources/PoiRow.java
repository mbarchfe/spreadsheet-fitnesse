package de.markusbarchfeld.spreadsheetfitnesse.sources;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class PoiRow implements IRow {

  private int rowNumber;
  private Row row;
  private PoiSource poiSource;

  public PoiRow(int rowNumber, Row row, PoiSource poiSource) {
    this.rowNumber = rowNumber;
    this.row = row;
    this.poiSource = poiSource;
    
  }

  @Override
  public int getRowNumber() {
    return rowNumber;
  }

  @Override
  public Iterator<ICell> getCells() {
    return new Iterator<ICell>() {
      int colIndex = row.getFirstCellNum();

      @Override
      public boolean hasNext() {
        return colIndex >= 0 && colIndex <= row.getLastCellNum();
      }

      @Override
      public ICell next() {
        PoiCell result = null;
        while (result == null && colIndex <= row.getLastCellNum()) {
          Cell cell = row.getCell(colIndex);
          if (cell != null) {
            result = new PoiCell(colIndex, cell, poiSource);
          }
          colIndex += 1;
        };
        return result;
      }

      @Override
      public void remove() {
        throw new RuntimeException("Remove not supported");
      };
    };
  }

}
