package com.np.ti.runner;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;

import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import com.google.common.collect.Lists;
import com.np.ti.excel.ExcelGenerator;
import com.np.ti.excel.ExcelScreenerRow;
import com.np.ti.excel.ExcelScreenerRowConvertor;
import com.np.ti.model.StockHelper;
import com.np.ti.model.MACD.MACDBackTest;
import com.np.ti.model.MACD.MACDCalcInput;
import com.np.ti.model.MACD.MACDCalcOutput;
import com.np.ti.model.MACD.MACDCalculator;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class tester {

	//http://www.heatonresearch.com/content/technical-analysis-library-ta-lib-tutorial-java
	  
	
	public static void main(String[] args) throws IOException {
		List<ExcelScreenerRow> mExcelRows = new ArrayList<ExcelScreenerRow>();
		File file = new File("/dev/excel/list.txt");
		List<String> symbols = FileUtils.readLines(file);
		for (String symbol : symbols) {
			run(symbol, mExcelRows);
		}
		ExcelGenerator generator = new ExcelGenerator(
				mExcelRows);
		generator.generate();
	}


	private static void run(String symbol, List<ExcelScreenerRow> mExcelRows) throws IOException {
		StockHelper stockHelper = new StockHelper(symbol);
		 
		Stock stock = stockHelper.getStock();
//		BigDecimal price = stock.getQuote().getPrice();
//		BigDecimal change = stock.getQuote().getChangeInPercent();
//		BigDecimal peg = stock.getStats().getPeg();
//		BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();
//		
		stock.print();
		
		LocalDate dateStart = new LocalDate(2005,1,1);

		LocalDate dateEnd = LocalDate.now();
		
		List<HistoricalQuote> quotes =  stockHelper.getHistory(dateStart, dateEnd, Interval.WEEKLY);
		
		
		/*for (HistoricalQuote historicalQuote : quotes) {
			System.out.println(historicalQuote.getAdjClose());
		}*/
		testMacd2(quotes, symbol, mExcelRows);
	}
	

	private static void testMacd2(List<HistoricalQuote> quotes, String symbol, List<ExcelScreenerRow> mExcelRows) {

		
		try {
			MACDCalcInput input = new MACDCalcInput();
			input.setFastPeriod(12);
			input.setSlowPeriod(26);
			input.setSignalPeriod(9);
			input.setQuotes(quotes);
			MACDCalculator calc = new MACDCalculator(input, symbol);
			List<MACDCalcOutput> output = Lists.newArrayList(calc.getMACD()
					.values());
			MACDBackTest test = new MACDBackTest(calc.getMACD());
			test.runRegressionSimpleCrossOver();
			
			ExcelScreenerRow row = ExcelScreenerRowConvertor.convert(
					output.get(output.size() - 1),
					output.get(output.size() - 2));
			mExcelRows.add(row);
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}


	private static void testMacd(List<HistoricalQuote> quotes) {
		int size = quotes.size();
		double[] closePrice = new double[size];
	      MInteger begin = new MInteger();
	      MInteger length = new MInteger();

	      for (int i = 0; i < size; i++) {
	        closePrice[i] = quotes.get(i).getAdjClose().doubleValue();
	      }

	      Core c = new Core();
	      //RetCode retCode = c.sma(0, closePrice.length - 1, closePrice, PERIODS_AVERAGE, begin, length, out);

	      //RetCode retCode = c.ema(0, closePrice.length - 1, closePrice, PERIODS_AVERAGE, begin, length, out);

	      double[] outMACD = new double[size];
	      double[] outMACDSignal = new double[size];
	      double[] outMACDHist = new double[size];
	      RetCode retCode = c.macd(0, closePrice.length - 1, closePrice, 12, 26, 9, begin, length, outMACD, outMACDSignal, outMACDHist);
	      
	      
	      
	      if (retCode == RetCode.Success) {
	        System.out.println("Output Begin:" + begin.value);
	        System.out.println("Output Begin:" + length.value);

	        for (int i = begin.value; i < length.value; i++) {
	          StringBuilder line = new StringBuilder();
	          line.append("Period #");
	          line.append(i+1);
	          line.append(" close= ");
	          line.append(closePrice[i]);
	          line.append(" macd=");
	          line.append(outMACD[i]);
	          line.append(" signal=");
	          line.append(outMACDSignal[i]);
	          line.append(" hist=");
	          line.append(outMACDHist[i]);
	          System.out.println(line.toString());
	      }
	    }
	    else {
	      System.out.println("Error");
	    }		
	}

}
