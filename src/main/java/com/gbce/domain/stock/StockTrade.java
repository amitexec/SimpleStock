package com.gbce.domain.stock;


import java.time.Instant;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Structure to support stock trade
 */
public class StockTrade {
    String stockSymbol;
    Long quantity;
    Double price;
    TradeType type;
    Date time;

    private StockTrade(Builder builder){
        this.stockSymbol=builder.getStockSymbol();
        this.quantity=builder.getQuantity();
        this.price=builder.getPrice();
        this.type=builder.getType();
        this.time=builder.getTime();
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public TradeType tradeType() {
        return type;
    }

    public Date getTime(){ return time;}
    public static class Builder{
        String stockSymbol;
        Long quantity;
        Double price;
        TradeType type;
        Date time;
        public Builder withStock(Stock stock){
            this.stockSymbol=stock.getSymbol();
            return this;
        }
        public Builder withQuantity(Long quantity){
            this.quantity=quantity;
            return this;
        }
        public Builder withPrice(Double price){
            this.price=price;
            return this;
        }
        public Builder withBuyIndicator(boolean buyIndicator){
            this.type=buyIndicator? TradeType.BUY: TradeType.SELL;
            return this;
        }
        public Builder withTime(Date time){
            this.time=time;
            return this;
        }
        public Builder withTime(Instant time){
            this.time=Date.from(time);
            return this;
        }

        public Date getTime() {
            return time;
        }

        public String getStockSymbol() {
            return stockSymbol;
        }

        public long getQuantity() {
            return quantity;
        }

        public void setQuantity(long quantity) {
            this.quantity = quantity;
        }

        public Double getPrice() {
            return price;
        }

        public TradeType getType() {
            return type;
        }

        public StockTrade build(){
            checkNotNull(stockSymbol,"Stock symbol cannot be null");
            checkNotNull(price,"Price cannot be null");
            checkNotNull(quantity,"quantity can not be null");
            checkNotNull(type,"TradeType cannot be null");
            checkNotNull(time, "Trade time cannot be null");

            //Preconditions.checkState(price <= 0.0, "Price needs to be positive.");
            //Preconditions.checkState(quantity <= 0.0, "Quantity needs to be positive.");

            return new StockTrade(this);
        }
    }
}
