package de.markusbarchfeld.spreadsheetfitnesse.token;

public class EndOfLine implements IVisitable {

  @Override
  public void accept(IVisitor visitor) {
    visitor.visitEndOfLine(this);
  }
  
  @Override
  public String toString() {
    return EndOfLine.class.getSimpleName();
  }

}
