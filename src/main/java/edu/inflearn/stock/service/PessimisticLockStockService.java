package edu.inflearn.stock.service;

import edu.inflearn.stock.domain.Stock;
import edu.inflearn.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessimisticLockStockService {
    private final StockRepository stockRepository;

    public PessimisticLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // 충돌이 빈번하다면 Optimistic Lock 보다 성능이 좋을 수 있다.
    // 락을 통해 데이터를 제어하기 때문에 데이터 정합성이 어느 정도 보장된다.
    // 성능 감소가 있을 수 있다.
    @Transactional
    public synchronized void decrease(Long id, Long quantity) {
        // 재고 조회
        Stock stock = stockRepository.findByIdWithPessimisticLock(id);

        // 재고 감소
        stock.decrease(quantity);

        // 저장
        stockRepository.saveAndFlush(stock);
    }
}
