package org.example.memstorge;

/**
 * @author : kairbon
 * @date : 2021/3/18
 */
public class Sequence {
  private final long        startValue;                           // inclusive
  private final long        endValue;                             // exclusive

  public Sequence(long startValue, long endValue) {
    this.startValue = startValue;
    this.endValue = endValue;
  }

  /**
   * 可用的最小值, 包含 [startValue, endValue)
   */
  public long getStartValue() {
    return startValue;
  }

  /**
   * 可用的最大值, 不包含 [startValue, endValue)
   */
  public long getEndValue() {
    return endValue;
  }

  @Override
  public String toString() {
    return "Sequence{" + "startValue=" + startValue + ", endValue=" + endValue + '}';
  }
}
