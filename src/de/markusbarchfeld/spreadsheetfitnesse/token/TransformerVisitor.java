package de.markusbarchfeld.spreadsheetfitnesse.token;

import java.util.ArrayList;
import java.util.List;

public abstract class TransformerVisitor implements IVisitor{

  protected List<IVisitable> transformedTokens = new ArrayList<IVisitable>();

  public TransformerVisitor() {
    super();
  }

  public void visit(Tokens tokens) {
    tokens.accept(this);
  }

  public Tokens getTransformedTokens() {
    return new Tokens(transformedTokens);
  }

  @Override
  public abstract void visitRegularCell(RegularCell regularCell);

  @Override
  public abstract void visitTableCell(TableCell tableCell) ;

  @Override
  public abstract void visitEndOfLine(EndOfLine endOfLine);

  @Override
  public void visitTableRow(TableRow tableRow) {
  }

  @Override
  public void visitTable(Table table) {
  }

}