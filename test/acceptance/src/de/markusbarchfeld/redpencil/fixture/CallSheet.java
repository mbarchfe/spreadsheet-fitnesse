package de.markusbarchfeld.redpencil.fixture;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.markusbarchfeld.spreadsheetfitnesse.FitnesseRest;

/** 
 * A fixture to call parameterized macro pages in order to 
 * mark them as passed or failed.
 * 
 * This is taking place in the slim client process, which runs a request
 * to the fitnesse server. This is probably not ideal. Instead, it should
 * be handled on the server side when the slim table is processed there.
 *
 */
public class CallSheet {

  private static final String PAGE_CALLED_COLUMN_HEADER = "page called";

  public CallSheet(String sheetName) {
  }

  public List<List<String>> doTable(List<List<String>> table) {
    List<List<String>> result = createEmptyCells(table);
    List<String> header = table.get(0);
    if (table.size() > 0) {
      for (int rowIndex = 1; rowIndex < result.size(); rowIndex++) {
        List<String> row = table.get(rowIndex);
        for (int col = 0; col < row.size(); col++) {
          String cell = row.get(col);
          if (header.size() > col
              && header.get(col).equals(PAGE_CALLED_COLUMN_HEADER)) {
            String filePartUrl = extractUrlFromAnchorElement(cell);
            String cellresult = null;
            if (filePartUrl == null) {
              cellresult = "could not extract url from cell '"
                  + PAGE_CALLED_COLUMN_HEADER + "'";
            } else if (hasTestErrors(filePartUrl)) {
              cellresult =  cell;
            } else {
              cellresult = "pass";
            }
            result.get(rowIndex).remove(col);
            result.get(rowIndex).add(col, cellresult);
          }

        }
      }
    }
    return result;
  }

  private String extractUrlFromAnchorElement(String cell) {
    String result = null;
    int start = cell.indexOf("\"");
    int end = cell.lastIndexOf("\"");
    if (end > -1) {
      result = cell.substring(start + 1, end);
    }
    return result;
  }

  private List<List<String>> createEmptyCells(List<List<String>> table) {
    ArrayList<List<String>> result = new ArrayList<List<String>>();
    for (List<String> existingRow : table) {
      List<String> newRow = new ArrayList<String>();
      for (int i = 0; i < existingRow.size(); i++) {
        newRow.add("");
      }
      result.add(newRow);
    }
    return result;
  }

  private boolean hasTestErrors(String filePartUrl) {
    try {
      String runSuiteResult = new FitnesseRest().runSuite(filePartUrl,
          "format=xml");
      DocumentBuilder documentBuilder;
      documentBuilder = DocumentBuilderFactory.newInstance()
          .newDocumentBuilder();
      ByteArrayInputStream is = new ByteArrayInputStream(
          runSuiteResult.getBytes("UTF-8"));
      Document xmlResult = documentBuilder.parse(is);
      NodeList wrongElements = xmlResult.getElementsByTagName("wrong");
      int wrongcells = 0;
      for (int j = 0; j < wrongElements.getLength(); j++) {
        Node wrong = wrongElements.item(j);
        try {
          wrongcells += Integer.parseInt(wrong.getTextContent());
        } catch (NumberFormatException nfe) {
          wrongcells += 1;
        }
      }
      return wrongcells > 0;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
