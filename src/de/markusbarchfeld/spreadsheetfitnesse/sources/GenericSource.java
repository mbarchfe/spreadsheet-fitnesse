package de.markusbarchfeld.spreadsheetfitnesse.sources;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import de.markusbarchfeld.spreadsheetfitnesse.token.FixtureStartRowVisitor;
import de.markusbarchfeld.spreadsheetfitnesse.token.JSONCreationVisitor;
import de.markusbarchfeld.spreadsheetfitnesse.token.TokenGenerator;
import de.markusbarchfeld.spreadsheetfitnesse.token.Tokens;

public class GenericSource implements ISource {

  private TreeMap<Integer, IRow> map = new TreeMap<Integer, IRow>();
  private Tokens tokens;
  public void add(int row, int column, String content) {

    add(row, column, content, null);
  }

  public void add(int row, int column, String content, String style) {
    GenericRow unoRow = getOrCreateRow(row);
    GenericCell unoCell = new GenericCell(unoRow, column, content, style);
    unoRow.add(unoCell);
  }
  
  private GenericRow getOrCreateRow(int row) {
    IRow result = map.get(row);
    if (result == null) {
      result = new GenericRow(row);
      map.put(row, result);
    }
    return (GenericRow) result;
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
