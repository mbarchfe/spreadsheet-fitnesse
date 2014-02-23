package de.markusbarchfeld.redpencil.fixture;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.markusbarchfeld.redpencil.RedPencil;

/**
 * The fixture referenced from the Red Pencil FIT tests.
 * 
 * @author mbarchfe
 * 
 */
public class RedPencilFixture {

  private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
      "dd.MM.yy");
  private RedPencil redPencil;

  public void createProductAtWithPrice(String date, String price)
      throws ParseException {
    redPencil = new RedPencil(convertToDate(date), convertToPrice(price));
  }

  private BigDecimal convertToPrice(String price) {
    return new BigDecimal(price);
  }

  private Date convertToDate(String date) throws ParseException {
    return SIMPLE_DATE_FORMAT.parse((String) date);
  }

  public void setPriceAtTo(String date, String price) throws ParseException {
    redPencil.setPriceAtTo(convertToDate(date), convertToPrice(price));
  }

  public String redPencilStarts() {
    Date redPencilStart = redPencil.getRedPencilStart();
    return redPencilStart != null ? SIMPLE_DATE_FORMAT.format(redPencilStart)
        : "None";
  }

}
