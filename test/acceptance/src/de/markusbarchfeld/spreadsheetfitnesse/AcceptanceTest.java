package de.markusbarchfeld.spreadsheetfitnesse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AcceptanceTest {

  private FitnesseRest fitnesseRest;
  private ImportExcelIntoFitnesse importExcelIntoFitnesse;

  @BeforeClass
  public static void startFitnesse() throws Exception {

    File dir = new File("build/acceptancetests");
    if (dir.exists()) {
      FileUtils.deleteDirectory(dir);
    }
    dir.mkdirs();
    FitnesseStarter.start(dir.getAbsolutePath());
  }

  @Before
  public void setUp() {

    fitnesseRest = new FitnesseRest();
    importExcelIntoFitnesse = new ImportExcelIntoFitnesse(fitnesseRest);
  }

  @Test
  public void testSpreadsheetNameNotCamelCase() throws Exception {

    // wiki page names must conform certain rules, e.g. they must be camel cased
    // with at least two upper case letters
    // for details see ImportExcelIntoFitnessTest

    try {
      importExcelIntoFitnesse
          .importFile(createFullPathToAcceptanceTestData("nocamelcase.xls"));
      fail("Exception not thrown");
    } catch (RuntimeException re) {
      assertEquals("Invalid page name 'nocamelcase'", re.getMessage());
    }

  }

  private String createFullPathToAcceptanceTestData(String string) {
    return "test/acceptance/data/" + string;
  }

  @Test
  public void testFolderHierarchy() throws Exception {
    importExcelIntoFitnesse
        .importFolder(createFullPathToAcceptanceTestData("TestSuite"));

    assertEquals("Level 1\n",
        fitnesseRest.getPageContent("TestSuite.Level1Test"));
    assertEquals("Level 2\n",
        fitnesseRest.getPageContent("TestSuite.SubFolder.Level2Test"));
    assertEquals("!contents overwritten",
        fitnesseRest.getPageContent("TestSuite"));
    assertEquals("!contents overwritten SubFolder",
        fitnesseRest.getPageContent("TestSuite.SubFolder"));
  }

  @Test
  public void testRunRedPencil() throws Exception {
    importExcelIntoFitnesse
        .importFolder(createFullPathToAcceptanceTestData("RedPencil"));
    String resultsPage = fitnesseRest
        .runSuite("RedPencil.RedPencilSpecification");
    String expectedResult = "16 right, 0 wrong, 0 ignored, 0 exceptions";
    assertTrue(resultsPage.contains(expectedResult));
  }

  @Test
  public void testMultipleSheets() throws Exception {
    // creates two pages for sheets A and B ignores sheets which names start
    // with exclamation mark
    importExcelIntoFitnesse
        .importFile(createFullPathToAcceptanceTestData("MultipleSheets.xls"));
    assertEquals("A\n", fitnesseRest.getPageContent("MultipleSheets.SheetOne"));
    assertEquals("B\n", fitnesseRest.getPageContent("MultipleSheets.SheetTwo"));
    assertNull(fitnesseRest.getPageContent("MultipleSheets.!ignored"));
  }

}
