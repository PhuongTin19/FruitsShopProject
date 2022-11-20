package com.tin.ultil;

import java.io.IOException;
import java.util.List;
 
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tin.entity.Order;
 
public class OrderExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Order> findAll;
     
    public OrderExcelExporter(List<Order> findAll) {
        this.findAll = findAll;
        workbook = new XSSFWorkbook();
    }
 
 
    private void writeHeaderLine() {
        sheet = workbook.createSheet("ListOfOrder");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "OrderID", style);      
        createCell(row, 1, "AccountID", style);       
        createCell(row, 2, "PaymentMethod", style);    
        createCell(row, 3, "OrderDate", style);
        createCell(row, 4, "OrderStatus", style);
        createCell(row, 5, "TotalPrice", style);
    }
     
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (Order order : findAll) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, order.getOrder_id(), style);
            createCell(row, columnCount++, order.getAccount().getAccount_id(), style);
            createCell(row, columnCount++, order.getPayment_method().getName().toString(), style);
            createCell(row, columnCount++, String.valueOf(order.getOrderdate()), style);
            createCell(row, columnCount++, order.getOrderStatus(), style);
            createCell(row, columnCount++, order.getTotalPrice(), style);
        }
    }
     
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
}