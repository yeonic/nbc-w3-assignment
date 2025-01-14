package level6.Cart;

import java.util.ArrayList;
import java.util.List;
import level6.menu.MenuItem;

public class Cart {

  List<MenuItem> cart = new ArrayList<>();

  public void clear() {
    cart.clear();
  }

  public boolean isEmpty() {
    return cart.isEmpty();
  }

  public void addToCart(MenuItem item) {
    cart.add(item);
  }

  public double calculateTotal() {
    double total = 0.0;
    for (MenuItem item : cart) {
      total += item.getPrice();
    }
    return Math.round(total * 100.0) / 100.0;
  }

  public double applyDiscount(double percentage) {
    return calculateTotal() * (1.0 - percentage);
  }
}
