package de.markusbarchfeld.spreadsheetfitnesse.token;

import de.markusbarchfeld.spreadsheetfitnesse.sources.ICell;

public class CellToken {

  public CellToken(ICell cell) {
    this.cell = cell;
  }

  private ICell cell;

  public String getStringValue() {
    return cell.getContent();
  }
  
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ":" + getStringValue();
  }

}
