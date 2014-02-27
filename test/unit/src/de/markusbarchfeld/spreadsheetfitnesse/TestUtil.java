package de.markusbarchfeld.spreadsheetfitnesse;

import java.util.List;

public class TestUtil {
  public static String createFullPathToUnitTestData(String string) {
    return "test/unit/data/" + string;
  }

  @SuppressWarnings("rawtypes")
  public static Class[] getClasses(List<? extends Object> objects) {
    Class[] classes = new Class[objects.size()];
    for (int i = 0; i < classes.length; i++) {
      classes[i] = objects.get(i).getClass();
    }
    return classes;
  }

}
