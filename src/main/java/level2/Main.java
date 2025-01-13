package level2;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import level2.menu.MenuItem;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String format = "%-15s | W %.1f | %s\n";

    // List 객체 초기화
    List<MenuItem> menuItems = new ArrayList<>();
    menuItems.add(new MenuItem("ShackBurger", 6.9, "토마토, 양상추, 쉑소스가 토핑된 치즈버거"));
    menuItems.add(new MenuItem("SmokeShack", 8.9, "베이컨, 체리 페퍼에 쉑소스가 토핑된 치즈버거"));
    menuItems.add(new MenuItem("Cheeseburger", 6.9, "포테이토 번과 비프패티, 치즈가 토핑된 치즈버거"));
    menuItems.add(new MenuItem("Hamburger", 5.4, "비프패티를 기반으로 야채가 들어간 기본버거"));

    while (true) {
      System.out.println("[ SHAKESHACK MENU ]");
      for (int i = 0; i < menuItems.size(); i++) {
        MenuItem tempItem = menuItems.get(i);
        System.out.printf((i+1) + ". " + format, tempItem.getName(), tempItem.getPrice(), tempItem.getDescription());
      }
      System.out.println("0. 종료");

      try {
        // input 받기
        int cmd = nextInt(sc);

        // cmd가 0이면 종료, 이외의 int면 반복하도록
        if(cmd == 0) {
          System.out.println("프로그램을 종료합니다.");
          break;
        } else if (cmd - 1 < menuItems.size()) {
          MenuItem selectedItem = menuItems.get(cmd - 1);
          System.out.printf("선택한 메뉴 : " + format, selectedItem.getName(), selectedItem.getPrice(), selectedItem.getDescription());
        } else {
          System.out.println("존재하지 않는 메뉴입니다. 다시 선택해주세요.");
        }
      } catch (InputMismatchException e) {
        System.out.println("숫자를 입력하세요.");
        throw new RuntimeException(e);
      }
    }
  }

  public static int nextInt(Scanner scanner) throws InputMismatchException {
    int i = scanner.nextInt();
    scanner.nextLine();
    return i;
  }
}