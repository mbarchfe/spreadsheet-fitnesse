package de.markusbarchfeld.spreadsheetfitnesse;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyle;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyles;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;

public class StyleHelper {
  private XSSFWorkbook workbook = null;

  public StyleHelper(Workbook workbook) {
    if (workbook instanceof XSSFWorkbook) {
      this.workbook = (XSSFWorkbook) workbook;
    }
  }

  /**
   * Retrieves the cell style name. Works only for xlsx, not for xls. If the
   * spreadsheet user did not choose any particular cell style, the standard
   * cell style is 'Normal'.
   * 
   * @param cell
   * @return the style name or null
   */
  public String getCellStyleName(Cell cell) {
    String result = null;
    if (workbook != null) {
      CellStyle cellStyle = cell.getCellStyle();
      if (cellStyle instanceof XSSFCellStyle) {
        result = getCellStyleName((XSSFCellStyle) cellStyle);
      }

    }
    return result;
  }

  private String getCellStyleName(XSSFCellStyle xssfCellStyle) {
    CTXf coreXf = xssfCellStyle.getCoreXf();
    long xfId = coreXf.getXfId();

    CTCellStyle ctCellStyle = getCTCellStyleForXFID(xfId);
    String name = ctCellStyle.getName();
    if (name != null && ctCellStyle.getCustomBuiltin()) {
      name = name.replaceAll("Excel Built-in ", "");
    }
    return name;
  }

  /*
   * Get the cell style from the list of cell styles in the styles.xml file This
   * relies on the assumption, that there is a one to one mapping between the
   * cell style and xfid entry. i.e. the list of xfids used in the cellstyles is
   * unique.
   */
  private CTCellStyle getCTCellStyleForXFID(long xfId) {
    CTStylesheet ctStylesheet = workbook.getStylesSource().getCTStylesheet();
    CTCellStyle result = null;
    CTCellStyles cellStyles = ctStylesheet.getCellStyles();
    List<CTCellStyle> cellStyleList = cellStyles.getCellStyleList();
    for (CTCellStyle ctCellStyle : cellStyleList) {
      if (ctCellStyle.getXfId() == xfId) {
        result = ctCellStyle;
        break;
      }
    }
    return result;
  }
}
