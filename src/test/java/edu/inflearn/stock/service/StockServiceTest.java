package edu.inflearn.stock.service;

import edu.inflearn.stock.domain.Stock;
import edu.inflearn.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StockServiceTest {
    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        Stock stock = new Stock(1L, 100L);

        stockRepository.saveAndFlush(stock);
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @Test
    public void stock_decrease() {
        stockService.decrease(1L, 1L);

        Stock stock = stockRepository.findById(1L)
                                     .orElseThrow(RuntimeException::new);

        assertEquals(99, stock.getQuantity());
    }
}
