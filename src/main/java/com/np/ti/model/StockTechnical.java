package com.np.ti.model;

import org.joda.time.LocalDate;

import com.np.ti.model.MACD.MACDCalcOutput;
import com.np.ti.model.SMA.SMACalcOutput;

public class StockTechnical {
	
	private LocalDate asofDate;
	private String symbol;
	
	private MACDCalcOutput macdOutput12269;
	
	private SMACalcOutput smaCalcOutput200;
	
	private SMACalcOutput smaCalcOutput50;

	private SMACalcOutput smaCalcOutput100;

}
