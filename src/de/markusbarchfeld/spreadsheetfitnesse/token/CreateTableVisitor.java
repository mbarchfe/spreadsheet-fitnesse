package de.markusbarchfeld.spreadsheetfitnesse.token;

public class CreateTableVisitor extends TransformerVisitor {

  private Table table;

  @Override
  public void visitRegularCell(RegularCell regularCell) {
    table = null;
    transformedTokens.add(regularCell);
  }

  @Override
  public void visitTableCell(TableCell tableCell) {
    if (table == null) {
      table = new Table();
      transformedTokens.add(table);
    }
    table.add(tableCell);
  }

  @Override
  public void visitEndOfLine(EndOfLine endOfLine) {
    if (table != null) {
      table.newRow();
    } else {
      transformedTokens.add(endOfLine);
    }
  }

}
