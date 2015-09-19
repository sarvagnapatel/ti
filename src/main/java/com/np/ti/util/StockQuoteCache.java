package com.np.ti.util;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.concurrent.ExecutionException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.stock.StockQuote;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class StockQuoteCache {
	private static volatile Object lock = new Object();
	private volatile static StockQuoteCache instance = null;
	private StockQuoteCache() {
		 
	}
	
	public static StockQuoteCache getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new StockQuoteCache();
			}
		}
		return instance;
	}

	 LoadingCache<String, StockQuote> quoteCache = 
	         CacheBuilder.newBuilder()
	            .build(new CacheLoader<String, StockQuote>(){ 
	               @Override
	               public StockQuote load(String symbol) throws Exception {
	                  //make the expensive call
	                  return getStockQuote(symbol);
	            }});
	 
	 public StockQuote getQuote(String symbol) {
		 try {
			return quoteCache.get(symbol);
		} catch (ExecutionException e) {
			throw new RuntimeException(e.toString());
		}
	 }
	 
	 private StockQuote getStockQuote(String symbol) {
			Stock stock = null;
			try {
				stock = YahooFinance.get(symbol);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(stock == null) {
				return null;
			}
			
			return stock.getQuote();
			
		}
	 
	 
	 
}
