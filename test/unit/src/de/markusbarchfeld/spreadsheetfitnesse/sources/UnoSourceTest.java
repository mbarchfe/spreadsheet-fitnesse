package de.markusbarchfeld.spreadsheetfitnesse.sources;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

public class UnoSourceTest {

  @Test
  public void testOneRowOneCell() {
    // simplest case, check that content and style are available in the cell
    UNOSource unoSource = new UNOSource();
    String content = "fixturename";
    String styleName = "FitTableCell";
    unoSource.add(0, 0, content, styleName);
    Iterator<IRow> rowIterator = unoSource.getRowIterator();
    IRow iRow = (IRow) rowIterator.next();
    Assert.assertEquals(0, iRow.getRowNumber());
    Assert.assertFalse(rowIterator.hasNext());

    Iterator<ICell> cells = iRow.getCells();
    ICell cell = cells.next();
    Assert.assertFalse(cells.hasNext());
    Assert.assertEquals(content, cell.getContent());
    Assert.assertEquals(styleName, cell.getStyle());
  }
  
  @Test
  public void testTwoRowsInReverseOrder() {
    // row iteration does not depend on the input order
    UNOSource unoSource = new UNOSource();
    unoSource.add(1, 0, "c");
    unoSource.add(0, 0, "c");
    Iterator<IRow> rowIterator = unoSource.getRowIterator();
    IRow row1 = (IRow) rowIterator.next();
    Assert.assertEquals(0, row1.getRowNumber());
    
    IRow row2 = (IRow) rowIterator.next();
    Assert.assertEquals(1, row2.getRowNumber());
    
    Assert.assertFalse(rowIterator.hasNext());
  }
  
  @Test
  public void testTwoCellsInReverseOrder() {
    // cell iteration does not depend on the input order
    UNOSource unoSource = new UNOSource();
    unoSource.add(5, 2, "c");
    unoSource.add(5, 0, "c");
    Iterator<IRow> rowIterator = unoSource.getRowIterator();
    IRow row = (IRow) rowIterator.next();
    Iterator<ICell> cells = row.getCells();
    Assert.assertEquals(0, cells.next().getColumnNumber());
    Assert.assertEquals(2, cells.next().getColumnNumber());
  }
  
  @Test
  public void testCellAddedTwice() {
    // if a cell is added twice, the second add overwrites the first
    UNOSource unoSource = new UNOSource();
    unoSource.add(1, 2, "a");
    unoSource.add(1, 2, "b");
    Iterator<IRow> rowIterator = unoSource.getRowIterator();
    IRow row = (IRow) rowIterator.next();
    Iterator<ICell> cells = row.getCells();
    Assert.assertEquals("b", cells.next().getContent());
    Assert.assertFalse(cells.hasNext());
    
  }
  
  /*
   * integration test which covers the use case of the libreoffice extension:
   * Cells are added to a UNOSource, tokens are generated and then JSon is created.
   * In the libreoffice extension this JSON then is sent to fit websocket server for evaluation.
   */
    @Test
    public void testToJson() {
      UNOSource unoSource = new UNOSource();
      unoSource.add(1, 0, "fixtureName", "FitTableHeader");
      unoSource.add(2, 1, "header1", "FitTableHeader");
      unoSource.add(3, 1, "content1", "FitTableCell");
      unoSource.add(2, 2, "header2", "FitTableHeader");
      unoSource.add(3, 2, "content2", "FitTableCell");
      
      Assert
          .assertEquals(
              "[[[\"fixtureName\"],[\"header1\",\"header2\"],[\"content1\",\"content2\"]]]",
              unoSource.toJSON());
    }
  
  
}
