package de.markusbarchfeld.spreadsheetfitnesse.macrocall;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

import de.markusbarchfeld.spreadsheetfitnesse.CreateMarkupFromExcelFile;
import de.markusbarchfeld.spreadsheetfitnesse.WikiPage;
import de.markusbarchfeld.spreadsheetfitnesse.token.AddedTableCell;
import de.markusbarchfeld.spreadsheetfitnesse.token.IVisitable;
import de.markusbarchfeld.spreadsheetfitnesse.token.Tokens;

public class MacroCall implements IMacroCall {

  private CreateMarkupFromExcelFile createMarkupFromExcelFile;

  public MacroCall(CreateMarkupFromExcelFile createMarkupFromExcelFile) {
    this.createMarkupFromExcelFile = createMarkupFromExcelFile;
  }

  public Tokens createTokens(String testCaseName, String sheetName,
      KeyValue... params) throws MacroCallException {
    Workbook workbook = createMarkupFromExcelFile.getWorkbook();
    for (KeyValue keyValue : params) {
      Cell cell = getCellForParameter(keyValue);
      if (cell == null) {
        throw new MacroCallException("There is no cell named '" +keyValue.getKey()+"'");
      }
      cell.setCellValue(keyValue.getValue());

    }
    FormulaEvaluator createFormulaEvaluator = workbook.getCreationHelper()
        .createFormulaEvaluator();
    createFormulaEvaluator.evaluateAll();
    return createMarkupFromExcelFile.createTokens(sheetName);
  }

  private Cell getCellForParameter(KeyValue keyValue) {
    Workbook workbook = createMarkupFromExcelFile.getWorkbook();
    Name name = workbook.getName(keyValue.getKey());
    Cell result = null;
    if (name != null) {
      CellReference cellReference = new CellReference(name.getRefersToFormula());
      Sheet sheetOfReference = workbook.getSheet(name.getSheetName());
      Row row = sheetOfReference.getRow(cellReference.getRow());
      result = row.getCell(cellReference.getCol());
    }
    return result;
  }

  @Override
  public void call(String testCaseName, String sheetName, KeyValue... params) {
    try {
      Tokens tokens = this.createTokens(testCaseName, sheetName, params);
      WikiPage page = createMarkupFromExcelFile.createPageFromTokens(tokens);
      page.setName(testCaseName);
    } catch (MacroCallException e) {
      List<IVisitable> tokens = new ArrayList<IVisitable>();
      tokens.add(new AddedTableCell(e.getMessage()));
      Tokens errorTokens = new Tokens(tokens );
      WikiPage page = createMarkupFromExcelFile.createPageFromTokens(errorTokens);
      page.setName(testCaseName);
    }
  }

}
