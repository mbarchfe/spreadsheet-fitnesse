package de.markusbarchfeld.spreadsheetfitnesse.macrocall;


public interface IMacroCall {
    String call(String testCaseName, String sheetName, KeyValue... params );
}
