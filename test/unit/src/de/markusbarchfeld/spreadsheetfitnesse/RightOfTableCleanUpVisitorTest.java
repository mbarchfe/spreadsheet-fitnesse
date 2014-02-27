package de.markusbarchfeld.spreadsheetfitnesse;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.junit.Test;

import de.markusbarchfeld.spreadsheetfitnesse.token.EndOfLine;
import de.markusbarchfeld.spreadsheetfitnesse.token.IVisitable;
import de.markusbarchfeld.spreadsheetfitnesse.token.RightOfTableCleanUpVisitor;
import de.markusbarchfeld.spreadsheetfitnesse.token.RegularCell;
import de.markusbarchfeld.spreadsheetfitnesse.token.TableCell;
import de.markusbarchfeld.spreadsheetfitnesse.token.Tokens;

@SuppressWarnings("rawtypes")
public class RightOfTableCleanUpVisitorTest {

  @Test
  public void testFilteredTokens() throws Exception {
    // visitor removes regular cells at the right side of a table cell
    IVisitable[] inputTokens = { new EndOfLine(), new TableCell(null, null), new RegularCell(null, null),
        new EndOfLine(), new TableCell(null, null), new EndOfLine() };
    Class[] expectedOutputTokens = { 
        EndOfLine.class, TableCell.class, EndOfLine.class, TableCell.class,
        EndOfLine.class };

    RightOfTableCleanUpVisitor leftOfTableCleanUpVisitor = new RightOfTableCleanUpVisitor();
    leftOfTableCleanUpVisitor.visit(new Tokens(Arrays.asList(inputTokens)));

    Class[] actualTokenClasses = TestUtil.getClasses(leftOfTableCleanUpVisitor
        .getFilteredTokens());
    assertArrayEquals(expectedOutputTokens, actualTokenClasses);
  }
  
  @Test
  public void testNoFilteredTokens() throws Exception {
    // visitor does not remove regular cells which are not at the right side of a table cell
    IVisitable[] inputTokens = { new RegularCell(null, null),
        new EndOfLine() };
    Class[] expectedOutputTokens = { RegularCell.class,
        EndOfLine.class };

    RightOfTableCleanUpVisitor leftOfTableCleanUpVisitor = new RightOfTableCleanUpVisitor();
    leftOfTableCleanUpVisitor.visit(new Tokens(Arrays.asList(inputTokens)));

    Class[] actualTokenClasses = TestUtil.getClasses(leftOfTableCleanUpVisitor
        .getFilteredTokens());
    assertArrayEquals(expectedOutputTokens, actualTokenClasses);
  }
}
