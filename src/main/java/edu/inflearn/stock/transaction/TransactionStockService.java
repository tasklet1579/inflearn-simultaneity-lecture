package edu.inflearn.stock.transaction;

import edu.inflearn.stock.service.StockService;

public class TransactionStockService {
    private final StockService stockService;

    public TransactionStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) {
        startTransaction();

        stockService.decrease(id, quantity);

        endTransaction();
    }

    public void startTransaction() {

    }

    public void endTransaction() {

    }
}
