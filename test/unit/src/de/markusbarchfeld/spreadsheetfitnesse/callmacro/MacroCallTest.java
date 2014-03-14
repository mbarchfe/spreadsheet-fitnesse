package de.markusbarchfeld.spreadsheetfitnesse.callmacro;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

import de.markusbarchfeld.spreadsheetfitnesse.CreateMarkupFromExcelFile;
import de.markusbarchfeld.spreadsheetfitnesse.TestUtil;
import de.markusbarchfeld.spreadsheetfitnesse.macrocall.KeyValue;
import de.markusbarchfeld.spreadsheetfitnesse.macrocall.MacroCall;
import de.markusbarchfeld.spreadsheetfitnesse.macrocall.MacroCallException;
import de.markusbarchfeld.spreadsheetfitnesse.token.RegularCell;
import de.markusbarchfeld.spreadsheetfitnesse.token.Tokens;

public class MacroCallTest {


  public MacroCall createMacroCall(String excelFile) throws Exception {
    CreateMarkupFromExcelFile createMarkupFromExcelFile = new CreateMarkupFromExcelFile(
        new File(TestUtil.createFullPathToUnitTestData(excelFile)), true /* isXlsx */);
    return new MacroCall(createMarkupFromExcelFile);
  }

  @Test
  public void testParamSubstitutionAndFormulaEvalution() throws Exception {

    Tokens actualTokens = createMacroCall("CallSheet.xlsx").createTokens("testCaseName", "MacroSheet",
        new KeyValue("param", "2"));
    RegularCell paramCell = (RegularCell) actualTokens.asList().get(4);

    assertEquals("parameter has been set", "2", paramCell.getStringValue());
    RegularCell formulaCell = (RegularCell) actualTokens.asList().get(8);

    assertEquals("formula is updated", "4", formulaCell.getStringValue());
  }
  
  @Test
  public void testParamSubstitutionAndFormulaEvalutionWithDate() throws Exception {

    Tokens actualTokens = createMacroCall("CallSheetDateParameter.xlsx").createTokens("testCaseName", "DateMacroSheet",
        new KeyValue("dateParam", "01.06.70"));
    RegularCell paramCell = (RegularCell) actualTokens.asList().get(1);

    assertEquals("parameter has been set", "01.06.70", paramCell.getStringValue());
    RegularCell formulaCell = (RegularCell) actualTokens.asList().get(5);

    assertEquals("formula is updated", "02.06.70", formulaCell.getStringValue());
  }

  @Test()
  public void testInvalidParameter() throws Exception {
    try {
      createMacroCall("CallSheet.xlsx").createTokens("testCaseName", "MacroSheet", new KeyValue(
          "invalidParameter", "2"));
      fail("MacroCallException not thrown");
    } catch (MacroCallException ex) {
      assertEquals("There is no cell named 'invalidParameter'", ex.getMessage());
    }

  }
}
