package com.np.ti.runner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;

import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import com.google.common.collect.Lists;
import com.np.ti.excel.ExcelScreenerRow;
import com.np.ti.excel.ExcelScreenerRowConvertor;
import com.np.ti.model.StockHelper;
import com.np.ti.model.MACD.MACDBackTest;
import com.np.ti.model.MACD.MACDCalcInput;
import com.np.ti.model.MACD.MACDCalcOutput;
import com.np.ti.model.MACD.MACDCalculator;

public class MACDBackTestRunner {
	
	private static final String SYMBOL = "NKE";
	List<ExcelScreenerRow> mExcelRows = new ArrayList<ExcelScreenerRow>();

	public static void main(String[] args) throws IOException {
		runSP500();
		//run one
		//runOne();

	}

	private static void runOne() throws IOException {
		List<ExcelScreenerRow> mExcelRows = new ArrayList<ExcelScreenerRow>();
		
		//run(SYMBOL, mExcelRows);
		
	}

	private static void runSP500() throws IOException {
		List<ExcelScreenerRow> mExcelRows = new ArrayList<ExcelScreenerRow>();
		File file = new File("/dev/excel/list.txt");
		List<String> symbols = FileUtils.readLines(file);
		for (String symbol : symbols) {
			StockHelper stockHelper = new StockHelper(symbol);
			
			LocalDate dateStart = new LocalDate(2005,1,1);
			LocalDate dateEnd = LocalDate.now();
			List<HistoricalQuote> quotes =  stockHelper.getHistory(dateStart, dateEnd, Interval.WEEKLY);
			run(symbol, quotes, mExcelRows , 12, 26, 9, "test12269");
			//run(symbol, quotes, mExcelRows , 12, 26, 26, "test122626");
			//run(symbol, quotes, mExcelRows , 8, 17, 9, "test8179");
		}
		//ExcelGenerator generator = new ExcelGenerator(
			//	mExcelRows);
		//generator.generate();
	}
	

	
	private static void run(String symbol, List<HistoricalQuote> quotes, List<ExcelScreenerRow> mExcelRows, 
			int fast, int slow, int signal, String testName) throws IOException {
		
		testMacd2(quotes, symbol, mExcelRows, fast, slow, signal, testName);
	}
	
	private static void testMacd2(List<HistoricalQuote> quotes, String symbol, List<ExcelScreenerRow> mExcelRows, 
			int fast, int slow, int signal, String testName) {

		
		try {
			MACDCalcInput input = new MACDCalcInput();
			input.setFastPeriod(fast);
			input.setSlowPeriod(slow);
			input.setSignalPeriod(signal);
			input.setQuotes(quotes);
			MACDCalculator calc = new MACDCalculator(input, symbol);
			List<MACDCalcOutput> output = Lists.newArrayList(calc.getMACD()
					.values());
			MACDBackTest test = new MACDBackTest(calc.getMACD(), testName);
			test.runRegressionSimpleCrossOver();
			
			ExcelScreenerRow row = ExcelScreenerRowConvertor.convert(
					output.get(output.size() - 1),
					output.get(output.size() - 2));
			mExcelRows.add(row);
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}
	
	/*
	public void generate() {
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Screener");
        
        int rowCount = 0;
        int columnCount = 0;
        
        ++rowCount;
        createHeaders(sheet);
        
        
        for (ExcelScreenerRow row : mRows) {
        	Row excelRow = sheet.createRow(++rowCount);
            
            columnCount = 0;
             
            Cell cell1 = excelRow.createCell(++columnCount);
            setDateFormat(workbook, excelRow, cell1);
            cell1.setCellValue(row.getAsofDate().toDateTimeAtStartOfDay().toDate());
            
            
            Cell cell2 = excelRow.createCell(++columnCount);
            cell2.setCellValue(row.getSymbol());
            
            Cell cell3 = excelRow.createCell(++columnCount);
            cell3.setCellValue(row.getMacdOutput().getMacd());
            
            Cell cell4 = excelRow.createCell(++columnCount);
            cell4.setCellValue(row.getPrevMacdOutput().getMacd());
            
            Cell cell5 = excelRow.createCell(++columnCount);
            cell5.setCellValue(row.getMacdOutput().getMacdSignal());
            
            Cell cell6 = excelRow.createCell(++columnCount);
            cell6.setCellValue(row.getPrevMacdOutput().getMacdSignal());
            
        }
        FileOutputStream outputStream = null;
        
        try {
        	outputStream = new FileOutputStream("/dev/excel/test.xlsx");
        	workbook.write(outputStream);
        	workbook.close();
        } catch (Exception e) {
        	throw new RuntimeException(e.toString());
		} 		
	}

	private void createHeaders(XSSFSheet sheet) {
		Row excelRowHeader = sheet.createRow(1);
		int columnCount = 0;
        Cell cell1Header = excelRowHeader.createCell(++columnCount);
        cell1Header.setCellValue("Date");
        
        Cell cell2Header = excelRowHeader.createCell(++columnCount);
        cell2Header.setCellValue("Symbol");
        
        Cell cell3Header = excelRowHeader.createCell(++columnCount);
        cell3Header.setCellValue("MACD");
        
        Cell cell4Header = excelRowHeader.createCell(++columnCount);
        cell4Header.setCellValue("Prev MACD");
        
        Cell cell5Header = excelRowHeader.createCell(++columnCount);
        cell5Header.setCellValue("Signal");
        
        Cell cell6Header = excelRowHeader.createCell(++columnCount);
        cell6Header.setCellValue("Prev Signal");
	}
	
	private void setDateFormat(Workbook wb, Row row, Cell cell) {
		CellStyle cellStyle = wb.createCellStyle();
		CreationHelper createHelper = wb.getCreationHelper();
		cellStyle.setDataFormat(
		    createHelper.createDataFormat().getFormat("yy-MM-dd"));
		cell = row.createCell(1);
		cell.setCellValue(new Date());
		cell.setCellStyle(cellStyle);
		
	}*/
}
