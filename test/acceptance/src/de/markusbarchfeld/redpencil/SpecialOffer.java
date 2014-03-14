package de.markusbarchfeld.redpencil;

import java.util.Date;

public class SpecialOffer implements IReductionLimits {

  private Date from;
  private Date to;
  private int maxReductionLimit;

  public SpecialOffer(Date from, Date to, int maxReductionLimit) {
    this.from = from;
    this.to = to;
    this.maxReductionLimit = maxReductionLimit;
  }

  public int getMaxReductionLimitAt(Date day) {
    int result = RedPencil.MAX_REDUCTION_LIMIT_PERCENT;
    System.out.println(maxReductionLimit);
    if (day.before(to) && day.after(from)) {
      result = this.maxReductionLimit;
      System.out.println("S");
    }
    return result;
  }

}
