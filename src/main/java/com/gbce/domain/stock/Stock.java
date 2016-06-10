package com.gbce.domain.stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * stock structure
 */
public class Stock {
    String symbol;
    StockType type=StockType.COMMON;
    Double lastDividend=0D;
    Double fixedDividend=0D;
    Double parValue;
    Double ticker;
    Long quantity =1L;
    List<StockTrade> trades=new ArrayList<>();

    public Stock(){

    }
    public Stock(String stockSymbol,StockType stockType, Double lastDividend,
                 Double fixedDividend, Double parValue,Long quantity){
        this.symbol=stockSymbol;
        this.lastDividend=lastDividend;
        this.type=stockType;
        this.fixedDividend=fixedDividend;
        this.parValue=parValue;
        this.quantity=quantity;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public StockType getType() {
        return type;
    }

    public Double getLastDividend() {
        return lastDividend;
    }

    public Double getFixedDividend() {
        return fixedDividend;
    }

    public Double getParValue() {
        return parValue;
    }

    public void setTicker(Double ticker) {
        this.ticker = ticker;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Double getTicker() {
        return ticker;
    }


    public void addTrade(StockTrade trade) {
        this.trades.add(trade);
        this.quantity -= trade.getQuantity();
        this.setTicker(trade.getPrice());
    }
    public List<StockTrade> getTrades(){
        return Collections.unmodifiableList(trades);
    }
}
