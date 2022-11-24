package com.tin.ultil;

import java.awt.Color;
import java.io.IOException;
import java.text.DateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;


import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.tin.entity.Order;

public class OrderPDFExporter {
	private List<Order> findAll;

	public OrderPDFExporter(List<Order> findAll) {
		this.findAll = findAll;
	}
	
	private void writeTableHeader(PdfPTable table) {
		// style cho tiêu đề cột
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		font.setColor(Color.WHITE);
		
		// thêm cột && đặt tên cột
		cell.setPhrase(new Phrase("OrderID", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("AccountID", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("PaymentMethod", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("OrderDate", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("OrderStatus", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("TotalPrice", font));
		table.addCell(cell);
	}

	private void writeTableData(PdfPTable table) {
		for (Order order : findAll) {
			table.addCell(String.valueOf(order.getOrder_id()));
			table.addCell(String.valueOf(order.getAccount().getAccount_id()));
			table.addCell(order.getPayment_method().getName().toString());
			table.addCell(String.valueOf(order.getOrderdate()));
			table.addCell(String.valueOf(order.getOrderStatus()));
			table.addCell(Double.toString(order.getTotalPrice()));
		}
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();
		//chỉnh style cho tiêu đề 
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLACK);
        Paragraph p = new Paragraph("List of All Orders", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);
		// căn lề cho data 
		PdfPTable table = new PdfPTable(6);
		table.setHorizontalAlignment(Element.ALIGN_MIDDLE);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.6f, 2.0f, 3.0f, 3.5f, 2.2f, 2.0f });
		table.setSpacingBefore(10);
	
		writeTableHeader(table);
		writeTableData(table);

		document.add(table);

		document.close();

	}
}