package level6.menu;

import java.util.List;
import level6.constants.MenuCategory;

public class Menu {

  private MenuCategory category;
  private List<MenuItem> items;

  public Menu(MenuCategory category, List<MenuItem> items) {
    this.category = category;
    this.items = items;
  }

  public List<MenuItem> getMenuItems() {
    return items;
  }

  public boolean isCategoryEqualTo(MenuCategory category) {
    return this.category == category;
  }
}

// menuItem
// menu
// kiosk