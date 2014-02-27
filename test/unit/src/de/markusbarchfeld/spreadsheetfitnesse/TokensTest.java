package de.markusbarchfeld.spreadsheetfitnesse;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import de.markusbarchfeld.spreadsheetfitnesse.token.EndOfLine;
import de.markusbarchfeld.spreadsheetfitnesse.token.IVisitable;
import de.markusbarchfeld.spreadsheetfitnesse.token.RegularCell;
import de.markusbarchfeld.spreadsheetfitnesse.token.Tokens;

@SuppressWarnings("rawtypes")
public class TokensTest {

  @Test
  public void testOneOfTwoTrailingEndOfLinesArePurged() {
    IVisitable[] inputTokens = { new EndOfLine(), new RegularCell(null, null),
        new EndOfLine(), new EndOfLine() };
    Class[] expectedOutputTokens = {  EndOfLine.class, RegularCell.class, EndOfLine.class };
    actAndAssert(inputTokens, expectedOutputTokens);

  }
  
  @Test
  public void testOneTrailingEndOfLineIsNotTouched() {
    IVisitable[] inputTokens = { new EndOfLine(), new RegularCell(null, null),
        new EndOfLine()};
    Class[] expectedOutputTokens = {  EndOfLine.class, RegularCell.class, EndOfLine.class };
    actAndAssert(inputTokens, expectedOutputTokens);
  }
  
  @Test
  public void testNoTrailingEndOfLinesArePurged() {
    IVisitable[] inputTokens = {new RegularCell(null, null) };
    Class[] expectedOutputTokens = { RegularCell.class };
    actAndAssert(inputTokens, expectedOutputTokens);
  }

  private void actAndAssert(IVisitable[] inputTokens,
      Class[] expectedOutputTokens) {
    Tokens tokens = new Tokens(Arrays.asList(inputTokens));
    Assert.assertArrayEquals(expectedOutputTokens,
        TestUtil.getClasses(tokens.asList()));
  }
  

}
