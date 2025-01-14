package level4_5;

import java.util.ArrayList;
import java.util.List;
import level4_5.kiosk.Kiosk;
import level4_5.menu.Menu;
import level4_5.menu.MenuCategory;
import level4_5.menu.MenuItem;

public class Main {

  public static void main(String[] args) {
    List<Menu> menus = initMenu();
    Kiosk kiosk = new Kiosk(menus);
    kiosk.start();
  }

  private static List<Menu> initMenu() {
    List<Menu> menus = new ArrayList<>();

    // Setting up burgers
    menus.add(
        new Menu(MenuCategory.BURGERS, List.of(
            new MenuItem("ShackBurger", 6.9, "토마토, 양상추, 쉑소스가 토핑된 치즈버거"),
            new MenuItem("SmokeShack", 8.9, "베이컨, 체리 페퍼에 쉑소스가 토핑된 치즈버거"),
            new MenuItem("Cheeseburger", 6.9, "포테이토 번과 비프패티, 치즈가 토핑된 치즈버거"),
            new MenuItem("Hamburger", 5.4, "비프패티를 기반으로 야채가 들어간 기본버거")
        ))
    );

    // Setting up drinks
    menus.add(
        new Menu(MenuCategory.DRINKS, List.of(
            new MenuItem("Fresh Lemonade", 4.3, "매장에서 직접 만드는 상큼한 레몬에이드"),
            new MenuItem("Fifty/Fifty", 3.5, "레몬에이드와 유기농 홍차를 우려낸 아이스 티 만나 탄생한 쉐이크쉑의 시그니처 음료"),
            new MenuItem("Fountain Soda", 2.9, "코카콜라, 코카콜라 제로, 스프라이트, 환타 오렌지/그레이프/파인애플"),
            new MenuItem("Abita Root Beer", 4.8, "청량감 있는 독특한 미국식 무알콜 탄산음료"),
            new MenuItem("Hot Tea", 3.4, "보성 유기농 찻잎을 우려낸 녹차, 홍, 페퍼민트 & 레몬그라스")
        ))
    );

    // Setting up desserts
    menus.add(
        new Menu(MenuCategory.DESSERTS, List.of(
            new MenuItem("Honey Butter Crunch", 6.2, "바닐라 커스터드에 허니 버터 소스와 슈가 콘이 달콤하게 블렌딩된 콘트리트"),
            new MenuItem("Shack Attack", 6.2, "진한 초콜릿 커스터드에 퍼지 소스와 세 가지 초콜릿 토핑이 블렌딩된 쉐이크쉑의 대표 콘트리트")
        ))
    );

    return menus;
  }


}