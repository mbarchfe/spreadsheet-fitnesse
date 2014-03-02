package de.markusbarchfeld.spreadsheetfitnesse.token;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import de.markusbarchfeld.spreadsheetfitnesse.TestUtil;
import de.markusbarchfeld.spreadsheetfitnesse.token.CreateTableVisitor;
import de.markusbarchfeld.spreadsheetfitnesse.token.EndOfLine;
import de.markusbarchfeld.spreadsheetfitnesse.token.IVisitable;
import de.markusbarchfeld.spreadsheetfitnesse.token.Table;
import de.markusbarchfeld.spreadsheetfitnesse.token.TableCell;
import de.markusbarchfeld.spreadsheetfitnesse.token.Tokens;

public class CreateTableVisitorTest {

  @SuppressWarnings("rawtypes")
  @Test
  public void testOneTableWithTwoRows() throws Exception {
    // assuming that tokens are filtered, i.e. there are no regular cells right
    // of a table
    IVisitable[] inputTokens = { new TableCell(), new EndOfLine(), new TableCell(),
        new TableCell(), new EndOfLine() };
    Class[] expectedOutputTokens = { Table.class };
    Tokens tokens = actAndAssert(inputTokens, expectedOutputTokens);
    Table table = (Table) tokens.asList().get(0);
    assertEquals(2, table.getRows().size());
  }
  
  @SuppressWarnings("rawtypes")
  @Test
  public void testTwoTablesWithOneRow() throws Exception {
    // tables are separated by at least one end of line
    IVisitable[] inputTokens = { new TableCell(), new EndOfLine(), new EndOfLine(), new TableCell(),
        new EndOfLine() };
    Class[] expectedOutputTokens = { Table.class, EndOfLine.class, Table.class };
    actAndAssert(inputTokens, expectedOutputTokens);
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
