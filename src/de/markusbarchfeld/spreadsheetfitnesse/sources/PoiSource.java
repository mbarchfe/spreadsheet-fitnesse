package de.markusbarchfeld.spreadsheetfitnesse.sources;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class PoiSource implements ISource {
  private Sheet sheet;
  private FormulaEvaluator formulaEvaluator;

  public PoiSource(Sheet sheet, FormulaEvaluator formulaEvaluator) {
    this.sheet = sheet;
    this.formulaEvaluator = formulaEvaluator;
  }

  @Override
  public Iterator<IRow> getRowIterator() {

    return new Iterator<IRow>() {
      int rowIndex = sheet.getFirstRowNum();

      @Override
      public boolean hasNext() {
        return rowIndex <= sheet.getLastRowNum();
      }

      @Override
      public IRow next() {
        PoiRow result = null;
        while (result == null && rowIndex <= sheet.getLastRowNum()) {
          Row row = sheet.getRow(rowIndex);
          if (row != null) {
            result = new PoiRow(rowIndex, row, PoiSource.this);
          }
          rowIndex += 1;
        }
        return result;
      }

      @Override
      public void remove() {
        throw new RuntimeException("Remove not supported");
      }

    };
  }

  public Sheet getSheet() {
    return sheet;
  }
}
