package de.markusbarchfeld.spreadsheetfitnesse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.markusbarchfeld.spreadsheetfitnesse.macrocall.IMacroCall;
import de.markusbarchfeld.spreadsheetfitnesse.macrocall.MacroCall;
import de.markusbarchfeld.spreadsheetfitnesse.sources.PoiSource;
import de.markusbarchfeld.spreadsheetfitnesse.token.CallMacroTableVisitor;
import de.markusbarchfeld.spreadsheetfitnesse.token.CreateTableVisitor;
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
  private Stack<WikiPage> pageStack;

  public CreateMarkupFromExcelFile(File excelFile, boolean isXlsx)
      throws FileNotFoundException, IOException {
    this.excelFile = excelFile;
    this.pageStack = new Stack<WikiPage>();
    workbook = isXlsx ? new XSSFWorkbook(new FileInputStream(excelFile))
        : new HSSFWorkbook(new FileInputStream(excelFile));
  }

  public Tokens createTokens(String sheetName) {
    FormulaEvaluator formulaEvaluator = workbook.getCreationHelper()
        .createFormulaEvaluator();
    Sheet sheet = getSheet(sheetName);
    TokenGenerator tokenGenerator = new TokenGenerator(new PoiSource(sheet,
        formulaEvaluator));
    tokenGenerator.generateTokens();
    return new Tokens(tokenGenerator.getTokens());
  }

  private Sheet getSheet(String sheetName) {
    int sheetIndex = workbook.getSheetIndex(sheetName);
    if (sheetIndex == -1) {
      sheetIndex = workbook.getSheetIndex("#" + sheetName);
    }
    if (sheetIndex == -1) {
      // TODO: test case
      throw new RuntimeException("There is no sheet '" + sheetName
          + "' to create markup from");
    }
    return workbook.getSheetAt(sheetIndex);
  }

  public WikiPage getWikiMarkup(String sheetName) {
    Tokens tokens = createTokens(sheetName);
    WikiPage result = createPageFromTokens(tokens);
    result.setName(sheetName);
    return result;
  }

  public WikiPage createPageFromTokens(Tokens tokens) {
    // put on stack to enable nested macro calls
    pageStack.push(new WikiPage());
    String markup = createWikiMarkupFromTokens(tokens);
    WikiPage result = pageStack.pop();
    result.setContent(markup);
    if (!pageStack.isEmpty()) {
      pageStack.peek().addSubPage(result);
    }
    return result;
  }

  public String createWikiMarkupFromTokens(Tokens tokens) {
    // 1. Filter Tokens
    TransformerVisitor cleanUpVisitor = new RightOfTableCleanUpVisitor();
    cleanUpVisitor.visit(tokens);
    Tokens filteredTokens = cleanUpVisitor.getTransformedTokens();
    // 2. Generate Tables
    CreateTableVisitor createTableVisitor = new CreateTableVisitor();
    createTableVisitor.visit(filteredTokens);
    Tokens tokensWithTables = createTableVisitor.getTransformedTokens();
    // 3. Handle Sheet Calls
    CallMacroTableVisitor callMacroTableVisitor = new CallMacroTableVisitor(
        this);
    callMacroTableVisitor.visit(tokensWithTables);
    Tokens tokensWithCallMacros = callMacroTableVisitor.getTransformedTokens();
    // 4. Generate Markup
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
    return new MacroCall(this);
  }

  public List<String> getSheetNames() {
    List<String> sheetNames = new ArrayList<String>();
    for (int i = 0; i < workbook.getNumberOfSheets(); i += 1) {
      sheetNames.add(workbook.getSheetName(i));
    }
    return sheetNames;
  }

}
