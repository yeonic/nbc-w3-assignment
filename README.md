# Week3 키오스크 구현 과제
## About Project
| 구분  | 내용                                                               |
|-----|------------------------------------------------------------------|
| 목적  | 요구사항이 있는 과제 및 클래스 설계를 할 수 있다.                                    |
| 기간  | 2025.1.13 ~ 2025.1.14                                            |
| 설명  | CLI 기반으로 키오스크의 동작을 시뮬레이션하는 프로젝트. 음식을 장바구니에 담고 총액을 계산하여 주문할 수 있다. |
| JDK | Amazon Corretto 17.0.13 - aarch64                                |

<br/><br/><br/>

## Features
### 카테고리 선택
<img src="https://github.com/yeonic/nbc-w3-assignment/blob/main/resource/feature1.png" width="30%" alt="feature1">

- Burger, Drink, Dessert의 카테고리 중 하나를 선택하는 화면.
- 0 입력시 프로그램 종료, 잘못된 입력은 예외처리
  <br/><br/>

### 메뉴 선택 및 장바구니 추가
<img src="https://github.com/yeonic/nbc-w3-assignment/blob/main/resource/feature2.png" width="40%" alt="feature2">

- *1. 카테고리 선택* 에서 골랐던 카테고리에 맞는 메뉴들 나열.
- 선택한 메뉴가 출력되고, 장바구니에 추가할 것인지 묻는 대화창 출력.
<br/><br/>

### 주문서
#### 주문서 인터페이스<br/>
<img src="https://github.com/yeonic/nbc-w3-assignment/blob/main/resource/feature3.png" width="65%" alt="feature3">

- 장바구니에 추가된 항목이 있으면 *1. 카테고리 선택* 화면에 추가로 주문 관련 선택지가 나타남(그림 최상단)
- 주문서에는 담은 항목과 총 금액이 나타남
<br/>

#### 장바구니 제거<br/>
<img src="https://github.com/yeonic/nbc-w3-assignment/blob/main/resource/feature4.png" width="50%" alt="feature4">

- 주문서의 *메뉴 삭제* 항목을 선택하여 진입.
- 메뉴 이름의 일부를 입력하면 해당하는 메뉴는 전부 삭제
<br/>

#### 할인 정책 적용 및 주문완료<br/>
<img src="https://github.com/yeonic/nbc-w3-assignment/blob/main/resource/feature5.png" width="50%" alt="feature5">

- 주문서의 *주문* 항목을 선택하여 진입.
- 할인 정책을 보여주는 화면이 나타남. 번호로 할인 정책 적용.
- 주문이 완료되면, 주문 총액이 나타나고 장바구니는 비워짐.
<br/><br/><br/>

## Structure
### class Kiosk
- Kiosk 클래스는 `KioskMessage`와 `SelectionPhase`를 중심으로 동작한다.
- Kiosk 클래스에는 각 Phase에 따라 사용자의 입력을 처리하는 Handler들이 존재.
- 이 Handler는 입력을 처리한 결과를 `KioskMessage`와 `SelectionPhase`에 반영.
<img src="https://github.com/yeonic/nbc-w3-assignment/blob/main/resource/structure1.png" width="80%" alt="structure1">

### interface KioskMessage
- Kiosk에서 보여질 메시지를 표현하는 인터페이스
- 구현체로는 `MenuKioskMessage`, `StringKioskMessage`, `ComposedKioskMessage`가 존재.
  - `MenuKioskMessage`는 전달되는 메뉴에 따라 동적으로 변하는 메시지이다.
  - `StringKioskMessage`는 별도의 메뉴 엔트리 없이 String으로 표현 가능한 메시지이다.
  - `ComposedKioskMessage`는 둘 이상의 KioskMessage로 구성된 메시지이다.
    <br/><br/>

### class Menu, MenuItem
- `MenuItem`은 키오스크에 등록된 메뉴를 모델링한 클래스이다.
  - 이름, 가격, 설명을 property로 갖는다.
- `Menu`는 카테고리별로 묶이는 `MenuItem`을 관리하는 객체이다.
  - 카테고리, `MenuItem`의 List를 property로 갖는다.
