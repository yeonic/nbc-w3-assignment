package level4.kiosk;

import java.util.ArrayList;
import java.util.List;
import level4.menu.MenuItem;

public class KioskMessage {

  private final String header;
  private final String footer;
  private final List<MenuItem> entries = new ArrayList<>();

  public KioskMessage(String header, String footer) {
    this.header = header;
    this.footer = footer;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("\n%s\n", header));
    for (int i = 0; i < entries.size(); i++) {
      sb.append(String.format("%d. %s\n", i + 1, entries.get(i)));
    }
    sb.append(String.format("%s", footer));

    return sb.toString();
  }

  public void setEntries(List<MenuItem> newEntries) {
    entries.addAll(newEntries);
  }

  public List<MenuItem> getEntries() {
    return entries;
  }
}
