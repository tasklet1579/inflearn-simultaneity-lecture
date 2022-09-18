package edu.inflearn.stock.service;

import edu.inflearn.stock.domain.Stock;
import edu.inflearn.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptimisticLockStockService {
    private final StockRepository stockRepository;

    public OptimisticLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public synchronized void decrease(Long id, Long quantity) {
        // 재고 조회
        Stock stock = stockRepository.findByIdWithOptimisticLock(id);

        // 재고 감소
        stock.decrease(quantity);

        // 저장
        stockRepository.saveAndFlush(stock);
    }
}
