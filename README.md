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

<details>
<summary>✍️ Database를 활용한 다양한 방법</summary>
<br>

Pessimistic Lock (exclusive lock)

- 데이터에 락을 걸어서 정합성을 맞추는 방법입니다.
- 다른 트랜잭션에서는 락이 해제되기 전까지 데이터에 접근할 수 없습니다.
- 데드락이 걸릴 수 있기 때문에 주의해야 합니다.

Optimistic Lock

- 락이 아닌 버전을 이용함으로써 정합성을 맞추는 방법입니다.
- 데이터를 읽은 후에 업데이트할 때 읽은 버전이 맞는지 확인합니다.
- 읽은 버전에서 수정사항이 생겼을 경우 애플리케이션에서 다시 읽은 후 작업을 수행해야 합니다.

Named Lock

- 이름을 가진 락입니다.
- 해당 락은 다른 세션에서 획득 및 해제가 불가능합니다.
- 트랜잭션이 종료될 때 락이 자동으로 해제되지 않습니다.
- 별도의 명령어로 해제하거나 선점 시간이 끝나야 해제됩니다.

</details>

<details>
<summary>✍️ Redis 라이브러리</summary>
<br>

Lettuce

- setnx 명령어를 활용하여 분산락 구현
- spin lock 방식
    - 스레드가 락을 획득할 수 있는지 반복적으로 확인함

Redisson

- pub-sub 반의 락 구현 제공
    - 채널을 만들고 락을 점유중인 스레드가 대기중인 스레드에게 해제를 알림
    - 별도의 리트라이 로직을 작성할 필요 없음

</details>

<details>
<summary>✍️ Redis 설치</summary>
<br>

docker pull redis

docker run --name myredis -d -p 6379:6379 redis

docker ps

docker exec -it myredis redis-cli

setnx 1 lock

</details>

<details>
<summary>✍️ Redis 라이브러리 장단점</summary>
<br>

Lettuce

- 구현이 간단하다
- spring data redis를 이용하면 lettuce가 기본이기 때문에 별도의 라이브러리를 사용하지 않아도 된다.
- spin lock 방식이기 때문에 동시에 많은 스레드가 lock 획득 대기 상태라면 redis에 부하가 갈 수 있다.

Redisson

- 락 획득 재시도를 기본으로 제공한다.
- pub-sub 방식으로 구현이 되어있기 때문에 lettuce와 비교했을 때 redis에 부하가 덜 간다.
- 별도의 라이브러리를 사용해야 한다.
- lock을 라이브러리 차원에서 제공해주기 떄문에 사용법을 공부해야 한다.

실무에서는?

- 재시도가 필요하지 않은 lock은 lettuce 활용
    - 선착순으로 물건을 구매하는 경우
- 재시도가 필요한 경우에는 redisson 활용
    - 순서대로 물건을 구매하는 경우

</details>
