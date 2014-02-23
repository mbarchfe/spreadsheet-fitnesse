package de.markusbarchfeld.spreadsheetfitnesse;

public class InvalidFileNameException extends Exception {

  private static final long serialVersionUID = 4759066656823120325L;

  public InvalidFileNameException() {
  }

  public InvalidFileNameException(String arg0) {
    super(arg0);
  }

  public InvalidFileNameException(Throwable arg0) {
    super(arg0);
  }

  public InvalidFileNameException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

}
