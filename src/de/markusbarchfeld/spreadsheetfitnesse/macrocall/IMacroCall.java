package de.markusbarchfeld.spreadsheetfitnesse.macrocall;


public interface IMacroCall {
    void call(String testCaseName, String sheetName, KeyValue... params );
}
