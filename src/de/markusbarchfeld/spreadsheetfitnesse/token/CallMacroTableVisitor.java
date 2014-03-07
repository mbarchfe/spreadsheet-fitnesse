package de.markusbarchfeld.spreadsheetfitnesse.token;

import java.util.ArrayList;
import java.util.List;

import de.markusbarchfeld.spreadsheetfitnesse.CreateMarkupFromExcelFile;
import de.markusbarchfeld.spreadsheetfitnesse.WikiPage;
import de.markusbarchfeld.spreadsheetfitnesse.macrocall.IMacroCall;
import de.markusbarchfeld.spreadsheetfitnesse.macrocall.KeyValue;

public class CallMacroTableVisitor extends TransformerVisitor {

  class NoCallSheetTable {
    protected void visitArgumentCell(TableCell tableCell) {
    }

    protected void visitHeaderCell(TableCell tableCell) {
    }

    protected void visitCell(TableCell tableCell) {
    }

    protected void call(int rowNo) {
    }

    public void endOfHeader(TableRow tableRow) {
    }

  }

  class CallSheetTable extends NoCallSheetTable{
    private String sheetName;
    private int cellCount = 0;
    private List<String> paramKeys = new ArrayList<String>();

    private String testCaseName;
    private List<String> paramValues;

    public CallSheetTable() {
      resetRow();
    }

    protected void visitArgumentCell(TableCell tableCell) {
      this.sheetName = tableCell.getStringValue();
    }

    protected void visitHeaderCell(TableCell tableCell) {
      String content = tableCell.getStringValue();
      if (cellCount == 0) {
        if ("test case name".equals(cellCount)) {
          error("First header cell is supposed to be 'test case name'");
        }
      } else {
        paramKeys.add(content);
      }
      cellCount += 1;
    }

    private void error(String string) {

    }

    protected void visitCell(TableCell tableCell) {
      if (testCaseName == null) {
        testCaseName = tableCell.getStringValue();
      } else {
        paramValues.add(tableCell.getStringValue());
      }
    }

    protected void call(int rowNo) {
      currentTableToken.add(new AddedTableCell(sheetName+testCaseName), rowNo);
      createMarkupFromSheet.getSheetCall().call(testCaseName, sheetName, getParams());
      resetRow();
    }
    
    public void endOfHeader(TableRow tableRow) {
      currentTableToken.add(new AddedTableCell("page called"), 1);
    }

    private KeyValue[] getParams() {
      int results = Math.min(paramKeys.size(), paramValues.size());
      KeyValue[] result = new KeyValue[results];
      for (int i = 0; i < results; i++) {
        result[i] = new KeyValue(paramKeys.get(i), paramValues.get(i));
      }
      return result;
    }

    protected void resetRow() {
      testCaseName = null;
      paramValues = new ArrayList<String>();
    }
    

  }

  private CreateMarkupFromExcelFile createMarkupFromSheet;
  private NoCallSheetTable sheetCaller = null;
  private int rowNo;
  private Table currentTableToken;
 

  public CallMacroTableVisitor(CreateMarkupFromExcelFile createMarkupFromExcelFile) {
    this.createMarkupFromSheet = createMarkupFromExcelFile;
  }

  @Override
  public void visitRegularCell(RegularCell regularCell) {
    transformedTokens.add(regularCell);
  }

  @Override
  public void visitEndOfLine(EndOfLine endOfLine) {
    transformedTokens.add(endOfLine);
  }
  


  @Override
  public void visitTableCell(TableCell tableCell) {
    if (sheetCaller == null) {
      boolean isCallSheetTable = "call sheet".equals(tableCell.getStringValue());
      sheetCaller = isCallSheetTable ? new CallSheetTable() : new NoCallSheetTable();
    } else {
      if (rowNo == 0) {
        sheetCaller.visitArgumentCell(tableCell);
      } else if (rowNo == 1) {
        sheetCaller.visitHeaderCell(tableCell);
      } else {
        sheetCaller.visitCell(tableCell);
      }
    }
  }

  @Override
  public void visitTableRow(TableRow tableRow) {
    if (rowNo == 0 ) {
      // nothing
    } else if (rowNo == 1) {
      sheetCaller.endOfHeader(tableRow);
    } else {
      sheetCaller.call(rowNo);      
    }
    rowNo +=1;
  }
  
  public void visitTableBeforeRows(Table table) {
    rowNo = 0;
    currentTableToken = table;
  }

  public void visitTable(Table table) {
    transformedTokens.add(currentTableToken);
    currentTableToken = null;
    sheetCaller = null;
  }
 

  public void visit(Tokens tokens) {
    tokens.accept(this);
  }
}
