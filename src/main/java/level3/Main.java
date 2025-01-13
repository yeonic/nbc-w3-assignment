package level3;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import level3.kiosk.Kiosk;
import level3.menu.MenuItem;

public class Main {

  public static void main(String[] args) {
    // List 객체 초기화
    List<MenuItem> menuItems = new ArrayList<>();
    // List 객체 초기화
    menuItems.add(new MenuItem("ShackBurger", 6.9, "토마토, 양상추, 쉑소스가 토핑된 치즈버거"));
    menuItems.add(new MenuItem("SmokeShack", 8.9, "베이컨, 체리 페퍼에 쉑소스가 토핑된 치즈버거"));
    menuItems.add(new MenuItem("Cheeseburger", 6.9, "포테이토 번과 비프패티, 치즈가 토핑된 치즈버거"));
    menuItems.add(new MenuItem("Hamburger", 5.4, "비프패티를 기반으로 야채가 들어간 기본버거"));

    Kiosk kiosk = new Kiosk(menuItems);
    kiosk.start();
  }


}