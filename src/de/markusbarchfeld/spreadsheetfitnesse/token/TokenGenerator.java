package de.markusbarchfeld.spreadsheetfitnesse.token;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;

public class TokenGenerator {

  private static Log log = LogFactory.getLog(TokenGenerator.class);
  private FormulaEvaluator formulaEvaluator;
  private Sheet sheet;
  private List<IVisitable> tokens = new ArrayList<IVisitable>();

  public TokenGenerator(Sheet sheet, FormulaEvaluator formulaEvaluator) {
    this.sheet = sheet;
    this.formulaEvaluator = formulaEvaluator;
  }

  protected boolean isBordered(Cell cell) {
    CellStyle cellStyle = cell.getCellStyle();
    if (cellStyle instanceof XSSFCellStyle) {
      // a very first guess at how to check for styles instead of borders
      // currently only tested from the acceptance test for RedPencilUsingMacros
      // basic unit tests needed
      XSSFCellStyle xssfCellStyle = (XSSFCellStyle) cellStyle;
      CTXf coreXf = xssfCellStyle.getCoreXf();
      return coreXf.getBorderId() == 1;
    }
    return cell.getCellStyle().getBorderLeft() != HSSFCellStyle.BORDER_NONE;
  }

  public void generateTokens() {
    int lastIndex = -1;
    for (int rowIndex = sheet.getFirstRowNum(); rowIndex <= sheet
        .getLastRowNum(); rowIndex += 1) {
      int rowDelta = rowIndex - lastIndex;
      if (rowDelta > 1) {
        for (int i = 1; i < rowDelta; i++) {
          addToken(new EndOfLine());
        }
      }
      lastIndex = rowIndex;
      Row row = sheet.getRow(rowIndex);
      if (row != null && row.getFirstCellNum() >= 0) {
        for (int colIndex = row.getFirstCellNum(); colIndex <= row
            .getLastCellNum(); colIndex += 1) {
          
          Cell cell = row.getCell(colIndex);
          log.debug(rowIndex + "," +colIndex + ": " + cell);
          if (cell != null) {
            if (isBordered(cell)) {
              addToken(new TableCell(formulaEvaluator, cell));
            } else {
              addToken(new RegularCell(formulaEvaluator, cell));
            }
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
