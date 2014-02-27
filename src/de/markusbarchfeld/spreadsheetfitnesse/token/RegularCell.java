package de.markusbarchfeld.spreadsheetfitnesse.token;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class RegularCell extends CellToken implements IVisitable {

  public RegularCell(FormulaEvaluator formulaEvaluator, Cell cell) {
    super(formulaEvaluator, cell);
  }

  @Override
  public void accept(IVisitor visitor) {
    visitor.visitRegularCell(this);
  }

}
