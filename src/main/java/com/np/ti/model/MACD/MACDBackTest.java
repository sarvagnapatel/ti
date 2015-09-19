package com.np.ti.model.MACD;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;

import yahoofinance.histquotes.HistoricalQuote;

import com.google.common.collect.Lists;
import com.np.ti.model.EnumCross;
import com.np.ti.util.StockQuoteCache;

public class MACDBackTest {
	
	private static final boolean maxLossEnabled = false;

	Map<LocalDate, MACDCalcOutput> mMap = new TreeMap<LocalDate, MACDCalcOutput>();
	
	Map<LocalDate, MACDBackTestRow> mBackTestRows = new TreeMap<LocalDate, MACDBackTest.MACDBackTestRow>();
	
	private static double INITIAL_AMT = 10000;
	
	private static double BUY_HIST_DIFF = 0d;
	

	private static double SELL_HIST_DIFF = 0d;
	
	private static double MAX_LOSS = -0.03;
	private String mTestName;
	
	public MACDBackTest(Map<LocalDate, MACDCalcOutput> map, String testName) {
		mMap = map;
		mTestName = testName;
	}
	
	//Simple crossOver
	public void runRegressionSimpleCrossOver() {
		List<MACDCalcOutput> output =  Lists.newArrayList(mMap.values());
		
		boolean hasPosition = false;
		MACDBackTestRow row = new MACDBackTestRow();
		
		double amt = INITIAL_AMT;
		
		for (int i = 0; i < output.size(); i++) {
			if (i == 0) {
				continue;
			}
			
			MACDCalcOutput macdCalcOutput = output.get(i);
			
			double macd = macdCalcOutput.getMacd();
			double macdHist = macdCalcOutput.getMacdHist();
			double macdSignal = macdCalcOutput.getMacdSignal();
			
			MACDCalcOutput prevMacdCalcOutput = output.get(i -1);
			
			double previousMacd = prevMacdCalcOutput.getMacd();
			double previousMacdHist = prevMacdCalcOutput.getMacdHist();
			double previousMacdSignal = prevMacdCalcOutput.getMacdSignal();

			HistoricalQuote quote = macdCalcOutput.getHistoricalQuote();
			
			EnumCross cross;
			
			LocalDate asofDate = macdCalcOutput.getAsofDate();
			
			LocalDate debugDate = new LocalDate(2006, 8, 20);
			
			if(debugDate.equals(asofDate) && "FIS".equals(macdCalcOutput.getSymbol())) 
				System.out.println("A");
		
			
			//buy
			if (macd > macdSignal && previousMacd < previousMacdSignal && macdHist > BUY_HIST_DIFF && !hasPosition) {
				row = new MACDBackTestRow();

				mBackTestRows.put(asofDate, row);
				row.setBuyDate(asofDate);
				row.setMacd(macd);
				row.setMacdHist(macdHist);
				row.setMacdSignal(macdSignal);
				row.setPreviousMacd(previousMacd);
				row.setPreviousMacdHist(previousMacdHist);
				row.setPreviousMacdSignal(previousMacdSignal);
				row.setHistoricalQuote(quote);
				row.setInvestedAmt(amt);
				
				row.setBuyPrice(quote.getAdjClose().doubleValue());
				hasPosition = true;
				
				cross = EnumCross.BULLISH_CROSS; 				
				//|| previousMacd > previousMacdSignal)&& macd < 0
			} else if (hasPosition && (macd < macdSignal && previousMacd > previousMacdSignal) || 
					hasMaxLossOccured(row, quote)) //&& macdHist < SELL_HIST_DIFF
			{ //sell
				cross = EnumCross.BEARISH_CROSS;
				if (row != null) {
					row.setSellDate(asofDate);
					row.setSellPrice(quote.getAdjClose().doubleValue());
					amt = amt * ( 1 + row.getRtn());
				}
				hasPosition = false;
			} else {
				cross = EnumCross.UNCH;
			}
			
			
		}
		
		//printAllRows();
		printLastRow();
	}

	private void printLastRow() {
		int size = 0;
		if (mBackTestRows.isEmpty()) {
			return;
		}
		size = mBackTestRows.values().size();
		List<MACDBackTestRow> list = Lists.newArrayList(mBackTestRows.values());
		
		MACDBackTestRow lastRow = list.get(size - 1);
		StringBuilder sb = new StringBuilder();
		sb.append(mTestName + ",");
		sb.append(lastRow.getHistoricalQuote().getSymbol()+",");
		sb.append(lastRow.getReturnAmt());
		if(lastRow.getReturnAmt() == 0) {
			sb.append(lastRow.getInvestedAmt());
		} else {
			sb.append(lastRow.getReturnAmt());
		}
		sb.append(System.getProperty("line.separator"));
		
		File f = new File("/dev/excel/print.csv");
		try {
			FileUtils.writeStringToFile(f, sb.toString(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printAllRows() {
		StringBuilder sb = new StringBuilder();
		File f = new File("/dev/excel/print.csv");
		
		if(!f.exists()) {
			sb.append("Test Name,");
			sb.append("Symbol,");
			sb.append("Buy Date,");
			sb.append("Sell Date,");
			sb.append("Buy Price,");
			sb.append("Sell Price,");
			sb.append("BV,");
			sb.append("EV,");
			sb.append("Rtn");
			sb.append(System.getProperty("line.separator"));
		}
		for (MACDBackTestRow macdCalcOutput : mBackTestRows.values()) {
			sb.append(mTestName + ",");
			sb.append(macdCalcOutput.getHistoricalQuote().getSymbol()+",");
			sb.append(macdCalcOutput.getBuyDate()+",");
			sb.append(macdCalcOutput.getSellDate()+",");
			sb.append(macdCalcOutput.getBuyPrice()+",");
			sb.append(macdCalcOutput.getSellPrice()+",");
			sb.append(macdCalcOutput.getInvestedAmt()+",");
			sb.append(macdCalcOutput.getReturnAmt()+",");
			sb.append(macdCalcOutput.getRtn());
			sb.append(System.getProperty("line.separator"));
		}
		//System.out.println(sb.toString());
		
		
		try {
			FileUtils.writeStringToFile(f, sb.toString(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean hasMaxLossOccured(MACDBackTestRow row, HistoricalQuote quote) {
		if (!maxLossEnabled) {
			return false;
		}
		return row.getCurrentRtn(quote.getAdjClose().doubleValue()) < MAX_LOSS && maxLossEnabled;
	}
	
	public class MACDBackTestRow {
		public HistoricalQuote historicalQuote;
		public double macd;
		public double macdHist;
		public double macdSignal;
		
		public double previousMacd;
		public double previousMacdHist;
		public double previousMacdSignal;
		
		public double buyPrice;
		public double sellPrice;
		
		public LocalDate buyDate;
		public LocalDate sellDate;
		
		public double investedAmt;
		
		public HistoricalQuote getHistoricalQuote() {
			return historicalQuote;
		}
		public void setHistoricalQuote(HistoricalQuote historicalQuote) {
			this.historicalQuote = historicalQuote;
		}
		public double getMacd() {
			return macd;
		}
		public void setMacd(double macd) {
			this.macd = macd;
		}
		public double getMacdHist() {
			return macdHist;
		}
		public void setMacdHist(double macdHist) {
			this.macdHist = macdHist;
		}
		public double getMacdSignal() {
			return macdSignal;
		}
		public void setMacdSignal(double macdSignal) {
			this.macdSignal = macdSignal;
		}
		public double getPreviousMacd() {
			return previousMacd;
		}
		public void setPreviousMacd(double previousMacd) {
			this.previousMacd = previousMacd;
		}
		public double getPreviousMacdHist() {
			return previousMacdHist;
		}
		public void setPreviousMacdHist(double previousMacdHist) {
			this.previousMacdHist = previousMacdHist;
		}
		public double getPreviousMacdSignal() {
			return previousMacdSignal;
		}
		public void setPreviousMacdSignal(double previousMacdSignal) {
			this.previousMacdSignal = previousMacdSignal;
		}
		public double getBuyPrice() {
			return buyPrice;
		}
		public void setBuyPrice(double buyPrice) {
			this.buyPrice = buyPrice;
		}
		public double getSellPrice() {
			if(sellPrice < 1) {
				return StockQuoteCache.getInstance().getQuote(historicalQuote.getSymbol()).getPrice().doubleValue();
			}
			return sellPrice;
		}
		public void setSellPrice(double sellPrice) {
			this.sellPrice = sellPrice;
		}
		public LocalDate getBuyDate() {
			return buyDate;
		}
		public void setBuyDate(LocalDate buyDate) {
			this.buyDate = buyDate;
		}
		public LocalDate getSellDate() {
			return sellDate;
		}
		public void setSellDate(LocalDate sellDate) {
			this.sellDate = sellDate;
		}
		
		public double getInvestedAmt() {
			return investedAmt;
		}
		public void setInvestedAmt(double investedAmt) {
			this.investedAmt = investedAmt;
		}
		public double getReturnAmt() {
			return investedAmt * (1 + getRtn());
		}
		
		public double getCurrentRtn(double currentPrice) {
			if(currentPrice == 0) {
				return 0;
			}
			return (currentPrice - buyPrice) / buyPrice;
		}
		
		public double getRtn() {
			return (getSellPrice() - buyPrice) / buyPrice; 
		}
		
		public double getPl() {
			return getReturnAmt() - investedAmt;
		}
		
		@Override
		public String toString() {
			return "MACDBackTestRow [quote=" + historicalQuote + ", macd=" + macd
					+ ", macdHist=" + macdHist + ", macdSignal=" + macdSignal
					+ ", previousMacd=" + previousMacd + ", previousMacdHist="
					+ previousMacdHist + ", previousMacdSignal="
					+ previousMacdSignal + ", buyPrice=" + buyPrice
					+ ", sellPrice=" + getSellPrice() + ", buyDate=" + buyDate
					+ ", sellDate=" + sellDate 
					+ ", investedAmt=" + investedAmt 
					+ ", returnAmt=" + getReturnAmt() 
					+ ", pl=" + getPl() 
					+ ", return=" + getRtn() + "]";
		}
	}

}
