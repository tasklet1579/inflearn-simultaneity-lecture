package edu.inflearn.stock.repository;

import edu.inflearn.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select s from Stock s where s.id = :id")
    Stock findByIdWithPessimisticLock(Long id);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query(value = "select s from Stock s where s.id = :id")
    Stock findByIdWithOptimisticLock(Long id);
}
