package level6.Cart;

import java.util.ArrayList;
import java.util.List;
import level2.menu.MenuItem;

public class Cart {

  List<MenuItem> cart = new ArrayList<>();

  public void clear() {
    cart.clear();
  }

  public double calculateTotal() {
    double total = 0.0;
    for (MenuItem item : cart) {
      total += item.getPrice();
    }
    return total
  }
}
