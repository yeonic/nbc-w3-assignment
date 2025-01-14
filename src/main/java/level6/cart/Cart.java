package level6.cart;

import java.util.ArrayList;
import java.util.List;
import javax.naming.LimitExceededException;
import level6.constants.DiscountPolicy;
import level6.menu.MenuItem;

public class Cart {

  private final List<MenuItem> cart = new ArrayList<>();

  public void clear() {
    cart.clear();
  }

  public boolean isEmpty() {
    return cart.isEmpty();
  }

  public void addToCart(MenuItem item) throws LimitExceededException {
    if(cart.contains(item)){
      throw new LimitExceededException("장바구니에는 같은 메뉴는 하나만 담을 수 있습니다.");
    }
    cart.add(item);
  }

  public void removeContainsName(String menuName) {
    List<MenuItem> filtered = cart.stream()
        .filter(item -> !item.getName().toLowerCase().contains(menuName.toLowerCase()))
        .toList();



    cart.clear();
    cart.addAll(filtered);
  }

  public List<MenuItem> getCartItems() {
    return cart;
  }

  public double calculateTotal() {
    double total = 0.0;
    for (MenuItem item : cart) {
      total += item.getPrice();
    }
    return Math.round(total * 100.0) / 100.0;
  }

  public double applyDiscount(DiscountPolicy policy) {
    return calculateTotal() * (1.0 - policy.getRate());
  }
}
