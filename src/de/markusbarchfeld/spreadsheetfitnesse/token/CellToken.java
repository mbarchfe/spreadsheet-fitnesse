package de.markusbarchfeld.spreadsheetfitnesse.token;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyle;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyles;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;

public class CellToken {
  public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy");

  private static Log log = LogFactory.getLog(CellToken.class);

  private static DecimalFormat decimalFormat;

  public CellToken(FormulaEvaluator formulaEvaluator, Cell cell) {
    super();
    this.formulaEvaluator = formulaEvaluator;
    this.cell = cell;
  }

  private Cell cell;

  private FormulaEvaluator formulaEvaluator;

  public String getStringValue() {
    String value = "";
    switch (cell.getCellType()) {
    case HSSFCell.CELL_TYPE_STRING:
      value = cell.getStringCellValue();
      break;
    case HSSFCell.CELL_TYPE_NUMERIC:
      value = getStringValueFromNumeric();
      break;
    case HSSFCell.CELL_TYPE_BOOLEAN:
      // TODO: add test
      value = Boolean.toString(cell.getBooleanCellValue());
      break;
    case HSSFCell.CELL_TYPE_FORMULA:
      if (cell.getCachedFormulaResultType() == HSSFCell.CELL_TYPE_NUMERIC)
        value = getStringValueFromNumeric();
      else if (cell.getCachedFormulaResultType() == HSSFCell.CELL_TYPE_BOOLEAN) {
        // TODO add test
        value = Boolean.toString(cell.getBooleanCellValue());
      }
      else
        value = cell.getStringCellValue();
      break;
    default:
      log.error("unknown cell type at cell " + cell.getColumnIndex()
          + " in row " + cell.getRowIndex());
    }
    return value;
  }

  private String getStringValueFromNumeric() {
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
      value = DATE_FORMAT.format(dateCellValue);
    } else {
      value = convertDouble(cell);
    }
    return value;
  }

  private String convertDouble(Cell cell) {
    return getDecimalFormat().format(cell.getNumericCellValue());
  }
  
  
  public static DecimalFormat getDecimalFormat() {
    if (decimalFormat == null) {
      decimalFormat = new DecimalFormat("#.##########",
          DecimalFormatSymbols.getInstance(Locale.US));
      decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
    }
    return decimalFormat;
  }

  public Object getStyleName(XSSFWorkbook xssfWorkbook) {
    CellStyle cellStyle = cell.getCellStyle();
    if (cellStyle instanceof XSSFCellStyle) {
      // a very first guess at how to check for styles instead of borders
      // currently only tested from the acceptance test for RedPencilUsingMacros
      // basic unit tests needed
      XSSFCellStyle xssfCellStyle = (XSSFCellStyle) cellStyle;
      CTXf coreXf = xssfCellStyle.getCoreXf();
      long xfId = coreXf.getXfId();
      //XSSFCellStyle cellStyle2 = xssfWorkbook.getCellStyleAt((short)xfId);
      CTXf cellStyleXfAt = xssfWorkbook.getStylesSource().getCellStyleXfAt((int) xfId);
      CTStylesheet ctStylesheet = xssfWorkbook.getStylesSource().getCTStylesheet();
      CTCellStyles cellStyles = ctStylesheet.getCellStyles();
      List<CTCellStyle> cellStyleList = cellStyles.getCellStyleList();
      for (CTCellStyle ctCellStyle : cellStyleList) {
        if (ctCellStyle.getXfId() == xfId) {
          String name = ctCellStyle.getName();
          if (ctCellStyle.getCustomBuiltin()) {
            name = name.replaceAll("Excel Built-in ", "");
          }
          return name;
           
        }
      }
     
    }
    return "X";
  }
 

}
