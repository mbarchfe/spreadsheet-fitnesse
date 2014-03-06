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
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.markusbarchfeld.spreadsheetfitnesse.macrocall.IMacroCall;
import de.markusbarchfeld.spreadsheetfitnesse.macrocall.KeyValue;
import de.markusbarchfeld.spreadsheetfitnesse.token.CallMacroTableVisitor;
import de.markusbarchfeld.spreadsheetfitnesse.token.CreateTableVisitor;
import de.markusbarchfeld.spreadsheetfitnesse.token.IVisitable;
import de.markusbarchfeld.spreadsheetfitnesse.token.RightOfTableCleanUpVisitor;
import de.markusbarchfeld.spreadsheetfitnesse.token.TokenGenerator;
import de.markusbarchfeld.spreadsheetfitnesse.token.Tokens;
import de.markusbarchfeld.spreadsheetfitnesse.token.TransformerVisitor;
import de.markusbarchfeld.spreadsheetfitnesse.token.WikiPageMarkupVisitor;

/**
 * Opens Excel File (xls or xlsx) and converts the content into FitNesse Wiki
 * markup by driving the WikiPageContentBuilder. Handels multiple sheets.
 * 
 */
public class CreateMarkupFromExcelFile {

  private static Log log = LogFactory.getLog(CreateMarkupFromExcelFile.class);
  private Workbook workbook;
  public Workbook getWorkbook() {
    return workbook;
  }

  private final File excelFile;

  public CreateMarkupFromExcelFile(File excelFile, boolean isXlsx)
      throws FileNotFoundException, IOException {
    this.excelFile = excelFile;
    workbook = isXlsx ? new XSSFWorkbook(new FileInputStream(excelFile))
        : new HSSFWorkbook(new FileInputStream(excelFile));
  }
  
  public Tokens createTokens(String sheetName) {
    FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
    Sheet sheet = workbook.getSheetAt(workbook.getSheetIndex(sheetName));
    TokenGenerator tokenGenerator = new TokenGenerator(sheet, formulaEvaluator);
    tokenGenerator.generateTokens();
    return new Tokens(tokenGenerator.getTokens());
  }

  public String getWikiMarkup(String sheetName) {
    //Sheet sheet = workbook.getSheetAt(workbook.getSheetIndex(sheetName));
    // 1. Create Tokens from Cells
    Tokens tokens = createTokens(sheetName);
    // 2. Filter Tokens
    TransformerVisitor cleanUpVisitor = new RightOfTableCleanUpVisitor();
    cleanUpVisitor.visit(tokens);
    Tokens filteredTokens = cleanUpVisitor.getTransformedTokens();
    // 3. Generate Tables
    CreateTableVisitor createTableVisitor = new CreateTableVisitor();
    createTableVisitor.visit(filteredTokens);
    Tokens tokensWithTables = createTableVisitor.getTransformedTokens();
    // 4. Handle Sheet Calls
    CallMacroTableVisitor callMacroTableVisitor = new CallMacroTableVisitor(getSheetCall());
    callMacroTableVisitor.visit(tokensWithTables);
    Tokens tokensWithCallMacros = callMacroTableVisitor.getTransformedTokens();
    // 5. Generate Markup
    WikiPageMarkupVisitor wikiPageMarkupVisitor = new WikiPageMarkupVisitor();
    wikiPageMarkupVisitor.visit(tokensWithCallMacros);
    String pageContent = wikiPageMarkupVisitor.toString();
    if (log.isDebugEnabled()) {
      log.debug("Created page content from " + excelFile.getName() + ":");
      log.debug(pageContent);
      log.debug("---- end page content ----");
    }
    return pageContent;
  }

  public IMacroCall getSheetCall() {
    return new IMacroCall() {
      
      @Override
      public void call(String testCaseName, String sheetName, KeyValue... params) {
      }
    };
  }

  public List<String> getSheetNames() {
    List<String> sheetNames = new ArrayList<String>();
    for (int i = 0; i < workbook.getNumberOfSheets(); i += 1) {
      sheetNames.add(workbook.getSheetName(i));
    }
    return sheetNames;
  }

}
