package de.markusbarchfeld.spreadsheetfitnesse;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

public class RunExcelTestSuite {

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.out.println("Please call with root folder as argument");
      System.exit(1);
    }
    String folderName = args[0];// "WarmWasserNavigator";
    String[] fitnesseArgs = ArrayUtils.subarray(args, 1, args.length);
    FitnesseStarter.start(fitnesseArgs);
    FitnesseRest fitnesseRest = new FitnesseRest();
    ImportExcelIntoFitnesse importExcelIntoFitnesse = new ImportExcelIntoFitnesse(
        fitnesseRest);
    importExcelIntoFitnesse.importFolder(folderName);
    String content = fitnesseRest.runSuite(folderName);
    content = content.replace("/files/", "./files/");
    FileUtils.write(new File("testresults.html"), content, "utf-8");
    System.out.println("Results written to testresults.html.");
    //System.exit(0);
  }

}
