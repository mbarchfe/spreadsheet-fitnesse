package de.markusbarchfeld.spreadsheetfitnesse;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import de.markusbarchfeld.spreadsheetfitnesse.token.CreateTableVisitor;
import de.markusbarchfeld.spreadsheetfitnesse.token.EndOfLine;
import de.markusbarchfeld.spreadsheetfitnesse.token.IVisitable;
import de.markusbarchfeld.spreadsheetfitnesse.token.Table;
import de.markusbarchfeld.spreadsheetfitnesse.token.TableCell;
import de.markusbarchfeld.spreadsheetfitnesse.token.Tokens;

public class CreateTableVisitorTest {

  @SuppressWarnings("rawtypes")
  @Test
  public void testTableVisitorAndTableVisiting() throws Exception {
    // assuming that tokens are filtered, i.e. there are no regular cells right
    // of a table
    TableCell tableCell1 = new TableCell(null, null);
    TableCell tableCell2 = new TableCell(null, null);
    TableCell tableCell3 = new TableCell(null, null);
    IVisitable[] inputTokens = { tableCell1, new EndOfLine(), tableCell2,
        tableCell3, new EndOfLine() };
    Class[] expectedOutputTokens = { Table.class };
    Tokens tokens = actAndAssert(inputTokens, expectedOutputTokens);
    Table table = (Table) tokens.asList().get(0);
    assertEquals(2, table.getRows().size());
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testTableVisitorAndNoTableCellTokens() throws Exception {

    IVisitable[] inputTokens = { new EndOfLine() };
    Class[] expectedOutputTokens = { EndOfLine.class };
    actAndAssert(inputTokens, expectedOutputTokens);
  }

  @SuppressWarnings("rawtypes")
  private Tokens actAndAssert(IVisitable[] inputTokens,
      Class[] expectedOutputTokens) {
    CreateTableVisitor createTableVisitor = new CreateTableVisitor();
    createTableVisitor.visit(new Tokens(Arrays.asList(inputTokens)));

    Tokens transformedTokens = createTableVisitor.getTransformedTokens();
    Class[] actualTokenClasses = TestUtil
        .getClasses(transformedTokens.asList());
    assertArrayEquals(expectedOutputTokens, actualTokenClasses);
    return transformedTokens;
  }

}
