package de.markusbarchfeld.spreadsheetfitnesse.token;

public class CreateTableVisitor extends TransformerVisitor {

  private Table table;
  private boolean tableCellInRow = false;

  @Override
  public void visitRegularCell(RegularCell regularCell) {
    table = null;
    transformedTokens.add(regularCell);
  }

  @Override
  public void visitTableCell(TableCell tableCell) {
    tableCellInRow = true;
    if (table == null) {
      table = new Table();
      transformedTokens.add(table);
    }
    table.add(tableCell);
  }

  @Override
  public void visitEndOfLine(EndOfLine endOfLine) {
    // separate Tables
    if (!tableCellInRow) {
      table = null;
    }
    if (table != null) {
      table.newRow();
    } else {
      transformedTokens.add(endOfLine);
    }
    tableCellInRow = false;
  }

}
