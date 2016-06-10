package com.gbce.dao;

import com.gbce.domain.stock.Stock;
import com.gbce.domain.stock.StockType;
import java.util.*;
import java.util.stream.Stream;

/**
 * Stock DAO
 */
public class StockDAO {
    private static HashMap<String,Stock> stocks = new HashMap<>();

    static {
        Stream.of(
                new Stock("TEA", StockType.COMMON, 0d, 0d,100d,1000L ),
                new Stock("POP", StockType.COMMON, 8d, 0d,100d,1000L ),
                new Stock("ALE", StockType.COMMON, 23d, 0d,60d,1000L ),
                new Stock("GIN", StockType.PREFERRED, 8d, 2d,100d,1000L ),
                new Stock("JOE", StockType.COMMON, 13d, 0d,250d,1000L )
        ).forEach(e->stocks.put(e.getSymbol(),e));
    }
    protected StockDAO(){

    }

    /**
     * finds stock for given stock symbol
     */
    public Stock findStock(String stockSymbol){
        Stock stock=stocks.get(stockSymbol);
        return stock;
    }

    /**
     *  returns unmodifiable map of all stocks.
     */
    public Map<String,Stock> getStocks() {
        return Collections.unmodifiableMap(stocks);
    }
}
