package com.np.ti.model.MACD;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.quotes.stock.StockQuote;

public class MACDCalculator {
	
	private MACDCalcInput input;
	double[] closePrice;
	double[] outMACD;
	double[] outMACDSignal;
	double[] outMACDHist;
	String mSymbol;
	
	MInteger begin = new MInteger();
    MInteger length = new MInteger();
    
    Map<LocalDate, MACDCalcOutput> mapOutput = new TreeMap<LocalDate, MACDCalcOutput>();
    
    RetCode retCode;
	
	public MACDCalculator(MACDCalcInput input, String symbol) {
		mSymbol = symbol;
		this.input = input;
		int size = input.getQuotes().size();
		List<HistoricalQuote> quotes = input.getQuotes();
		closePrice = new double[size];
		for (int i = 0; i < size; i++) {
	        closePrice[i] = quotes.get(i).getAdjClose().doubleValue();
	    }
		
		Core c = new Core();

		int outArraySize = size - (input.getSlowPeriod() - 1) - (input.getSignalPeriod() - 1);
		if(outArraySize < 0) {
			throw new RuntimeException("Not enough data");
		}
		outMACD = new double[outArraySize];
		outMACDSignal = new double[outArraySize];
		outMACDHist = new double[outArraySize];
		retCode = c.macd(0, closePrice.length - 1, closePrice,
				input.getFastPeriod(), input.getSlowPeriod(),
				input.getSignalPeriod(), begin, length, outMACD,
				outMACDSignal, outMACDHist);
		
		List<HistoricalQuote> trimmedQuotes = Lists.reverse(FluentIterable.from(Lists.reverse(quotes)).limit(outArraySize).toList());
		
		
		for (int i = 0; i < trimmedQuotes.size(); i++) {
			MACDCalcOutput output = new MACDCalcOutput();
			HistoricalQuote historicalQuote = trimmedQuotes.get(i);
			Calendar cal = historicalQuote.getDate();
			TimeZone tz = cal.getTimeZone();
			DateTimeZone jodaTz = DateTimeZone.forID(tz.getID());
			DateTime dateTime = new DateTime(cal.getTimeInMillis(),jodaTz);
			LocalDate date = dateTime.toLocalDate();
			//LocalDate date = new LocalDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
			output.setAsofDate(date);
			output.setMacd(outMACD[i]);
			output.setSymbol(mSymbol);
			output.setMacdSignal(outMACDSignal[i]);
			output.setMacdHist(outMACDHist[i]);
			output.setHistoricalQuote(historicalQuote);
			output.setQuote(input.getQuote());
			mapOutput.put(date, output);
		}
	}
	
	public Map<LocalDate, MACDCalcOutput> getMACD() {
		return mapOutput;
	}
}
