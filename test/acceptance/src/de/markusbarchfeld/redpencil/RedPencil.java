package de.markusbarchfeld.redpencil;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Implementation of the Red Pencil Kata.
 * 
 */
public class RedPencil {

  private final class NoSpecialOfferLimits implements IReductionLimits {
    @Override
    public int getMaxReductionLimitAt(Date day) {
      return MAX_REDUCTION_LIMIT_PERCENT;
    }
  }

  static final int MAX_REDUCTION_LIMIT_PERCENT = 50;
  private Date creationDate;
  private BigDecimal price;
  private Date redPhaseStart;
  private static SpecialOffer specialOffer;
  private IReductionLimits reductionLimits;

  // special offers are an extension to the original Kata.
  public static void setSpecialOfferFromToWithMaxReductionLimit(Date from,
      Date to, int maxReductionLimit) {
    specialOffer = new SpecialOffer(from, to, maxReductionLimit);
  }

  public RedPencil(Date creationDate, BigDecimal price) {
    this.creationDate = creationDate;
    this.price = price;
    redPhaseStart = null;
    reductionLimits = specialOffer == null ? new NoSpecialOfferLimits()
        : specialOffer;
  }

  public void setPriceAtTo(Date changeDate, BigDecimal updatedPrice) {

    if (isAfterStablePhase(changeDate)
        && isInBetweenAllowedReduction(updatedPrice, changeDate)) {
      redPhaseStart = changeDate;
    }

  }

  private boolean isInBetweenAllowedReduction(BigDecimal updatedPrice,
      Date changeDate) {
    BigDecimal deltaPrice = price.subtract(updatedPrice);
    Double deltaPercent = deltaPrice.doubleValue() / price.doubleValue();
    return deltaPercent >= 0.0499999f
        && deltaPercent <= reductionLimits.getMaxReductionLimitAt(changeDate) / 100d;
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
