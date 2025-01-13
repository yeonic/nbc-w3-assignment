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
            // 메뉴 출력 & 사용자 입력
            System.out.println(messageMap.get("MAIN"));
            int categoryInput = nextInt(sc);

            // input 처리
            currentMessage = processCategoryInput(categoryInput);
            if (currentMessage == null) {
              return;
            }

            // 다음 State 지정
            currentPhase = SelectionPhase.MENU;
            break;
          case MENU:
            // 메뉴 출력 & 사용자 입력
            System.out.println(currentMessage);
            int menuInput = nextInt(sc);

            // input 처리
            if (currentMessage == null || menuInput == 0) {
              currentPhase = SelectionPhase.CATEGORY;
              continue;
            }
            processMenuInput(menuInput, currentMessage);

            // 다음 State 지정
            currentPhase = SelectionPhase.CATEGORY;
            break;
        }
      } catch (
          InputMismatchException e) {
        System.out.println(e.getMessage());
      }

    }
  }

  private void processMenuInput(int subMenuInput, KioskMessage currentMessage)
      throws InputMismatchException {

    if (subMenuInput - 1 >= currentMessage.getEntries().size()) {
      throw new InputMismatchException("범위 안의 수를 입력해주세요.");
    }
    System.out.println("선택한 메뉴 : " + currentMessage.getEntries().get(subMenuInput - 1));
  }

  private KioskMessage processCategoryInput(int mainMenuInput) throws InputMismatchException {

    KioskMessage currentMessage;

    switch (mainMenuInput) {
      case 0 -> {
        System.out.println("프로그램을 종료합니다.");
        return null;
      }
      case 1 -> currentMessage = messageMap.get("BURGERS");
      case 2 -> currentMessage = messageMap.get("DRINKS");
      case 3 -> currentMessage = messageMap.get("DESSERTS");
      default -> throw new InputMismatchException("잘못된 입력입니다. 다시 입력해주세요.");
    }
    return currentMessage;
  }

  public static int nextInt(Scanner scanner) throws InputMismatchException {
    int i = scanner.nextInt();
    scanner.nextLine();
    return i;
  }
}