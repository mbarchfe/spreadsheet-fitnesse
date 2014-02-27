package de.markusbarchfeld.spreadsheetfitnesse.token;

import java.util.List;

public class Tokens implements IVisitable {

  private static List<IVisitable>  removeSuperfluousTrailingEndOfLines(
      List<IVisitable> tokens) {
    int startingElementsNotEndOfLine ;
    for (startingElementsNotEndOfLine = tokens.size() - 1; startingElementsNotEndOfLine >= 0; startingElementsNotEndOfLine--) {
      if (!(tokens.get(startingElementsNotEndOfLine) instanceof EndOfLine)) {
        break;
      }
    }
    // only remove if there are more than one end of lines left. Always keep one.
    if (startingElementsNotEndOfLine < tokens.size() - 2) {
      return tokens.subList(0, startingElementsNotEndOfLine+2);
    } else {
      return tokens;
    }
    
  }

  private List<IVisitable> tokens;

  public Tokens(List<IVisitable> tokens) {
    this.tokens = Tokens.removeSuperfluousTrailingEndOfLines(tokens);
  }

  @Override
  public void accept(IVisitor visitor) {
    for (IVisitable visible : tokens) {
      visible.accept(visitor);
    }
  }

  public List<? extends Object> asList() {
    return tokens;
  }

}
