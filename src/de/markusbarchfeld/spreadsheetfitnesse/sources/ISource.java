package de.markusbarchfeld.spreadsheetfitnesse.sources;

import java.util.Iterator;

public interface ISource {
    Iterator<IRow> getRowIterator();
    /*
    ITableCell createTableCell();
    IRegularCell createRegularCell();
    */
}
