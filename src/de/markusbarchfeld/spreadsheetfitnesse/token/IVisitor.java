package de.markusbarchfeld.spreadsheetfitnesse.token;

public interface IVisitor {
  public void visitRegularCell(RegularCell regularCell);

  public void visitTableCell(TableCell tableCell);

  public void visitEndOfLine(EndOfLine endOfLine);

  public void visitTableRow(TableRow tableRow);

  public void visitTable(Table table);

  public void visitTableBeforeRows(Table table);
}
