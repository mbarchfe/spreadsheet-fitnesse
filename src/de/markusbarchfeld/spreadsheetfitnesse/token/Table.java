package de.markusbarchfeld.spreadsheetfitnesse.token;

import java.util.ArrayList;
import java.util.List;

public class Table implements IVisitable {
  List<TableRow> rows = new ArrayList<TableRow>();
  //  remember that a new row needs to be created when a new cell arrives
  // do not create unnecessary rows
  boolean isNewRowRequired = true;

  @Override
  public void accept(IVisitor visitor) {
    for (TableRow tableRow : rows) {
      tableRow.accept(visitor);
    }
    visitor.visitTable(this);
  }

  public void add(TableCell tableCell) {
    if (isNewRowRequired) {
      rows.add(new TableRow());
      isNewRowRequired = false;
    }
    rows.get(rows.size() -1 ).cells.add(tableCell);
  }

  public void newRow() {
    isNewRowRequired = true;
  }

  public List<? extends Object> getRows() {
    return rows;
  }

}
