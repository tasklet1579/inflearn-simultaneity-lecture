package edu.inflearn.stock.facade;

import edu.inflearn.stock.service.StockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonLockStockFacade {
    private final RedissonClient redissonClient;
    private final StockService stockService;

    public RedissonLockStockFacade(RedissonClient redissonClient, StockService stockService) {
        this.redissonClient = redissonClient;
        this.stockService = stockService;
    }

    public void decrease(Long key, Long quantity) {
        RLock rLock = redissonClient.getLock(key.toString());

        try {
            boolean available = rLock.tryLock(5, 1, TimeUnit.SECONDS);

            if (!available) {
                System.out.println("락 획득 실패");
                return;
            }

            stockService.decrease(key, quantity);
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            rLock.unlock();
        }
    }
}
