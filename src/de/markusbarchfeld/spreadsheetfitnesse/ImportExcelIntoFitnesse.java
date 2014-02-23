package de.markusbarchfeld.spreadsheetfitnesse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Imports a single Excel file or a directory structure with Excel files into a
 * Fitnesse Wiki. In addition to Excel files it reads page content for suite
 * pages from text files named 'suite'.
 * 
 */
public class ImportExcelIntoFitnesse {

  private static Log log = LogFactory.getLog(ImportExcelIntoFitnesse.class);

  private final FitnesseRest fitnesseRest;

  public ImportExcelIntoFitnesse(FitnesseRest fitnesseRest) {
    this.fitnesseRest = fitnesseRest;
  }

  public void importFile(String fullPathToExcelFile)
      throws FileNotFoundException, IOException, UnkownFileTypeException,
      InvalidFileNameException {
    File excelFile = new File(fullPathToExcelFile);
    if (!excelFile.exists()) {
      throw new FileNotFoundException(fullPathToExcelFile + " does not exit");
    }
    if (!isXlsOrXlsx(excelFile.getName())) {
      throw new UnkownFileTypeException(
          "Only xls or xlsx files can be imported");
    }
    importFile("", excelFile);
  }

  /**
   * 
   * @param parent
   *          existing suite page in the wiki
   * @param file
   *          existing xls or xlsx file
   * @throws IOException
   * @throws FileNotFoundException
   * @throws InvalidFileNameException
   */
  private void importFile(String parentPage, File excelFile)
      throws FileNotFoundException, IOException, InvalidFileNameException {
    CreateMarkupFromExcelFile createMarkupFromExcelFile = new CreateMarkupFromExcelFile(
        excelFile, isXlsx(excelFile.getName()));
    List<String> sheetNames = createMarkupFromExcelFile.getSheetNames();
    filterIgnoredSheetNames(sheetNames);
    boolean createSubPagePerSheet = sheetNames.size() > 1;
    String excelFileName = excelFile.getName().replace(".xlsx", "")
        .replace(".xls", "");
    if (createSubPagePerSheet) {
      parentPage = fitnesseRest.createSuitePage(parentPage, excelFileName);
    }
    for (String sheetName : sheetNames) {
      String wikiMarkup = createMarkupFromExcelFile.getWikiMarkup(sheetName);
      String pageName = createSubPagePerSheet ? sheetName : excelFileName;
      log.info("Writing " + excelFile.getAbsolutePath() + " to wiki page "
          + pageName);
      fitnesseRest.save(parentPage, pageName, wikiMarkup);
    }
  }

  private void filterIgnoredSheetNames(List<String> sheetNames) {
    Iterator<String> iterator = sheetNames.iterator();
    while (iterator.hasNext()) {
      String name = (String) iterator.next();
      if (name.startsWith("!")) {
        iterator.remove();
      }
    }
  }

  public void importFolder(String absoluteFolderPath)
      throws NotADirectoryException {
    File folder = new File(absoluteFolderPath);
    if (!folder.isDirectory()) {
      throw new NotADirectoryException(absoluteFolderPath
          + " is not a directory");
    }
    importFolder("", folder.getName(), folder);
  }

  private void importFolder(String parent, String childPage, File folder) {
    String suitePage = fitnesseRest.createSuitePage(parent, childPage);
    log.info("Now filling test suite " + suitePage);

    File[] listFiles = folder.listFiles();
    for (File file : listFiles) {
      if (file.isDirectory()) {
        importFolder(suitePage, file.getName(), file);
      }
      if (isXlsOrXlsx(file.getName())) {
        try {
          importFile(suitePage, file);
        } catch (Exception e) {
          log.error("Failed to import " + file.getAbsolutePath(), e);
        }
      } else if ("suite".equals(file.getName())) {
        try {
          log.info("Writing suite page content for " + suitePage);
          String pageContent = FileUtils.readFileToString(file);
          fitnesseRest.save(suitePage, "", pageContent);
        } catch (IOException e) {
          log.error(
              "Could not write suite page content for "
                  + file.getAbsoluteFile(), e);
        }
      }
    }
  }

  private boolean isXlsOrXlsx(String name) {
    return isXlsx(name) || isXls(name);
  }

  private boolean isXls(String name) {
    return name.endsWith(".xls");
  }

  private boolean isXlsx(String name) {
    return name.endsWith(".xlsx");
  }

}
