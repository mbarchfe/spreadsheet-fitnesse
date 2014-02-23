package de.markusbarchfeld.redpencil;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Implementation of the Red Pencil Kata.
 * 
 */
public class RedPencil {

  private Date creationDate;
  private BigDecimal price;
  private Date redPhaseStart;

  public RedPencil(Date creationDate, BigDecimal price) {
    this.creationDate = creationDate;
    this.price = price;
    redPhaseStart = null;
  }

  public void setPriceAtTo(Date changeDate, BigDecimal updatedPrice) {

    if (isAfterStablePhase(changeDate)
        && isInBetweenAllowedReduction(updatedPrice)) {
      redPhaseStart = changeDate;
    }

  }

  private boolean isInBetweenAllowedReduction(BigDecimal updatedPrice) {
    BigDecimal deltaPrice = price.subtract(updatedPrice);
    Double deltaPercent = deltaPrice.doubleValue() / price.doubleValue();
    return deltaPercent >= 0.0499999f && deltaPercent <= 0.5f;
  }

  private boolean isAfterStablePhase(Date changeDate) {
    int deltaDays = calculcateDelta(creationDate, changeDate);
    return deltaDays > 30;
  }

  private int calculcateDelta(Date earlier, Date later) {
    Calendar earlierCalendar = Calendar.getInstance();
    Calendar laterCalendar = Calendar.getInstance();
    earlierCalendar.setTime(earlier);
    laterCalendar.setTime(later);
    return laterCalendar.get(Calendar.DAY_OF_YEAR)
        - earlierCalendar.get(Calendar.DAY_OF_YEAR);
  }

  // returns the date of the first red pencil period or null
  public Date getRedPencilStart() {
    return redPhaseStart;
  }

}
