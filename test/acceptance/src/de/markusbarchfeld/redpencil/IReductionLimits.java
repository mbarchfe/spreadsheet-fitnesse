package de.markusbarchfeld.redpencil;

import java.util.Date;

public interface IReductionLimits {
  int getMaxReductionLimitAt(Date day);
}