package level4_5.kiosk;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import level4_5.menu.Menu;
import level4_5.menu.MenuCategory;
import level4_5.menu.MenuItem;

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

  /**
   * Message Map에 KioskMessage 객체를 생성하고, 공통 요소를 초기화 한다.
   */
  private void initMessageMap() {
    messageMap.put("MAIN",
        new KioskMessage("[ MAIN MENU ]", "1. Burgers\n2. Drinks\n3. Desserts\n0. 종료"));
    messageMap.put("BURGERS", new KioskMessage("[ BURGERS MENU ]", "0. 뒤로가기"));
    messageMap.put("DRINKS", new KioskMessage("[ DRINKS MENU ]", "0. 뒤로가기"));
    messageMap.put("DESSERTS", new KioskMessage("[ DESSERTS MENU ]", "0. 뒤로가기"));
  }

  /**
   * Map에 존재하는 KioskMessage에 항목들을 추가한다.
   */
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

  /**
   * Kiosk Client의 EntryPoint
   */
  public void start() {
    Scanner sc = new Scanner(System.in);
    // currentMessage에 따라 메시지가 달라짐
    KioskMessage currentMessage = null;

    while (true) {
      try {
        // currentPhase에 따라 동작이 달라짐.
        switch (currentPhase) {
          case CATEGORY:
            // 메뉴의 카테고리를 고르는 화면
            // 메뉴 출력 & 사용자 입력
            System.out.println(messageMap.get("MAIN"));
            int categoryInput = nextInt(sc);

            // input 처리
            // 다음 시퀀스에서 출력할 메시지 세팅됨.
            currentMessage = processCategoryInput(categoryInput);
            if (currentMessage == null) {
              return;
            }

            // 다음 State 지정
            // 다음 시퀀스의 동작을 결정함
            currentPhase = SelectionPhase.MENU;
            break;

          case MENU:
            // 카테고리에 해당하는 메뉴를 고르는 화면
            // 메뉴 출력 & 사용자 입력
            System.out.println(currentMessage);
            int menuInput = nextInt(sc);

            // input 처리
            if (currentMessage == null || menuInput == 0) {
              currentPhase = SelectionPhase.CATEGORY;
              continue;
            }
            // 선택된 메뉴를 출력함
            processMenuInput(menuInput, currentMessage.getEntries());

            // 다음 State 지정
            currentPhase = SelectionPhase.CATEGORY;
            break;
        }
      } catch (
          InputMismatchException e) {
        String errorMessage = e.getMessage();
        if(errorMessage != null) {
          System.out.println(errorMessage);
        }
        sc.nextLine();
      }

    }
  }

  /**
   * 카테고리를 고르는 시퀀스의 사용자 입력을 처리하는 핸들러
   * @param mainMenuInput
   * @return 다음 시퀀스에서 표시할 KioskMessage
   * @throws InputMismatchException
   */
  private KioskMessage processCategoryInput(int mainMenuInput) throws InputMismatchException {

    KioskMessage currentMessage;

    // 입력에 따라 다음 시퀀스에서 표시될 KioskMessage를 결정하여 리턴한다.
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

  /**
   * 메뉴 선택 시퀀스의 사용자 입력을 처리하는 핸들러
   * @param subMenuInput
   * @param allMenu : 현재 시퀀스에 존재하는 메뉴 리스트
   * @throws InputMismatchException
   */
  private void processMenuInput(int subMenuInput, List<MenuItem> allMenu)
      throws InputMismatchException {

    // 입력받은 수가 정상적인 범위 밖인지를 체크하는 로직
    if (subMenuInput - 1 >= allMenu.size()) {
      throw new InputMismatchException("범위 안의 수를 입력해주세요.");
    }

    // 유효한 범위 내라면, 선택한 메뉴를 정해진 형식에 따라 출력
    System.out.println("선택한 메뉴 : " + allMenu.get(subMenuInput - 1));
  }

  /**
   * nextInt 이후에 남는 개행문자를 자동으로 처리해주는 메서드
   * @param scanner
   * @return scanner에서 입력받은 integer
   * @throws InputMismatchException
   */
  public static int nextInt(Scanner scanner) throws InputMismatchException {
    int i = scanner.nextInt();
    scanner.nextLine();
    return i;
  }
}