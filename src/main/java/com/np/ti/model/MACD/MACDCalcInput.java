package com.np.ti.model.MACD;

import java.util.List;

import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.quotes.stock.StockQuote;

public class MACDCalcInput {
	private int fastPeriod;
	private int slowPeriod;
	private int signalPeriod;
	private List<HistoricalQuote> quotes;
	private StockQuote quote;
	
	public MACDCalcInput() {
		
	}
	
	public int getFastPeriod() {
		return fastPeriod;
	}
	public void setFastPeriod(int fastPeriod) {
		this.fastPeriod = fastPeriod;
	}
	public int getSlowPeriod() {
		return slowPeriod;
	}
	public void setSlowPeriod(int slowPeriod) {
		this.slowPeriod = slowPeriod;
	}
	public int getSignalPeriod() {
		return signalPeriod;
	}
	public void setSignalPeriod(int signalPeriod) {
		this.signalPeriod = signalPeriod;
	}
	public List<HistoricalQuote> getQuotes() {
		return quotes;
	}
	public void setQuotes(List<HistoricalQuote> quotes) {
		this.quotes = quotes;
	}

	public StockQuote getQuote() {
		return quote;
	}

	public void setQuote(StockQuote quote) {
		this.quote = quote;
	}
	
	
	
}