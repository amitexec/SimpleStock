package com.gbce.services;

import com.gbce.GBCEException;
import com.gbce.domain.stock.Stock;
import com.gbce.domain.stock.StockTrade;
import com.gbce.domain.stock.StockType;
import com.google.common.base.Optional;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.gbce.dao.EntityManager.getEntityManager;
import static  com.google.common.base.Preconditions.checkNotNull;

/**
 * GBCE stock service to support stock operations
 */
public class GBCEStockService {
    /**
     * calculates dividend yield for given valid stock symbol as
     *    for Common, last dividend / market price
     *    for Common, fixed dividend * face value / market price
     * @throws GBCEException if no trades performed.
     */
    public double calculateDividendYield(String stockSymbol) throws GBCEException {
            return calculateDividendYield(findStock(stockSymbol));
    }


    public double  calculateDividendYield(Stock stock) throws GBCEException {
        double dividendYield;

        if(stock.getTicker() == null){
            throw new GBCEException(String.format("Ticker price is not defined stock for stock %s.",stock.getSymbol()));
        }
        if(StockType.COMMON.equals(stock.getType())){
            dividendYield=stock.getLastDividend()/stock.getTicker();
        }else{
            //PREFERRED
            //TODO make sure PREFERRED stock has fixed dividend
            dividendYield = stock.getFixedDividend()*stock.getParValue() /stock.getTicker();
        }

        return  dividendYield;
    }

    /**
     * finds stock object for given stock symbol
     * @param stockSymbol
     * @return
     * @throws GBCEException if no stock found for given stock symbol
     */
    protected Stock findStock(String stockSymbol) throws GBCEException {
        checkNotNull(stockSymbol,"Stock symbol cannot be null");

        Stock stock = getEntityManager().getStockDAO().findStock(stockSymbol.toUpperCase());

        if(stock==null) {
            throw new GBCEException(String.format("Unable to find known stock for %s.", stockSymbol));
        }

        return  stock;
    }

    /**
     * calculates P2E ratio as Market Price / Dividend Yield
     * @param stockSymbol
     * @return
     * @throws GBCEException
     */
    public double calculatePtoERatio(String stockSymbol) throws GBCEException {
            double p2eRatio ;
            Stock stock = findStock(stockSymbol);
            if (stock.getTicker() == null) {
                throw new GBCEException(String.format("Ticker price is not defined stock for stock %s.", stockSymbol));
            }
            p2eRatio =  stock.getTicker()/calculateDividendYield(stock);
            return p2eRatio;
    }

    /**
     * records trade for given stock
     * @throws GBCEException
     */
    public void recordTrade(String stockSymbol,Long quantiy, Double price, boolean buyIndicator) throws GBCEException {
        Stock stock=findStock(stockSymbol);

        //TODO BUY SELL trading logic
        StockTrade trade=new StockTrade.Builder()
                .withStock(stock)
                .withBuyIndicator(buyIndicator)
                .withQuantity(quantiy)
                .withPrice(price)
                .withTime(Instant.now())
                .build();

        recordTrade(stock,trade);
    }
    public void recordTrade(Stock stock, StockTrade trade) throws GBCEException {
        checkNotNull(trade,"Trade cannot be null.");
        checkNotNull(stock,"Stock cannot be null.");


        //TODO concurrency check
        stock.addTrade(trade);
    }

    /**
     * calculates GBCE index
     * @return
     */
    public Double calculateGBCEIndexForAllStock() {
        List<Optional<Double>> stockPrices=new ArrayList<>();
        Double priceMult=1.0d;
        getEntityManager().getStockDAO().getStocks().forEach((x,y)-> stockPrices.add(getPrice(y)));
        int count=0;
        for(Optional<Double> op:stockPrices){
            if(op.isPresent()){
                ++count;
                priceMult*=op.get();
            }
        }
        return Math.pow(priceMult,1.0/count);
    }
    protected Optional<Double> getPrice(Stock stock){
        return  Optional.fromNullable(stock.getTicker()).or(Optional.fromNullable(stock.getParValue()));

    }

    /**
     * calculates volume weighted stock
     * @throws GBCEException
     */

    public double calculateVolumeWeightedStock(String stockSymbol, Duration minutes) throws GBCEException {
        checkNotNull(minutes,"Duration cannot be null");

        Instant current = Instant.now();
        List<StockTrade> stockTrades= findStock(stockSymbol).getTrades().stream()
                .filter(stockTrade -> Duration.between(current,stockTrade.getTime().toInstant()).compareTo(minutes)<0)
                .collect(Collectors.toList());

        if(stockTrades.isEmpty()){
            new GBCEException(String.format("No trades found during last %s minutes",minutes.toMinutes()));
        }

        double stockPriceSum=0.0d;
        long stockQuantitySum= 0l;

        for(StockTrade trade:stockTrades){
            stockPriceSum += (trade.getQuantity()*trade.getPrice());
            stockQuantitySum +=trade.getQuantity();
        }
        return stockPriceSum/stockQuantitySum;
    }
}
