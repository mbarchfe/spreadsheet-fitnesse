package de.markusbarchfeld.spreadsheetfitnesse;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import de.markusbarchfeld.spreadsheetfitnesse.CreateMarkupFromExcelFile;

public class CreateMarkupFromExcelTest {

  private CreateMarkupFromExcelFile createMarkupFromExcelFile;

  @Before
  public void setUp() throws Exception {
    String excelFile = "CreateMarkupFromExcel.xlsx";
    createMarkupFromExcelFile = new CreateMarkupFromExcelFile(new File(
        TestUtil.createFullPathToUnitTestData(excelFile)), true /* isXlsx */);
  }

  @Test
  public void testDate() throws Exception {
    String expectedMarkup = "|value|fitnesse markup|# comment|\n"
        + "|31.01.12|31.01.12|Date in default local (german)|\n";
    // TODO: support different locals, but currently handling is buggy in
    // LibreOffice, too
    // "|01/31/13|01/31/13|Date in US Local|";

    testSheet("Date", expectedMarkup);
  }

  @Test
  public void testNumber() throws Exception {
    String expectedMarkup = "|value|fitnesse markup|# comment|\n"
        + "|1|1|integer|\n" + "|1.2|1.2|floating|\n"
        + "|0.6666666667|'0.6666666667|rounded|\n"
        + "|7.4182114913|'7.4182114913|rounded|\n";
    testSheet("Number", expectedMarkup);
  }

  @Test
  public void testFormula() throws Exception {
    String expectedMarkup = "|value|fitnesse markup|# comment|\n"
        + "|2|2|integer|\n" + "|1.4|1.4|float|\n" + "|AB|AB|text|\n"
        + "|01.02.12|01.02.12|referencing date in default locale|\n";

    testSheet("Formula", expectedMarkup);
  }
  
  @Test
  public void testUmlaute() throws Exception {
    testSheet("Umlaute", "äöüÄÖÜ\n");
  }

  public void testSheet(String sheetName, String expectedMarkup)
      throws Exception {
    String actualMarkup = createMarkupFromExcelFile.getWikiMarkup(sheetName);
    assertEquals(expectedMarkup, actualMarkup);
  }

}
