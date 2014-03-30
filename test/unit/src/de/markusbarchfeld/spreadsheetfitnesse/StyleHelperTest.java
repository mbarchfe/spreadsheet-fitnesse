package de.markusbarchfeld.spreadsheetfitnesse;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class StyleHelperTest {

  @Test
  public void testCellStyle() throws Exception {
    File file = new File(
        TestUtil.createFullPathToUnitTestData("UsingStyles.xlsx"));
    XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(file));
    XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
    XSSFCell headerCell = sheet.getRow(0).getCell(0);
    XSSFCell regularCell = sheet.getRow(1).getCell(0);
    XSSFCell descriptionCell = sheet.getRow(2).getCell(0);
    StyleHelper styleHelper = new StyleHelper(xssfWorkbook);

    assertEquals("Header", styleHelper.getCellStyleName(headerCell));

    assertEquals("Normal", styleHelper.getCellStyleName(regularCell));

    assertEquals("Description", styleHelper.getCellStyleName(descriptionCell));

  }
}
