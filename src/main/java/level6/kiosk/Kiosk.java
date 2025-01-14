package level6.kiosk;

import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.naming.LimitExceededException;
import level6.Cart.Cart;
import level6.kiosk.message.ComposedKioskMessage;
import level6.kiosk.message.KioskMessage;
import level6.kiosk.message.MenuKioskMessage;
import level6.kiosk.message.StringKioskMessage;
import level6.menu.Menu;
import level6.menu.MenuCategory;
import level6.menu.MenuItem;

public class Kiosk {

  private SelectionPhase currentPhase;
  private final List<Menu> menus;
  private final Cart myCart = new Cart();
  private final Map<String, KioskMessage> messageMap = new HashMap<>();

  public Kiosk(List<Menu> menus) {
    this.currentPhase = SelectionPhase.CATEGORY;
    this.menus = menus;
    initMessageMap();
  }

  private void initMessageMap() {
    // 메시지 조회 맵 초기화(String)
    messageMap.put("MAIN",
        new StringKioskMessage("[ MAIN MENU ]", "0. 종료", List.of("1. Burgers", "2. Drinks", "3. Desserts")));
    messageMap.put("CART",
        new StringKioskMessage("위 메뉴를 장바구니에 추가하시겠습니까?", "2. 취소", List.of("1. 확인")));
    messageMap.put("DISCOUNT",
        new StringKioskMessage("할인 정보를 입력하세요.", "",
            Arrays.stream(DiscountPolicy.values()).map(DiscountPolicy::toString).toList()));

    // 메시지 조회 맵 초기화(Menu)
    messageMap.put("BURGERS", new MenuKioskMessage("[ BURGERS MENU ]", "0. 뒤로가기",
        menus.stream().filter(m -> m.isCategoryEqualTo(MenuCategory.BURGERS))
                      .flatMap(menu -> menu.getMenuItems().stream()).toList()));

    messageMap.put("DRINKS", new MenuKioskMessage("[ DRINKS MENU ]", "0. 뒤로가기",
        menus.stream().filter(m -> m.isCategoryEqualTo(MenuCategory.DRINKS))
                      .flatMap(menu -> menu.getMenuItems().stream()).toList()));

    messageMap.put("DESSERTS", new MenuKioskMessage("[ DESSERTS MENU ]", "0. 뒤로가기",
        menus.stream().filter(m -> m.isCategoryEqualTo(MenuCategory.DESSERTS))
                      .flatMap(menu -> menu.getMenuItems().stream()).toList()));

    // 메시지 조회 맵 초기화(Composed)
    messageMap.put("ORDER_MAIN",
        new ComposedKioskMessage(List.of(
            messageMap.get("MAIN"),
            new StringKioskMessage("[ ORDER MENU ]", "5. Cancel", List.of("4. Orders")))
        ));
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
            // input 처리
            int categoryInput = nextInt(sc);

            // 다음 State 지정
            currentMessage = processCategoryInput(categoryInput);
            if (currentMessage == null) {
              return;
            }

            // 입력이 3보다 작으면 메뉴 선택 페이지로
            switch (categoryInput) {
              case 1:
              case 2:
              case 3:
                currentPhase = SelectionPhase.MENU;
                break;
              case 4:
                currentPhase = SelectionPhase.ORDER_SEQ;
                break;
              case 5:
                currentPhase = SelectionPhase.CATEGORY;
                break;
              default:
                throw new InputMismatchException("잘못된 입력입니다.");
            }
            break;

          case MENU:
            // input 처리
            int menuInput = nextInt(sc);
            if (currentMessage == null || menuInput == 0) {
              currentPhase = SelectionPhase.CATEGORY;
              currentMessage = selectMainMessageByCart();
              continue;
            }
            selectedItem = processMenuInput(menuInput, currentMessage);

            // 다음 State 지정
            currentMessage = messageMap.get("CART");
            currentPhase = SelectionPhase.CART;
            break;

          case CART:
            // input 처리
            int cartInput = nextInt(sc);

            // 다음 State 지정
            currentMessage = processCartInput(cartInput, selectedItem);
            currentPhase = SelectionPhase.CATEGORY;
            break;

          case ORDER_SEQ:
            // input 처리
            int orderInput = nextInt(sc);

            // 다음 State 지정
            switch (orderInput) {
              case 1:
                processOrderInput(sc);
                currentMessage = selectMainMessageByCart();
                currentPhase = SelectionPhase.CATEGORY;
                break;
              case 2:
                System.out.println("삭제하고자 하는 메뉴 이름의 일부를 적어주세요.");
                String keyword = sc.nextLine();
                myCart.removeContainsName(keyword);

                currentMessage = myCart.isEmpty() ? selectMainMessageByCart() : getOrderSheet();
                currentPhase =
                    myCart.isEmpty() ? SelectionPhase.CATEGORY : SelectionPhase.ORDER_SEQ;
                break;
              case 3:
                currentMessage = selectMainMessageByCart();
                currentPhase = SelectionPhase.CATEGORY;
                break;
              default:
                throw new IllegalArgumentException("잘못된 입력입니다. 다시 입력해주세요.");
            }
            break;
        }
      } catch (InputMismatchException e) {
        String errorMessage = e.getMessage();
        if(errorMessage != null) {
          System.out.println(errorMessage);
        }
        sc.nextLine();
      } catch (LimitExceededException e) {
        System.out.println("LimitExceed: " + e.getMessage());
        currentMessage = selectMainMessageByCart();
        currentPhase = SelectionPhase.CATEGORY;
      }

    }
  }

  private void processOrderInput(Scanner sc) {
    System.out.println(messageMap.get("DISCOUNT"));
    int discountInput = nextInt(sc);
    String orderDoneFormat = "주문이 완료되었습니다. 금액은 W %.1f 입니다.\n";

    switch (discountInput) {
      case 1 -> System.out.printf(orderDoneFormat, myCart.applyDiscount(DiscountPolicy.PATRIOTS));
      case 2 -> System.out.printf(orderDoneFormat, myCart.applyDiscount(DiscountPolicy.SOLDIER));
      case 3 -> System.out.printf(orderDoneFormat, myCart.applyDiscount(DiscountPolicy.STUDENT));
      case 4 -> System.out.printf(orderDoneFormat, myCart.applyDiscount(DiscountPolicy.NORMAL));
      default -> throw new InputMismatchException("범위를 벗어난 입력입니다.");
    }
    myCart.clear();
  }


  private KioskMessage selectMainMessageByCart() {
    return myCart.isEmpty() ? messageMap.get("MAIN") : messageMap.get("ORDER_MAIN");
  }

  private KioskMessage getOrderSheet() {
    return new ComposedKioskMessage(List.of(
        new MenuKioskMessage("[ Orders ]", "", myCart.getCartItems()),
        new StringKioskMessage(String.format("[ Total ]\nW %.1f\n", myCart.calculateTotal()), "",
            List.of("1. 주문", "2. 메뉴 삭제", "3. 메뉴판"))
    ));
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
      case 4 -> {
        if (myCart.isEmpty()) {
          throw new InputMismatchException("잘못된 입력입니다. 다시 입력해주세요.");
        }
        currentMessage = getOrderSheet();
      }
      case 5 -> {
        myCart.clear();
        currentMessage = messageMap.get("MAIN");
      }
      default -> throw new InputMismatchException("잘못된 입력입니다. 다시 입력해주세요.");
    }
    return currentMessage;
  }

  private MenuItem processMenuInput(int subMenuInput, KioskMessage currentMessage)
      throws InputMismatchException {
    if(!(currentMessage instanceof MenuKioskMessage tempMessage)) {
      throw new InputMismatchException("메뉴를 선택하는 과정에서 문제가 생겼습니다.");
    }

    if (subMenuInput - 1 >= tempMessage.getEntries().size()) {
      throw new InputMismatchException("범위 안의 수를 입력해주세요.");
    }
    MenuItem item = tempMessage.getEntries().get(subMenuInput - 1);
    System.out.println("선택한 메뉴 : " + item);

    return item;
  }

  private KioskMessage processCartInput(int cartInput, MenuItem selectedItem) throws LimitExceededException {
    switch (cartInput) {
      case 1 -> {
        if(selectedItem == null) {
          throw new IllegalArgumentException("선택한 메뉴가 없습니다.");
        }
        myCart.addToCart(selectedItem);
        System.out.println(selectedItem.getName() + "이 장바구니에 추가되었습니다.");
      }
      case 2 -> System.out.println("처음으로 돌아갑니다.");
      default -> throw new InputMismatchException("범위 안의 수를 입력해 주세요.");
    }
    return selectMainMessageByCart();
  }

  public static int nextInt(Scanner scanner) throws InputMismatchException {
    int i = scanner.nextInt();
    scanner.nextLine();
    return i;
  }
}