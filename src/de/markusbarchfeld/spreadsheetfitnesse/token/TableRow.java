package de.markusbarchfeld.spreadsheetfitnesse.token;

import java.util.ArrayList;
import java.util.List;

public class TableRow implements IVisitable {
  List<TableCell> cells = new ArrayList<TableCell>();

  @Override
  public void accept(IVisitor visitor) {
    for (TableCell cell : cells) {
      cell.accept(visitor);
    }
    visitor.visitTableRow(this);
  }
}