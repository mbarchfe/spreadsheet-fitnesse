package de.markusbarchfeld.spreadsheetfitnesse.token;

public interface IVisitor {
  public void visitRegularCell(RegularCell regularCell);

  public void visitTableCell(TableCell tableCell);

  public void visitEndOfLine(EndOfLine endOfLine);
}
