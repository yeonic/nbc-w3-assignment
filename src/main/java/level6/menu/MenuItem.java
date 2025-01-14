package level6.menu;

public class MenuItem {

  private String name;
  private double price;
  private String description;

  public MenuItem(String name, double price, String description) {
    this.name = name;
    this.price = price;
    this.description = description;
  }

  @Override
  public String toString() {
    return String.format("%-16s | W %.1f | %s", name, price, description);
  }

  public String getName() {
    return name;
  }

  public double getPrice() {
    return price;
  }
}
