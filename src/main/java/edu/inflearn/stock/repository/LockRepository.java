package edu.inflearn.stock.repository;

import edu.inflearn.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// 실무에서는 커넥션 풀 문제로 데이터소스를 분리하고 JDBC를 사용하는 것을 추천한다.
public interface LockRepository extends JpaRepository<Stock, Long> {
    @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
    void getLock(String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(String key);
}
