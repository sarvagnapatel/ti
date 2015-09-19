package com.np.ti.model;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.joda.time.LocalDate;

import com.google.common.collect.Lists;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class StockHelper{

	private String mSymbol;
	private Stock mStock;
	
	public StockHelper(String symbol) throws IOException {
		Stock stock = YahooFinance.get(symbol);
		mStock = stock;
		mSymbol = symbol;
	}
	
	
	public List<HistoricalQuote> getHistory(LocalDate dateStart, LocalDate dateEnd, Interval interval) throws IOException {
		return Lists.reverse(mStock.getHistory(dateStart.toDateTimeAtStartOfDay().toDateTime().toCalendar(Locale.getDefault()),
				dateEnd.toDateTimeAtStartOfDay().toDateTime().toCalendar(Locale.getDefault()), interval));
	}


	public String getSymbol() {
		return mSymbol;
	}


	public void setSymbol(String mSymbol) {
		this.mSymbol = mSymbol;
	}


	public Stock getStock() {
		return mStock;
	}


	public void setStock(Stock mStock) {
		this.mStock = mStock;
	}
	
	
	

}
