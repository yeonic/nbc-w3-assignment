package level6.kiosk.message;

import java.util.ArrayList;
import java.util.List;

public class StringKioskMessage implements KioskMessage{

  private String title;
  private String numberZeroChoice;
  private final List<String> entries = new ArrayList<>();

  public StringKioskMessage(String title, String numberZeroChoice, List<String> entries) {
    this.title = title;
    this.numberZeroChoice = numberZeroChoice;
    this.entries.addAll(entries);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("\n%s\n", title));
    if (entries.size() == 1) {
      sb.append(entries.get(0));
      sb.append("\t\t");
      sb.append(numberZeroChoice);
      return sb.toString();
    }

    for (String entry : entries) {
      sb.append(String.format("%s\n", entry));
    }
    sb.append(String.format("%s", numberZeroChoice));
    return sb.toString();
  }
}
