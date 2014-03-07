package de.markusbarchfeld.spreadsheetfitnesse.callmacro;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import de.markusbarchfeld.spreadsheetfitnesse.CreateMarkupFromExcelFile;
import de.markusbarchfeld.spreadsheetfitnesse.TestUtil;
import de.markusbarchfeld.spreadsheetfitnesse.macrocall.KeyValue;
import de.markusbarchfeld.spreadsheetfitnesse.macrocall.MacroCall;
import de.markusbarchfeld.spreadsheetfitnesse.macrocall.MacroCallException;
import de.markusbarchfeld.spreadsheetfitnesse.token.RegularCell;
import de.markusbarchfeld.spreadsheetfitnesse.token.Tokens;

public class MacroCallTest {

  private MacroCall macroCall;

  @Before
  public void setUp() throws Exception {
    String excelFile = "CallSheet.xlsx";
    CreateMarkupFromExcelFile createMarkupFromExcelFile = new CreateMarkupFromExcelFile(
        new File(TestUtil.createFullPathToUnitTestData(excelFile)), true /* isXlsx */);
    macroCall = new MacroCall(createMarkupFromExcelFile);
  }

  @Test
  public void testParamSubstitutionAndFormulaEvalution() throws Exception {

    Tokens actualTokens = macroCall.createTokens("testCaseName", "MacroSheet",
        new KeyValue("param", "2"));
    RegularCell paramCell = (RegularCell) actualTokens.asList().get(4);

    assertEquals("parameter has been set", "2", paramCell.getStringValue());
    RegularCell formulaCell = (RegularCell) actualTokens.asList().get(8);

    assertEquals("formula is updated", "4", formulaCell.getStringValue());
  }

  @Test()
  public void testInvalidParameter() {
    try {
      macroCall.createTokens("testCaseName", "MacroSheet", new KeyValue(
          "invalidParameter", "2"));
      fail("MacroCallException not thrown");
    } catch (MacroCallException ex) {
      assertEquals("There is no cell named 'invalidParameter'", ex.getMessage());
    }

  }
}
