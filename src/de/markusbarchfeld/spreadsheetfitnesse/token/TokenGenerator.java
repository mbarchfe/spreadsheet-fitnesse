package de.markusbarchfeld.spreadsheetfitnesse.token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.markusbarchfeld.spreadsheetfitnesse.sources.ICell;
import de.markusbarchfeld.spreadsheetfitnesse.sources.IRow;
import de.markusbarchfeld.spreadsheetfitnesse.sources.ISource;

public class TokenGenerator {

  private static Log log = LogFactory.getLog(TokenGenerator.class);
  private List<IVisitable> tokens = new ArrayList<IVisitable>();
  private ISource source;

  public TokenGenerator(ISource source) {
    this.source = source;
  }

  protected boolean isTableCell(ICell cell) {
    String style = cell.getStyle();
    return "FitTableCell".equals(style) || "FitTableHeader".equals(style);
  }

  public void generateTokens() {
    int lastIndex = -1;
    Iterator<IRow> rowIterator = this.source.getRowIterator();
    while (rowIterator.hasNext()) {
      IRow iRow = (IRow) rowIterator.next();
      if (iRow == null) {
        // only the last row can be null
        break;
      }

      int rowIndex = iRow.getRowNumber();
      int rowDelta = rowIndex - lastIndex;
      if (rowDelta > 1) {
        for (int i = 1; i < rowDelta; i++) {
          addToken(new EndOfLine());
        }
      }
      lastIndex = rowIndex;
      Iterator<ICell> cells = iRow.getCells();
      while (cells.hasNext()) {
        ICell cell = (ICell) cells.next();
        if (cell != null) {
          int colIndex = cell.getColumnNumber();
          log.debug(rowIndex + "," + colIndex + ": " + cell);
          if (isTableCell(cell)) {
            addToken(new TableCell(cell));
          } else {
            addToken(new RegularCell(cell));
          }
        }
      }
      addToken(new EndOfLine());
    }
  }

  public void addToken(IVisitable visitable) {
    tokens.add(visitable);
  }

  public List<IVisitable> getTokens() {
    return tokens;
  }

}
