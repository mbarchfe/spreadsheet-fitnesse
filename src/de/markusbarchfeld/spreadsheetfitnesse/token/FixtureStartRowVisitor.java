package de.markusbarchfeld.spreadsheetfitnesse.token;

import java.util.HashMap;
import java.util.Map;

import de.markusbarchfeld.spreadsheetfitnesse.sources.ICell;

public class FixtureStartRowVisitor implements IVisitor {
  int tableCounter = 0;
  private Map<Integer, Integer> fixtureToStartRow = new HashMap<Integer, Integer>();

  public Map<Integer, Integer> getFixtureToStartRow() {
    return fixtureToStartRow;
  }

  @Override
  public void visitRegularCell(RegularCell regularCell) {
  }

  @Override
  public void visitTableCell(TableCell tableCell) {
  }

  @Override
  public void visitEndOfLine(EndOfLine endOfLine) {
  }

  @Override
  public void visitTableRow(TableRow tableRow) {
  }

  @Override
  public void visitTable(Table table) {

  }

  @Override
  public void visitTableBeforeRows(Table table) {
    TableRow row = (TableRow) table.getRows().get(0);
    ICell firstCell = row.cells.get(0).getCell();
    fixtureToStartRow.put(tableCounter++, firstCell.getRow().getRowNumber());
  }

}
