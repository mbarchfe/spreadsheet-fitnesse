package de.markusbarchfeld.spreadsheetfitnesse.token;

public class WikiPageMarkupVisitor implements IVisitor {

  // private static Log log = LogFactory.getLog(WikiPageMarkupVisitor.class);

  private static final String Table_Cell_Separator = "|";

  private StringBuilder stringBuilder = new StringBuilder();

  private IVisitable last;

  @Override
  public String toString() {
    return stringBuilder.toString();
  }

  @Override
  public void visitRegularCell(RegularCell regularCell) {
    stringBuilder.append(regularCell.getStringValue());
    this.last = regularCell;
  }

  @Override
  public void visitTableCell(TableCell tableCell) {
    stringBuilder.append(Table_Cell_Separator);
    stringBuilder.append(tableCell.getStringValue());
    this.last = tableCell;
  }

  @Override
  public void visitEndOfLine(EndOfLine endOfLine) {
    if (this.last instanceof TableCell) {
      stringBuilder.append(Table_Cell_Separator);
    }
    stringBuilder.append("\n");
    this.last = endOfLine;
  }

  public void visit(Tokens tokens) {
    tokens.accept(this);
  }
}
