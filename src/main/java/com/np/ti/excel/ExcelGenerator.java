package com.np.ti.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelGenerator {

	private List<ExcelScreenerRow> mRows = new ArrayList<ExcelScreenerRow>();

	public ExcelGenerator(List<ExcelScreenerRow> rows) {
		mRows = rows;
	}

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
		
	}
	
}
