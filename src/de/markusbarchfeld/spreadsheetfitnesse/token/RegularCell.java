package de.markusbarchfeld.spreadsheetfitnesse.token;

import de.markusbarchfeld.spreadsheetfitnesse.sources.ICell;

public class RegularCell extends CellToken implements IVisitable {

  public RegularCell(ICell cell) {
    super(cell);
  }

  @Override
  public void accept(IVisitor visitor) {
    visitor.visitRegularCell(this);
  }

}
