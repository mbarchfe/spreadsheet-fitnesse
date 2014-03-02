package de.markusbarchfeld.spreadsheetfitnesse.token;

public class WikiPageMarkupVisitor implements IVisitor {

  // private static Log log = LogFactory.getLog(WikiPageMarkupVisitor.class);

  private static final String Table_Cell_Separator = "|";

  private StringBuilder stringBuilder = new StringBuilder();

  @Override
  public String toString() {
    return stringBuilder.toString();
  }

  @Override
  public void visitRegularCell(RegularCell regularCell) {
    stringBuilder.append(regularCell.getStringValue());
  }

  @Override
  public void visitTableCell(TableCell tableCell) {
    stringBuilder.append(Table_Cell_Separator);
    stringBuilder.append(tableCell.getStringValue());
  }

  @Override
  public void visitEndOfLine(EndOfLine endOfLine) {
    stringBuilder.append("\n");
  }

  public void visit(Tokens tokens) {
    tokens.accept(this);
  }

  @Override
  public void visitTableRow(TableRow tableRow) {
    stringBuilder.append(Table_Cell_Separator);
    stringBuilder.append("\n");

  }

  @Override
  public void visitTable(Table table) {

  }

  @Override
  public void visitTableBeforeRows(Table table) {
    // TODO Auto-generated method stub
    
  }
}
