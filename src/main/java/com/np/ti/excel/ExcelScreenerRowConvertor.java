package com.np.ti.excel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.LocalDate;

import com.np.ti.model.MACD.MACDCalcOutput;

public class ExcelScreenerRowConvertor {
	public static ExcelScreenerRow convert(MACDCalcOutput current, MACDCalcOutput previous) {
		
		//sort(output);
		
		ExcelScreenerRow row = new ExcelScreenerRow();
		row.setAsofDate(current.getAsofDate());
		row.setSymbol(current.getSymbol());
		row.setMacdOutput(current);
		row.setPrevMacdOutput(previous);
		
		return row;
		
	}

	private static void sort(List<MACDCalcOutput> output) {
		Collections.sort(output, new Comparator<MACDCalcOutput>() {

			public int compare(MACDCalcOutput a, MACDCalcOutput b) {
				if (a == null || b == null) {
					return 0;
				}
				if (a.getAsofDate().isAfter(b.getAsofDate())) {
					return 1;
				} 
				if (a.getAsofDate().isBefore(b.getAsofDate())) {
					return -1;
				} 
				return 0;
				
			}
			
		});
	}
	
	
}
