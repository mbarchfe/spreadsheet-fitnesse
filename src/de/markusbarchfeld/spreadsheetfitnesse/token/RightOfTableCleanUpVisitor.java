package de.markusbarchfeld.spreadsheetfitnesse.token;


public class RightOfTableCleanUpVisitor extends TransformerVisitor  {

  private boolean isTableCellEncountered = false;

  @Override
  public void visitRegularCell(RegularCell regularCell) {
    if (!isTableCellEncountered) {
      transformedTokens.add(regularCell);
    }
  }

  @Override
  public void visitTableCell(TableCell tableCell) {
    isTableCellEncountered = true;
    transformedTokens.add(tableCell);
  }

  @Override
  public void visitEndOfLine(EndOfLine endOfLine) {
    isTableCellEncountered = false;
    transformedTokens.add(endOfLine);
  }

  @Override
  public void visitTableRow(TableRow tableRow) {
    // assuming that table rows are not yet created
  }

  @Override
  public void visitTable(Table table) {
    // assuming that tables are not yet created
  }

}
