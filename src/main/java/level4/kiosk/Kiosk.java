package level4.kiosk;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import level4.menu.Menu;
import level4.menu.MenuCategory;

public class Kiosk {

  private SelectionPhase currentPhase;
  private Map<String, KioskMessage> messageMap = new HashMap<>();
  private List<Menu> menus;

  public Kiosk(List<Menu> menus) {
    this.currentPhase = SelectionPhase.CATEGORY;
    this.menus = menus;

    initMessageMap();
    initMessageEntries();
  }

  private void initMessageMap() {
    messageMap.put("MAIN",
        new KioskMessage("[ MAIN MENU ]", "1. Burgers\n2. Drinks\n3. Desserts\n0. 종료"));
    messageMap.put("BURGERS", new KioskMessage("[ BURGERS MENU ]", "0. 뒤로가기"));
    messageMap.put("DRINKS", new KioskMessage("[ DRINKS MENU ]", "0. 뒤로가기"));
    messageMap.put("DESSERTS", new KioskMessage("[ DESSERTS MENU ]", "0. 뒤로가기"));
  }

  private void initMessageEntries() {
    messageMap.get("BURGERS")
        .setEntries(menus.stream()
            .filter(m -> m.isCategoryEqualTo(MenuCategory.BURGERS))
            .flatMap(menu -> menu.getMenuItems().stream()).toList());

    messageMap.get("DRINKS")
        .setEntries(menus.stream()
            .filter(m -> m.isCategoryEqualTo(MenuCategory.DRINKS))
            .flatMap(menu -> menu.getMenuItems().stream()).toList());

    messageMap.get("DESSERTS")
        .setEntries(menus.stream()
            .filter(m -> m.isCategoryEqualTo(MenuCategory.DESSERTS))
            .flatMap(menu -> menu.getMenuItems().stream()).toList());
  }


  public void start() {
    Scanner sc = new Scanner(System.in);
    KioskMessage currentMessage = null;

    while (true) {
      try {
        switch (currentPhase) {
          case CATEGORY:
            System.out.println(messageMap.get("MAIN"));
            int mainMenuInput = nextInt(sc);
            switch (mainMenuInput) {
              case 0 -> {
                System.out.println("프로그램을 종료합니다.");
                return;
              }
              case 1 -> currentMessage = messageMap.get("BURGERS");
              case 2 -> currentMessage = messageMap.get("DRINKS");
              case 3 -> currentMessage = messageMap.get("DESSERTS");
              default -> throw new InputMismatchException("잘못된 입력입니다. 다시 입력해주세요.");
            }
            currentPhase = SelectionPhase.MENU;
            break;
          case MENU:
            System.out.println(currentMessage);
            int subMenuInput = nextInt(sc);
            if (subMenuInput == 0) {
              currentPhase = SelectionPhase.CATEGORY;
              continue;
            }
            if (subMenuInput - 1 >= currentMessage.getEntries().size()) {
              throw new InputMismatchException("범위 안의 수를 입력해주세요.");
            }
            System.out.println("선택한 메뉴 : " + currentMessage.getEntries().get(subMenuInput - 1));
            currentPhase = SelectionPhase.CATEGORY;
        }
      } catch (
          InputMismatchException e) {
        System.out.println(e.getMessage());
      }

    }
  }

  public static int nextInt(Scanner scanner) throws InputMismatchException {
    int i = scanner.nextInt();
    scanner.nextLine();
    return i;
  }
}

//try {
//// input 받기
//int cmd = nextInt(sc);
//
//        if (cmd == 0) {
//    // cmd가 0이면 종료
//    System.out.println("프로그램을 종료합니다.");
//          break;
//              } else if (cmd - 1 < menuItems.size()) {
//// List<MenuItem>의 범위 안에 존재하면 선택한 메뉴 출력
//MenuItem selectedItem = menuItems.get(cmd - 1);
//          System.out.printf("선택한 메뉴 : " + format, selectedItem.getName(), selectedItem.getPrice(),
//              selectedItem.getDescription());
//    } else {
//    // 범위를 벗어나면 다시 입력 받음
//    System.out.println("존재하지 않는 메뉴입니다. 다시 선택해주세요.");
//        }
//            } catch (InputMismatchException e) {
//    System.out.println("숫자를 입력하세요.");
//        throw new RuntimeException(e);
//}
