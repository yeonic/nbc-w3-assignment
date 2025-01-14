package level6.kiosk;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import level6.Cart.Cart;
import level6.menu.Menu;
import level6.menu.MenuCategory;
import level6.menu.MenuItem;

public class Kiosk {

  private SelectionPhase currentPhase;
  private final List<Menu> menus;
  private final Map<String, KioskMessage> messageMap = new HashMap<>();
  private final Cart myCart = new Cart();

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

    messageMap.put("CART", new KioskMessage("위 메뉴를 장바구니에 추가하시겠습니까?", "1. 확인\t\t2.취소"));
    messageMap.put("ORDER", new KioskMessage("[ ORDER MENU ]", "4. Orders\n5. Cancel"));

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
    KioskMessage currentMessage = messageMap.get("MAIN");
    MenuItem selectedItem = null;

    while (true) {
      System.out.println(currentMessage);
      try {
        switch (currentPhase) {
          case CATEGORY:
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
            int menuInput = nextInt(sc);

            // input 처리
            if (currentMessage == null || menuInput == 0) {
              currentPhase = SelectionPhase.CATEGORY;
              continue;
            }
            MenuItem item = processMenuInput(menuInput, currentMessage);
            selectedItem = item;

            // 다음 State 지정
            currentPhase = SelectionPhase.CART;
            currentMessage = messageMap.get("CART");
            break;
          case CART:
            int cartInput = nextInt(sc);
            switch (cartInput) {
              case 1 -> {
                if(selectedItem == null) {
                  throw new IllegalArgumentException("선택한 메뉴가 없습니다.");
                }
                myCart.addToCart(selectedItem);
                System.out.println(selectedItem.getName() + "이 장바구니에 추가되었습니다.");
              }
              case 2 -> currentMessage = selectMainByCart();
              default -> throw new InputMismatchException("범위 안의 수를 입력해 주세요.");
            }
            currentPhase = SelectionPhase.CATEGORY;
            currentMessage = selectMainByCart();
            break;
        }
      } catch (
          InputMismatchException e) {
        System.out.println(e.getMessage());
      }

    }
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

  private MenuItem processMenuInput(int subMenuInput, KioskMessage currentMessage)
      throws InputMismatchException {

    if (subMenuInput - 1 >= currentMessage.getEntries().size()) {
      throw new InputMismatchException("범위 안의 수를 입력해주세요.");
    }
    MenuItem item = currentMessage.getEntries().get(subMenuInput - 1);
    System.out.println("선택한 메뉴 : " + item);

    return item;
  }

  public static int nextInt(Scanner scanner) throws InputMismatchException {
    int i = scanner.nextInt();
    scanner.nextLine();
    return i;
  }
}