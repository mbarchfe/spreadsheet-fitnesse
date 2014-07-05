package de.markusbarchfeld.spreadsheetfitnesse.token;

import org.json.JSONArray;

public class JSONCreationVisitor implements IVisitor {

  private JSONArray tablesArray = new JSONArray();
  private JSONArray tableArray;
  private JSONArray rowArray;

  @Override
  public void visitRegularCell(RegularCell regularCell) {
  }

  @Override
  public void visitTableCell(TableCell tableCell) {
    if (rowArray == null) {
      rowArray = new JSONArray();
      tableArray.put(rowArray);
    }
    rowArray.put(tableCell.getStringValue());
  }

  @Override
  public void visitEndOfLine(EndOfLine endOfLine) {
  }

  @Override
  public void visitTableRow(TableRow tableRow) {
    // is called after all cells have been visited
    rowArray = null;

  }

  @Override
  public void visitTable(Table table) {
  }

  @Override
  public void visitTableBeforeRows(Table table) {
    tableArray = new JSONArray();
    tablesArray.put(tableArray);
  }

  public String toJSON() {
    return tablesArray.toString();
  }

}
