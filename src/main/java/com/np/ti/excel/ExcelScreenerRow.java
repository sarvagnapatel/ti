package com.np.ti.excel;

import org.joda.time.LocalDate;

import com.np.ti.model.MACD.MACDCalcOutput;

public class ExcelScreenerRow {

	private String symbol;
	private LocalDate asofDate;
	
	private MACDCalcOutput macdOutput;
	private MACDCalcOutput prevMacdOutput;
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public LocalDate getAsofDate() {
		return asofDate;
	}
	public void setAsofDate(LocalDate asofDate) {
		this.asofDate = asofDate;
	}
	public MACDCalcOutput getMacdOutput() {
		return macdOutput;
	}
	public void setMacdOutput(MACDCalcOutput macdOutput) {
		this.macdOutput = macdOutput;
	}
	public MACDCalcOutput getPrevMacdOutput() {
		return prevMacdOutput;
	}
	public void setPrevMacdOutput(MACDCalcOutput prevMacdOutput) {
		this.prevMacdOutput = prevMacdOutput;
	}
	
	
	
}
