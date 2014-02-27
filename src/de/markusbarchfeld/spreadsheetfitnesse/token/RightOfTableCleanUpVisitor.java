package de.markusbarchfeld.spreadsheetfitnesse.token;

import java.util.ArrayList;
import java.util.List;

public class RightOfTableCleanUpVisitor implements IVisitor {

  private List<IVisitable> filteredTokens = new ArrayList<IVisitable>();
  private boolean isTableCellEncountered = false;

  public void visit(Tokens tokens) {
    tokens.accept(this);
  }

  @Override
  public void visitRegularCell(RegularCell regularCell) {
    if (!isTableCellEncountered) {
      filteredTokens.add(regularCell);
    }
  }

  @Override
  public void visitTableCell(TableCell tableCell) {
    isTableCellEncountered = true;
    filteredTokens.add(tableCell);
  }

  @Override
  public void visitEndOfLine(EndOfLine endOfLine) {
    isTableCellEncountered = false;
    filteredTokens.add(endOfLine);
  }

  public List<IVisitable> getFilteredTokens() {
    return filteredTokens;
  }

}
