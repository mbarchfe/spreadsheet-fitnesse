package de.markusbarchfeld.spreadsheetfitnesse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Opens Excel File (xls or xlsx) and converts the content into FitNesse Wiki
 * markup by driving the WikiPageContentBuilder. Handels multiple sheets.
 * 
 */
public class CreateMarkupFromExcelFile {

  private static Log log = LogFactory.getLog(CreateMarkupFromExcelFile.class);
  private Workbook workbook;
  private final File excelFile;

  public CreateMarkupFromExcelFile(File excelFile, boolean isXlsx)
      throws FileNotFoundException, IOException {
    this.excelFile = excelFile;
    workbook = isXlsx ? new XSSFWorkbook(new FileInputStream(excelFile))
        : new HSSFWorkbook(new FileInputStream(excelFile));
  }

  public String getWikiMarkup(String sheetName) {
    Sheet sheet = workbook.getSheetAt(workbook.getSheetIndex(sheetName));

    WikiPageContentBuilder wikiPageContentBuilder = new WikiPageContentBuilder(
        workbook.getCreationHelper().createFormulaEvaluator());
    for (int rowIndex = sheet.getFirstRowNum(); rowIndex <= sheet
        .getLastRowNum(); rowIndex += 1) {
      Row row = sheet.getRow(rowIndex);
      if (row != null && row.getFirstCellNum() >= 0) {
        for (int colIndex = row.getFirstCellNum(); colIndex <= row
            .getLastCellNum(); colIndex += 1) {
          Cell cell = row.getCell(colIndex);
          if (cell != null) {
            wikiPageContentBuilder.appendCell(cell);
          }
        }
        wikiPageContentBuilder.startNewRow();
      }
    }
    String pageContent = wikiPageContentBuilder.toString();
    if (log.isDebugEnabled()) {
      log.debug("Created page content from " + excelFile.getName() + ":");
      log.debug(pageContent);
      log.debug("---- end page content ----");
    }
    return pageContent;
  }

  public List<String> getSheetNames() {
    List<String> sheetNames = new ArrayList<String>();
    for (int i = 0; i < workbook.getNumberOfSheets(); i += 1) {
      sheetNames.add(workbook.getSheetName(i));
    }
    return sheetNames;
  }

}
