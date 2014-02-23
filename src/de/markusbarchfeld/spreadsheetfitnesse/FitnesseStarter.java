package de.markusbarchfeld.spreadsheetfitnesse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fitnesseMain.FitNesseMain;

public class FitnesseStarter {
  
  public static void start(String rootDirectory) throws Exception {
    start(new String[] {"-d", rootDirectory});
  }
  
  static void start(String[] fitnesseArgs) throws Exception {
    List<String> allArgs = new ArrayList<String>();
    allArgs.addAll(Arrays.asList("-o", "-e", "0", "-p", "8080"));
    allArgs.addAll(Arrays.asList(fitnesseArgs));
    FitNesseMain.main(allArgs.toArray(new String[allArgs.size()]));
  }
}
