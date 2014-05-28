package de.markusbarchfeld.spreadsheetfitnesse.token;

import de.markusbarchfeld.spreadsheetfitnesse.sources.ICell;

public class TableCell extends CellToken implements IVisitable  {

  // for testing purposes only
  protected TableCell() {
    this(null);
  }
  
  public TableCell(ICell cell)  {
    super(cell);
  }

  @Override
  public void accept(IVisitor visitor) {
    visitor.visitTableCell(this);
  }

}
