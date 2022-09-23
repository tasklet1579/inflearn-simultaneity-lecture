package edu.inflearn.stock.facade;

import edu.inflearn.stock.repository.RedisRepository;
import edu.inflearn.stock.service.StockService;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class LettuceLockStockFacade {
    private final RedisRepository redisRepository;
    private final StockService stockService;

    public LettuceLockStockFacade(RedisRepository redisRepository, StockService stockService) {
        this.redisRepository = redisRepository;
        this.stockService = stockService;
    }

    public void decrease(Long key, Long quantity) throws InterruptedException {
        while (!redisRepository.lock(key)) {
            TimeUnit.MILLISECONDS.sleep(100); // 시간을 두고 부하를 줄여준다
        }

        try {
            stockService.decrease(key, quantity);
        } finally {
            redisRepository.unlock(key);
        }
    }
}
