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
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);

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
		
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setSize(18);
        font.setColor(Color.BLUE);
        Paragraph p = new Paragraph("List of Orders", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);

		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.6f, 2.0f, 3.0f, 3.5f, 2.2f, 2.0f });
		table.setSpacingBefore(10);

		writeTableHeader(table);
		writeTableData(table);

		document.add(table);

		document.close();

	}
}