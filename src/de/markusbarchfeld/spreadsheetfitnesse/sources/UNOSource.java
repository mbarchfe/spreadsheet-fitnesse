package de.markusbarchfeld.spreadsheetfitnesse.sources;

import java.util.Iterator;
import java.util.TreeMap;

import de.markusbarchfeld.spreadsheetfitnesse.token.JSONCreationVisitor;
import de.markusbarchfeld.spreadsheetfitnesse.token.TokenGenerator;
import de.markusbarchfeld.spreadsheetfitnesse.token.Tokens;

public class UNOSource implements ISource {

  private TreeMap<Integer, IRow> map = new TreeMap<Integer, IRow>();

  public void add(int row, int column, String content) {

    add(row, column, content, null);
  }

  public void add(int row, int column, String content, String style) {
    UNOCell unoCell = new UNOCell(column, content, style);
    getOrCreateRow(row).add(unoCell);
  }
  
  private UNORow getOrCreateRow(int row) {
    IRow result = map.get(row);
    if (result == null) {
      result = new UNORow(row);
      map.put(row, result);
    }
    return (UNORow) result;
  }

  @Override
  public Iterator<IRow> getRowIterator() {
    return map.values().iterator();
  }
  
  public String toJSON() {
    TokenGenerator tokenGenerator = new TokenGenerator(this);
    Tokens tokens = tokenGenerator.getFilteredTokens();
    JSONCreationVisitor jsonCreationVisitor = new JSONCreationVisitor();
    tokens.accept(jsonCreationVisitor);
    return jsonCreationVisitor.toJSON();
  }

}
