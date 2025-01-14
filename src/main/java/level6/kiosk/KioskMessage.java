package level6.kiosk;

import java.util.ArrayList;
import java.util.List;
import level6.menu.MenuItem;

public class KioskMessage {

  private final String title;
  private final String content;
  private final List<MenuItem> entries = new ArrayList<>();

  public KioskMessage(String title, String content) {
    this.title = title;
    this.content = content;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("\n%s\n", title));
    for (int i = 0; i < entries.size(); i++) {
      sb.append(String.format("%d. %s\n", i + 1, entries.get(i)));
    }
    sb.append(String.format("%s", content));

    return sb.toString();
  }

  public void setEntries(List<MenuItem> newEntries) {
    entries.addAll(newEntries);
  }

  public List<MenuItem> getEntries() {
    return entries;
  }
}
