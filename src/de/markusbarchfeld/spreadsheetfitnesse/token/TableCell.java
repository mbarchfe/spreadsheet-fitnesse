package de.markusbarchfeld.spreadsheetfitnesse.token;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class TableCell extends CellToken implements IVisitable  {

  // for testing purposes only
  protected TableCell() {
    this(null, null);
  }
  
  public TableCell(FormulaEvaluator formulaEvaluator, Cell cell)  {
    super(formulaEvaluator, cell);
  }

  @Override
  public void accept(IVisitor visitor) {
    visitor.visitTableCell(this);
  }

}
