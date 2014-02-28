package de.markusbarchfeld.spreadsheetfitnesse;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.InOrder;

import de.markusbarchfeld.spreadsheetfitnesse.token.IVisitor;
import de.markusbarchfeld.spreadsheetfitnesse.token.Table;
import de.markusbarchfeld.spreadsheetfitnesse.token.TableCell;
import de.markusbarchfeld.spreadsheetfitnesse.token.TableRow;

public class TableVisitingTest {

  @Test
  public void testTableVisiting() throws Exception {
    TableCell tableCell1 = new TableCell(null, null);
    TableCell tableCell2 = new TableCell(null, null);
    TableCell tableCell3 = new TableCell(null, null);
    // second part, assertions should probably be on the number of rows and
    // cells created
    // not on the visiting
    Table table = new Table();
    table.add(tableCell1);
    table.newRow();
    table.add(tableCell2);
    table.add(tableCell3);
    table.newRow();

    IVisitor visitor = mock(IVisitor.class);
    table.accept(visitor);
    InOrder inOrder = inOrder(visitor);
    inOrder.verify(visitor).visitTableCell(tableCell1);

    inOrder.verify(visitor).visitTableRow(any(TableRow.class));
    inOrder.verify(visitor).visitTableCell(tableCell2);
    inOrder.verify(visitor).visitTableCell(tableCell3);
    inOrder.verify(visitor).visitTableRow(any(TableRow.class));
  }

}
