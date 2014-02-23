package de.markusbarchfeld.spreadsheetfitnesse;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class WikiPageContentBuilder {

  private static Log log = LogFactory.getLog(WikiPageContentBuilder.class);
  private boolean writingBordered = false;
  private boolean isAtRightSideOfTable = false;
  StringBuilder stringBuilder = new StringBuilder();
  private FormulaEvaluator formulaEvaluator;

  public WikiPageContentBuilder(FormulaEvaluator createFormulaEvaluator) {
    this.formulaEvaluator = createFormulaEvaluator;

  }

  public void appendCell(Cell cell) {
    boolean isBordered = cell.getCellStyle().getBorderLeft() != HSSFCellStyle.BORDER_NONE;
    if (isBordered) {
      startBordered();
    } else {
      isAtRightSideOfTable = isAtRightSideOfTable || writingBordered;
      endBordered();
      writingBordered = false;
    }
    if (!isAtRightSideOfTable) {
      stringBuilder.append(getStringValue(cell));
    }

    if (isBordered) {
      stringBuilder.append("|");
    }

  }

  private void endBordered() {
    writingBordered = false;
  }

  private void startBordered() {
    if (!writingBordered) {
      stringBuilder.append("|");

    }
    writingBordered = true;

  }

  public void startNewRow() {
    endBordered();
    isAtRightSideOfTable = false;
    stringBuilder.append("\n");
  }

  private String getStringValue(Cell cell) {
    String value = "";
    switch (cell.getCellType()) {
    case HSSFCell.CELL_TYPE_STRING:
      value = cell.getStringCellValue();
      break;
    case HSSFCell.CELL_TYPE_NUMERIC:
      value = getStringValueFromNumeric(cell);
      break;
    case HSSFCell.CELL_TYPE_FORMULA:
      if (cell.getCachedFormulaResultType() == HSSFCell.CELL_TYPE_NUMERIC)
        value = getStringValueFromNumeric(cell);
      else
        value = cell.getStringCellValue();
      break;
    default:
      log.error("unknown cell type at cell " + cell.getColumnIndex()
          + " in row " + cell.getRowIndex());
    }
    return value;
  }

  private String getStringValueFromNumeric(Cell cell) {
    String value;
    if (DateUtil.isCellDateFormatted(cell)) {
      Locale.setDefault(Locale.GERMANY);
      // does not work, Locale is ignored: new
      // DataFormatter(Locale.GERMANY).formatCellValue(cell,
      // this.formulaEvaluator );
      new DataFormatter(Locale.GERMANY).formatCellValue(cell,
          this.formulaEvaluator);
      Date dateCellValue = cell.getDateCellValue();
      // also not possible: value = new
      // ExcelStyleDateFormatter(cell.getCellStyle().getDataFormatString(),
      // Locale.GERMANY).format(dateCellValue);
      // temp implementation for German locale
      value = new SimpleDateFormat("dd.MM.yy").format(dateCellValue);
    } else {
      value = convertDouble(cell);
    }
    return value;
  }

  private String convertDouble(Cell cell) {
    DecimalFormat decimalFormat = new DecimalFormat("#.##########",
        DecimalFormatSymbols.getInstance(Locale.US));
    decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
    return decimalFormat.format(cell.getNumericCellValue());
  }

  @Override
  public String toString() {
    return stringBuilder.toString();
  }
}
