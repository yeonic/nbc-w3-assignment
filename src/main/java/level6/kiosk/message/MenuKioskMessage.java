package level6.kiosk.message;

import java.util.ArrayList;
import java.util.List;
import level6.menu.MenuItem;

public class MenuKioskMessage implements KioskMessage{

  private final String title;
  private final String numberZeroChoice;
  private final List<MenuItem> entries = new ArrayList<>();

  public MenuKioskMessage(String title, String numberZeroChoice, List<MenuItem> newEntries) {
    this.title = title;
    this.numberZeroChoice = numberZeroChoice;
    entries.addAll(newEntries);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("\n%s", title));
    for (int i = 0; i < entries.size(); i++) {
      sb.append(String.format("\n%d. %s", i + 1, entries.get(i)));
    }
    if (!numberZeroChoice.isEmpty()){
      sb.append(String.format("\n%s", numberZeroChoice));
    }

    return sb.toString();
  }

  public List<MenuItem> getEntries() {
    return entries;
  }
}
