package level6.constants;

public enum DiscountPolicy {
  PATRIOTS("국가유공자",0.1),
  SOLDIER("군인",0.05),
  STUDENT("학생",0.03),
  NORMAL("일반",0.00);

  private final String name;
  private final double rate;

  DiscountPolicy(String name, double rate) {
    this.name = name;
    this.rate = rate;
  }

  public double getRate() {
    return rate;
  }

  @Override
  public String toString() {
    return String.format("%d. %s : %d%%", ordinal()+1, name, (int)(rate * 100));
  }
}
