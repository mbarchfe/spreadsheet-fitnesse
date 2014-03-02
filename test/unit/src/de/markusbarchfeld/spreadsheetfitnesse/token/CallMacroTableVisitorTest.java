package de.markusbarchfeld.spreadsheetfitnesse.token;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import de.markusbarchfeld.spreadsheetfitnesse.TestUtil;
import de.markusbarchfeld.spreadsheetfitnesse.macrocall.IMacroCall;
import de.markusbarchfeld.spreadsheetfitnesse.macrocall.KeyValue;

public class CallMacroTableVisitorTest {

  private String sheetName;
  private String testCaseName;
  private String paramKey;
  private String paramValue;
  private Table table;

  @Before
  public void setUp() {
    sheetName = "MySheet";
    testCaseName = "SimpleTestCase";
    paramKey = "param";
    paramValue = "5";

    table = new Table();
    table.add(tableCellWithContent("call sheet"));
    table.add(tableCellWithContent(sheetName));
    table.newRow();
    table.add(tableCellWithContent("test case name"));
    table.add(tableCellWithContent(paramKey));
    table.newRow();
    table.add(tableCellWithContent(testCaseName));
    table.add(tableCellWithContent(paramValue));
    table.newRow();
  }

  @Test
  public void testMacroCallInterfaceIsCalled() throws Exception {

    IMacroCall sheetCallMock = mock(IMacroCall.class);

    CallMacroTableVisitor callMacroTableVisitor = new CallMacroTableVisitor(
        sheetCallMock);
    table.accept(callMacroTableVisitor);

    KeyValue params = new KeyValue(paramKey, "5");
    verify(sheetCallMock).call(eq(testCaseName), eq(sheetName), eq(params));
  }

  @Test
  public void testNoTokensAreAddedForRegularTables() throws Exception {
    // testing here that the correct number and type of tokens are added
    // the content is tested from CreateMarkupFromExcelTest
    IMacroCall sheetCallMock = mock(IMacroCall.class);

    table = new Table();
    table.add(tableCellWithContent("some fixture"));
    table.newRow();
    table.add(tableCellWithContent("content"));
    table.newRow();
    CallMacroTableVisitor callMacroTableVisitor = new CallMacroTableVisitor(
        sheetCallMock);
    table.accept(callMacroTableVisitor);

    assertEquals("1 cell in first row", 1, table.rows.get(0).cells.size());
    assertEquals("1 cell in second row", 1, table.rows.get(1).cells.size());

  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testCellAreAddedForDisplayingLinkToMacro() throws Exception {

    IMacroCall sheetCallMock = mock(IMacroCall.class);

    CallMacroTableVisitor callMacroTableVisitor = new CallMacroTableVisitor(
        sheetCallMock);
    table.accept(callMacroTableVisitor);

    Class[] tokenClasses = TestUtil.getClasses(callMacroTableVisitor
        .getTransformedTokens().asList());

    AddedTableCell addedCellForHeader = (AddedTableCell) table.rows.get(1).cells
        .get(2);
    assertEquals("page called", addedCellForHeader.getStringValue());

    AddedTableCell addedCellForMacroCall = (AddedTableCell) table.rows.get(2).cells
        .get(2);
    assertEquals(this.sheetName + this.testCaseName,
        addedCellForMacroCall.getStringValue());

    assertEquals(Table.class, tokenClasses[0]);
    assertEquals(1, tokenClasses.length);

  }

  private TableCell tableCellWithContent(String returnContent) {
    TableCell result = spy(new TableCell(null, null));
    doReturn(returnContent).when(result).getStringValue();
    return result;
  }

}
