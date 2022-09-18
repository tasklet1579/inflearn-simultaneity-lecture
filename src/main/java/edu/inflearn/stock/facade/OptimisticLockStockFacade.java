package edu.inflearn.stock.facade;

import edu.inflearn.stock.service.OptimisticLockStockService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OptimisticLockStockFacade {
    private final OptimisticLockStockService optimisticLockStockService;

    public OptimisticLockStockFacade(OptimisticLockStockService optimisticLockStockService) {
        this.optimisticLockStockService = optimisticLockStockService;
    }

    // 실패했을 경우 재시도 로직을 개발자가 직접 작성해야 한다.
    // 충돌이 빈번하게 일어나거나 예상된다면 Pessimistic Lock을 고려하자.
    public void decrease(Long id, Long quantity) {
        while (true) {
            try {
                optimisticLockStockService.decrease(id, quantity);

                break;
            } catch (Exception e) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
