package level6.kiosk;

import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.naming.LimitExceededException;
import level6.cart.Cart;
import level6.constants.DiscountPolicy;
import level6.constants.SelectionPhase;
import level6.kiosk.message.ComposedKioskMessage;
import level6.kiosk.message.KioskMessage;
import level6.kiosk.message.MenuKioskMessage;
import level6.kiosk.message.StringKioskMessage;
import level6.menu.Menu;
import level6.constants.MenuCategory;
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

  /**
   * Message Map에 KioskMessage 객체를 생성하고, 초기화 한다.
   */
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

  /**
   * Kiosk Client의 EntryPoint
   */
  public void start() {
    Scanner sc = new Scanner(System.in);

    // currentMessage에 따라 메시지가 달라짐
    KioskMessage currentMessage = messageMap.get("MAIN");
    MenuItem selectedItem = null;

    while (true) {
      // 메뉴 출력
      System.out.println(currentMessage);
      try {
        // currentPhase에 따라 동작이 달라짐.
        switch (currentPhase) {
          case CATEGORY:
            // 메뉴의 카테고리를 고르는 화면
            // 사용자 입력
            int categoryInput = nextInt(sc);

            // input 처리
            // 다음 시퀀스에서 출력할 메시지 세팅됨.
            currentMessage = processCategoryInput(categoryInput);
            if (currentMessage == null) {
              return;
            }

            // 다음 State 지정
            // 다음 시퀀스의 동작을 결정함
            switch (categoryInput) {
              // 입력이 3보다 작으면 메뉴 선택 페이지로
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
            // 카테고리에 해당하는 메뉴를 고르는 화면
            // 사용자 입력
            int menuInput = nextInt(sc);

            // input 처리
            if (currentMessage == null || menuInput == 0) {
              currentPhase = SelectionPhase.CATEGORY;
              currentMessage = selectMainMessageByCart();
              continue;
            }
            // 선택된 메뉴를 출력함
            selectedItem = processMenuInput(menuInput, currentMessage);

            // 다음 State 지정
            currentMessage = messageMap.get("CART");
            currentPhase = SelectionPhase.CART;
            break;

          case CART:
            // 장바구니에 추가하는 동작을 하는 화면
            // 사용자 입력
            int cartInput = nextInt(sc);

            // input 처리 & 다음 State 지정
            currentMessage = processCartInput(cartInput, selectedItem);
            currentPhase = SelectionPhase.CATEGORY;
            break;

          case ORDER_SEQ:
            // 장바구니에 담긴 메뉴를 주문하는 시퀀스
            // 사용자 입력
            int orderInput = nextInt(sc);

            // input 처리 & 다음 State 지정
            switch (orderInput) {
              case 1:
                // 주문 완료 메시지 출력
                processOrderInput(sc);

                // 메인메뉴로 이동 준비
                currentMessage = selectMainMessageByCart();
                currentPhase = SelectionPhase.CATEGORY;
                break;
              case 2:
                // 입력 받은 키워드를 포함한 이름을 가진 메뉴를 삭제하는 동작
                System.out.println("삭제하고자 하는 메뉴 이름의 일부를 적어주세요.");

                // 사용자 입력 & input 처리
                String keyword = sc.nextLine();
                myCart.removeContainsName(keyword);

                // 다음 화면 전환 준비
                // 삭제 후 카트가 비었다면 메인메뉴로, 아닌 경우에는 주문 시퀀스로
                currentMessage = myCart.isEmpty() ? selectMainMessageByCart() : getOrderSheet();
                currentPhase =
                    myCart.isEmpty() ? SelectionPhase.CATEGORY : SelectionPhase.ORDER_SEQ;
                break;
              case 3:
                // 메인메뉴로 이동
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


  /**
   * 카트가 비었는지 여부에 따라 알맞는 Message를 리턴해주는 함수
   * @return 카트에 항목이 있다면 order 메뉴가 활성화된 메시지, 아닐 때는 기본 메시지
   */
  private KioskMessage selectMainMessageByCart() {
    return myCart.isEmpty() ? messageMap.get("MAIN") : messageMap.get("ORDER_MAIN");
  }

  /**
   * 주문 항목과 총액, 선택메뉴를 포함한 메시지를 리턴해주는 함수
   * @return 주문서 메시지
   */
  private KioskMessage getOrderSheet() {
    return new ComposedKioskMessage(List.of(
        new MenuKioskMessage("[ Orders ]", "", myCart.getCartItems()),
        new StringKioskMessage(String.format("[ Total ]\nW %.1f\n", myCart.calculateTotal()), "",
            List.of("1. 주문", "2. 메뉴 삭제", "3. 메뉴판"))
    ));
  }

  /**
   * 카테고리를 고르는 시퀀스의 사용자 입력을 처리하는 핸들러
   * @param mainMenuInput
   * @return 다음 시퀀스에서 표시할 KioskMessage
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

  /**
   * 메뉴 선택 시퀀스의 사용자 입력을 처리하는 핸들러
   * @param subMenuInput
   * @param currentMessage : 캐스팅하여 사용할 메시지
   * @throws InputMismatchException
   */
  private MenuItem processMenuInput(int subMenuInput, KioskMessage currentMessage)
      throws InputMismatchException {
    if(!(currentMessage instanceof MenuKioskMessage tempMessage)) {
      throw new InputMismatchException("메뉴를 선택하는 과정에서 문제가 생겼습니다.");
    }

    // 입력받은 수가 정상적인 범위 밖인지를 체크하는 로직
    if (subMenuInput - 1 >= tempMessage.getEntries().size()) {
      throw new InputMismatchException("범위 안의 수를 입력해주세요.");
    }

    // 유효한 범위 내라면, 선택한 메뉴를 정해진 형식에 따라 출력
    MenuItem item = tempMessage.getEntries().get(subMenuInput - 1);
    System.out.println("선택한 메뉴 : " + item);

    return item;
  }

  /**
   * 장바구니 시퀀스에서 사용자 입력을 처리하는 핸들러
   * @param cartInput
   * @param selectedItem
   * @return 다음 시퀀스에서 표시할 KioskMessage
   * @throws LimitExceededException
   */
  private KioskMessage processCartInput(int cartInput, MenuItem selectedItem) throws LimitExceededException {
    switch (cartInput) {
      case 1 -> {
        // null check logic
        if(selectedItem == null) {
          throw new IllegalArgumentException("선택한 메뉴가 없습니다.");
        }

        // 문제가 없다면 카트에 아이템을 추가한다.
        myCart.addToCart(selectedItem);
        System.out.println(selectedItem.getName() + "이 장바구니에 추가되었습니다.");
      }
      case 2 -> System.out.println("처음으로 돌아갑니다.");
      default -> throw new InputMismatchException("범위 안의 수를 입력해 주세요.");
    }
    return selectMainMessageByCart();
  }

  /**
   * 주문 시퀀스에서 할인 정책을 정하고 활용하는 핸들러
   * @param sc
   */
  private void processOrderInput(Scanner sc) {
    System.out.println(messageMap.get("DISCOUNT"));

    // 사용자 입력 & 주문 완료 메시지 포맷 설정
    int discountInput = nextInt(sc);
    String orderDoneFormat = "주문이 완료되었습니다. 금액은 W %.1f 입니다.\n";

    // enum을 활용한 할인 정책 출력
    switch (discountInput) {
      case 1 -> System.out.printf(orderDoneFormat, myCart.applyDiscount(DiscountPolicy.PATRIOTS));
      case 2 -> System.out.printf(orderDoneFormat, myCart.applyDiscount(DiscountPolicy.SOLDIER));
      case 3 -> System.out.printf(orderDoneFormat, myCart.applyDiscount(DiscountPolicy.STUDENT));
      case 4 -> System.out.printf(orderDoneFormat, myCart.applyDiscount(DiscountPolicy.NORMAL));
      default -> throw new InputMismatchException("범위를 벗어난 입력입니다.");
    }

    // 주문이 완료되면 카트를 비운다.
    myCart.clear();
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