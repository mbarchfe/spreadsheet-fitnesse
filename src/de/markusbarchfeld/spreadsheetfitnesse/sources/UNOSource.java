package de.markusbarchfeld.spreadsheetfitnesse.sources;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import de.markusbarchfeld.spreadsheetfitnesse.token.FixtureStartRowVisitor;
import de.markusbarchfeld.spreadsheetfitnesse.token.JSONCreationVisitor;
import de.markusbarchfeld.spreadsheetfitnesse.token.TokenGenerator;
import de.markusbarchfeld.spreadsheetfitnesse.token.Tokens;

public class UNOSource implements ISource {

  private TreeMap<Integer, IRow> map = new TreeMap<Integer, IRow>();
  private Map<Integer, Integer> fixtureToStartRow;
  private Tokens tokens;
  public void add(int row, int column, String content) {

    add(row, column, content, null);
  }

  public void add(int row, int column, String content, String style) {
    UNORow unoRow = getOrCreateRow(row);
    UNOCell unoCell = new UNOCell(unoRow, column, content, style);
    unoRow.add(unoCell);
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
    JSONCreationVisitor jsonCreationVisitor = new JSONCreationVisitor();
    getOrCreateTokens().accept(jsonCreationVisitor);
    return jsonCreationVisitor.toJSON();
  }

  private Tokens getOrCreateTokens() {
    if (tokens == null) {
      TokenGenerator tokenGenerator = new TokenGenerator(this);
      tokens = tokenGenerator.getFilteredTokens();      
    }
    return tokens;
  }

  public Map<Integer, Integer> getFixtureToStartRow() {
    FixtureStartRowVisitor fixtureStartRowVisitor = new FixtureStartRowVisitor();
    getOrCreateTokens().accept(fixtureStartRowVisitor);
    return fixtureStartRowVisitor.getFixtureToStartRow();
  }

}
