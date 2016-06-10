package com.gbce.services;

import com.gbce.GBCEException;
import com.gbce.domain.stock.Stock;
import com.gbce.domain.stock.StockType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.time.Duration;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

/**
 * Created by amitm on 02/06/2016.
 */
public class GBCEStockServiceTests {

    GBCEStockService gbceStockService = new GBCEStockService();

    final double marketPrice=100d;


    @Before
    public void setup(){
        gbceStockService=new GBCEStockService();
    }

    @Test
    public void testRecordTrade() throws GBCEException{
        String stockSymbol="TEA";
        gbceStockService.recordTrade(stockSymbol,10L,200d,true);
    }

    @Test
    public void testCalculatePtoERatio_COMMON() throws GBCEException{
        String stockSymbol="TEA";
        final double lastDividend=12d;
        final Double marketPrice=100d;
        GBCEStockService spyStockService = spy(gbceStockService);
        Stock stock=new Stock(stockSymbol, StockType.COMMON,lastDividend,120d,200d,100L);
        Stock spyStock = spy(stock);
        when(spyStock.getTicker()).thenReturn(marketPrice);
        when(spyStockService.findStock(stockSymbol)).thenReturn(spyStock);
        Double ptoERatio=spyStockService.calculatePtoERatio(stockSymbol);

        Double dividend=(lastDividend /marketPrice);

        Assert.assertEquals(marketPrice/dividend ,ptoERatio, 0.001);
    }
    @Test
    public void testCalculatePtoERatio_PREFFRED() throws GBCEException{
        String stockSymbol="POP";
        final double fixedDividend=12d;
        final double parValue=2000d;
        final Double marketPrice=100d;
        GBCEStockService spyStockService = spy(gbceStockService);
        Stock stock=new Stock(stockSymbol, StockType.PREFERRED,120d,fixedDividend,parValue,100L);
        Stock spyStock = spy(stock);
        when(spyStock.getTicker()).thenReturn(marketPrice);
        when(spyStockService.findStock(stockSymbol)).thenReturn(spyStock);
        Double ptoERatio=spyStockService.calculatePtoERatio(stockSymbol);

        Double dividend=(fixedDividend*parValue /marketPrice);

        Assert.assertEquals(marketPrice/dividend ,ptoERatio, 0.001);
    }
    @Test
    public void testCalculateDividendYield_COMMON() throws GBCEException{
        String stockSymbol="TEA";
        final double lastDividend=12d;
        final Double marketPrice=100d;
        GBCEStockService spyStockService = spy(gbceStockService);
        Stock stock=new Stock(stockSymbol, StockType.COMMON,lastDividend,120d,200d,100L);
        Stock spyStock = spy(stock);
        when(spyStock.getTicker()).thenReturn(marketPrice);
        when(spyStockService.findStock(stockSymbol)).thenReturn(spyStock);
        Double dividend=spyStockService.calculateDividendYield(stockSymbol);

        Assert.assertEquals(lastDividend /marketPrice ,dividend, 0.001);
    }
    @Test
    public void  testCalculateDividendYield_PREFFRED() throws GBCEException{
        String stockSymbol="POP";
        final double fixedDividend=12d;
        final double parValue=2000d;
        final Double marketPrice=100d;
        GBCEStockService spyStockService = spy(gbceStockService);
        Stock stock=new Stock(stockSymbol, StockType.PREFERRED,120d,fixedDividend,parValue,100L);
        Stock spyStock = spy(stock);
        when(spyStock.getTicker()).thenReturn(marketPrice);
        when(spyStockService.findStock(stockSymbol)).thenReturn(spyStock);
        Double dividend=spyStockService.calculateDividendYield(stockSymbol);

        Assert.assertEquals(fixedDividend*parValue /marketPrice ,dividend, 0.001);
    }
    @Test
    public void testCalculateVolumeWeightedStock() throws GBCEException {
        GBCEStockService stocksrv=new GBCEStockService();
        setUpTrade(stocksrv);
        String stockSymbol="TEA";
        Duration minutes = Duration.ofMinutes(15);
        Double weightedStock=stocksrv.calculateVolumeWeightedStock(stockSymbol,minutes);
        // (100d*10+100*10+100*10+100*10+100*10) / (10+10+10+10+10)
        double volumewtstock=(100d*10+100*10+100*10+100*10+100*10) / (10+10+10+10+10);
        Assert.assertEquals(weightedStock,volumewtstock,0.001);

    }
    @Test
    public void testCalculateGBCEIndexForAllStock() throws GBCEException{
        GBCEStockService stocksrv=new GBCEStockService();
        setUpTrade(stocksrv);
        Double stockIndex=stocksrv.calculateGBCEIndexForAllStock();
        //index = 5th root of 100^5 --> 100
        Double index = Math.pow(Math.pow(100,5),1/5d);
        Assert.assertEquals(stockIndex,index,0.001);
    }
    private void setUpTrade( GBCEStockService stocksrv){
        Stream.of("TEA","POP","ALE","GIN","JOE")
                .forEach(stockSymbol->{
                            try{
                                stocksrv.recordTrade(stockSymbol,10L,marketPrice,true);
                            }catch(Exception e){}
                        }
                );
    }
}
