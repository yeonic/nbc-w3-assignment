package level3.kiosk;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import level3.menu.MenuItem;

public class Kiosk {

  private final List<MenuItem> menuItems;

  public Kiosk(List<MenuItem> menuItems) {
    this.menuItems = menuItems;
  }

  public void start() {
    Scanner sc = new Scanner(System.in);
    String format = "%-13s | W %.1f | %s\n";

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

        if(cmd == 0) {
          // cmd가 0이면 종료
          System.out.println("프로그램을 종료합니다.");
          break;
        } else if (cmd - 1 < menuItems.size()) {
          // List<MenuItem>의 범위 안에 존재하면 선택한 메뉴 출력
          MenuItem selectedItem = menuItems.get(cmd - 1);
          System.out.printf("선택한 메뉴 : " + format, selectedItem.getName(), selectedItem.getPrice(), selectedItem.getDescription());
        } else {
          // 범위를 벗어나면 다시 입력 받음
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
