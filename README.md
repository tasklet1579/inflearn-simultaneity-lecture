<details>
<summary>✍️ MySQL 설치</summary>
<br>

docker pull mysql

docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1234 --name mysql mysql

docker ps

</details>

<details>
<summary>✍️ 레이스 컨디션</summary>
<br>

둘 이상의 스레드가 공유 데이터에 액세스할 수 있고 동시에 변경하려고 할 때 발생하는 문제

- 둘 이상의 스레드 : 요청
- 공유 데이터 : 재고 데이터
- 동시에 변경하려고 할 때 : 수량을 업데이트할 때
- 발생하는 문제 : 값이 정상적으로 바뀌지 않는 문제

예상

|Thread-1|Stock|Thread-2|
|---|---|---|
|select *<br>from stock<br>where id = 1|{id : 1, quantity : 5}| |
|update stock<br>set quantity = 4<br>where id = 1|{id : 1, quantity : 4}| |
| |{id : 1, quantity : 4}|select *<br>from stock<br>where id = 1|
| |{id : 1, quantity : 3}|update stock<br>set quantity = 3<br>where id = 1|

실제

|Thread-1|Stock|Thread-2|
|---|---|---|
|select *<br>from stock<br>where id = 1|{id : 1, quantity : 5}| |
| |{id : 1, quantity : 5}|select *<br>from stock<br>where id = 1|
|update stock<br>set quantity = 4<br>where id = 1|{id : 1, quantity : 4}| |
| |{id : 1, quantity : 4}|update stock<br>set quantity = 4<br>where id = 1|

해결 방법

- 하나의 스레드만 데이터에 액세스 할 수 있도록 한다.

</details>

<details>
<summary>✍️ Spring @Transactional과 Java synchronized를 동시에 사용할 수 없는 문제</summary>
<br>

Java synchronized는 한번에 하나의 스레드만 메서드에 접근할 수 있도록 한다.

하지만 Spring @Transactional은 프록시로 동작하기 때문에 함께 사용된다면 synchronized 코드 블럭은 트랜잭션 범위 내에서 실행된다.

그런 경우 데이터베이스에 커밋하기 전 다른 스레드에서 메서드에 접근할 수 있게 되고 동일한 데이터에 접근하기 때문에 문제가 발생할 수 있다.
</details>
