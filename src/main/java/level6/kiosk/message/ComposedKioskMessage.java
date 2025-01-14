package level6.kiosk.message;

import java.util.ArrayList;
import java.util.List;

public class ComposedKioskMessage implements KioskMessage{

  private final List<KioskMessage> elements = new ArrayList<>();

  public ComposedKioskMessage(List<KioskMessage> elements) {
    this.elements.addAll(elements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (KioskMessage element : elements) {
      sb.append(element);
      sb.append("\n");
    }
    return sb.toString();
  }
}
