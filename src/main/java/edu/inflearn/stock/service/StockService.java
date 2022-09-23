package edu.inflearn.stock.service;

import edu.inflearn.stock.domain.Stock;
import edu.inflearn.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // 락을 해제하기 전에 데이터베이스에 커밋한다.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public /*synchronized*/ void decrease(Long id, Long quantity) {
        // 재고 조회
        Stock stock = stockRepository.findById(id)
                                     .orElseThrow(RuntimeException::new);

        // 재고 감소
        stock.decrease(quantity);

        // 저장
        stockRepository.saveAndFlush(stock);
    }
}
