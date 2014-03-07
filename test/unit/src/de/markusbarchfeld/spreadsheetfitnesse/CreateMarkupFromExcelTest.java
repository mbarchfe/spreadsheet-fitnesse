package de.markusbarchfeld.spreadsheetfitnesse;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.markusbarchfeld.spreadsheetfitnesse.macrocall.IMacroCall;
import de.markusbarchfeld.spreadsheetfitnesse.macrocall.KeyValue;
import de.markusbarchfeld.spreadsheetfitnesse.token.EndOfLine;
import de.markusbarchfeld.spreadsheetfitnesse.token.RegularCell;
import de.markusbarchfeld.spreadsheetfitnesse.token.TableCell;
import de.markusbarchfeld.spreadsheetfitnesse.token.Tokens;

/**
 * this is something in between a unit test and the integration tests
 * which need to start fitnesse.
 * Tests here cover not only the CreateMarkupFromExcel functionality
 * but also the functianlity from the token generator and visitors.
 * @author mbarchfe
 *
 */
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
    WikiPage actualMarkup = createMarkupFromExcelFile.getWikiMarkup(sheetName);
    assertEquals(expectedMarkup, actualMarkup.getContent());
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testTokenGenerator() throws Exception {
    Class[] expectedTokens = { EndOfLine.class, RegularCell.class,
        EndOfLine.class, TableCell.class, RegularCell.class, EndOfLine.class,
        EndOfLine.class, RegularCell.class, EndOfLine.class };
    Tokens tokens = createMarkupFromExcelFile.createTokens("Token");
    Assert.assertArrayEquals(expectedTokens, TestUtil.getClasses(tokens.asList()));
  }
  
  @Test
  public void testCallSheetFromExcelAndExtendedMarkup() throws Exception {
    // reads in excel test data and verifies that ICallSheet was called
    // and that the markup has been extended with the additional "link called"
    // column

    String expectedMarkup = "|call sheet|MacroSheet|\n"
        + "|test case name|param|page called|\n"
        + "|TestCase1|5|MacroSheetTestCase1|\n" + "\n" + "|otherFixture|\n\nDocumentation\n";

    IMacroCall sheetCallMock = mock(IMacroCall.class);

    CreateMarkupFromExcelFile createMarkupFromExcelFile = new CreateMarkupFromExcelFile(
        new File(TestUtil.createFullPathToUnitTestData("CallSheet.xlsx")), true /* isXlsx */);
    CreateMarkupFromExcelFile spiedMarkupCreator = spy(createMarkupFromExcelFile);
    stub(spiedMarkupCreator.getSheetCall()).toReturn(sheetCallMock);

    WikiPage actualMarkup = spiedMarkupCreator.getWikiMarkup("CallSheet");

    KeyValue params = new KeyValue("param", "5");
    verify(sheetCallMock).call("TestCase1", "MacroSheet", params);
    assertEquals(expectedMarkup, actualMarkup.getContent());
  }

}
