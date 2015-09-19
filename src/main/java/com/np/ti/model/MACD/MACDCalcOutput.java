package com.np.ti.model.MACD;

import org.joda.time.LocalDate;

import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.quotes.stock.StockQuote;

public class MACDCalcOutput {
	private LocalDate asofDate;
	private String symbol;
	private HistoricalQuote historicalQuote;
	private double macd;
	private double macdSignal;
	private double macdHist;
	private StockQuote quote;
	
	public LocalDate getAsofDate() {
		return asofDate;
	}
	public void setAsofDate(LocalDate asofDate) {
		this.asofDate = asofDate;
	}
	public double getMacd() {
		return macd;
	}
	public void setMacd(double macd) {
		this.macd = macd;
	}
	public double getMacdSignal() {
		return macdSignal;
	}
	public void setMacdSignal(double macdSignal) {
		this.macdSignal = macdSignal;
	}
	public double getMacdHist() {
		return macdHist;
	}
	public void setMacdHist(double macdHist) {
		this.macdHist = macdHist;
	}
	
	public HistoricalQuote getHistoricalQuote() {
		return historicalQuote;
	}
	public void setHistoricalQuote(HistoricalQuote historicalQuote) {
		this.historicalQuote = historicalQuote;
	}
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public StockQuote getQuote() {
		return quote;
	}
	public void setQuote(StockQuote quote) {
		this.quote = quote;
	}
	@Override
	public String toString() {
		return "MACDCalcOutput [asofDate=" + asofDate + ", symbol=" + symbol
				+ ", quote=" + historicalQuote + ", macd=" + macd + ", macdSignal="
				+ macdSignal + ", macdHist=" + macdHist + "]";
	}
	
	
	
	
}