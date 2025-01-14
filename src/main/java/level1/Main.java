package level1;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String format = "%d. %-15s | W %.1f | %s\n";

    while (true) {
      System.out.println("[ SHAKESHACK MENU ]");

      // 쉽게 loop, object 사용 코드로 교체 가능하도록 string format 정의 활용.
      System.out.printf(format, 1, "ShackBurger", 6.9, "토마토, 양상추, 쉑소스가 토핑된 치즈버거");
      System.out.printf(format, 2, "SmokeShack", 8.9, "베이컨, 체리 페퍼에 쉑소스가 토핑된 치즈버거");
      System.out.printf(format, 3, "Cheeseburger", 6.9, "포테이토 번과 비프패티, 치즈가 토핑된 치즈버거");
      System.out.printf(format, 4, "Hamburger", 5.4, "비프패티를 기반으로 야채가 들어간 기본버거");
      System.out.println("0. 종료");

      try {
        // input 받기
        int cmd = nextInt(sc);

        // cmd가 0이면 종료, 이외의 int면 반복하도록
        if(cmd == 0) {
          System.out.println("프로그램을 종료합니다.");
          break;
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
