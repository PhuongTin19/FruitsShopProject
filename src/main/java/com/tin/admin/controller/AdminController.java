package com.tin.admin.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lowagie.text.DocumentException;
import com.tin.entity.Discount;
import com.tin.entity.Order;
import com.tin.entity.Report;
import com.tin.repository.ProductRepo;
import com.tin.service.DiscountService;
import com.tin.service.OrderService;
import com.tin.ultil.OrderExcelExporter;
import com.tin.ultil.OrderPDFExporter;

@Controller
public class AdminController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private DiscountService discountService;
	
	@Autowired
	private ProductRepo productRepo;
	
	// export data to excel
		@GetMapping("/admin-order/export/excel")
	    public void exportToExcel(HttpServletResponse response) throws IOException {
	        response.setContentType("application/octet-stream");
	        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	        String currentDateTime = dateFormatter.format(new Date(0));
	         
	        String headerKey = "Content-Disposition";
	        String headerValue = "attachment; filename=ListOfOrder_" + currentDateTime + ".xlsx";
	        response.setHeader(headerKey, headerValue);
	         
	        List<Order> findAll = orderService.findAll();
	         
	        OrderExcelExporter excelExporter = new OrderExcelExporter(findAll);
	         
	        excelExporter.export(response);    
	    }  
	 
		// export data to pdf
		@GetMapping("/admin-order/export/pdf")
		public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
			response.setContentType("application/pdf");
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String currentDateTime = dateFormatter.format(new Date(0));

			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=ListOfOrder_" + currentDateTime + ".pdf";
			response.setHeader(headerKey, headerValue);

			List<Order> findAll = orderService.findAll();

			OrderPDFExporter exporter = new OrderPDFExporter(findAll);
			exporter.export(response);
		}
		
	@RequestMapping({"/admin","/admin/home/index"})
	public String admin(Model model,HttpServletRequest request) { 
		//Biểu đồ đường doanh thu theo ngày
		String day1 = request.getParameter("day");
	    String end1 = request.getParameter("end");
	    model.addAttribute("day",day1);
	    model.addAttribute("end",end1);
		String[][] chartData;
		if(ObjectUtils.isEmpty(day1) && ObjectUtils.isEmpty(end1)) {
			chartData = orderService.getTotalPriceLast6Months();
			String text = "Thống kê doanh thu 6 tháng";
			model.addAttribute("text",text);
			model.addAttribute("chartData",chartData);
		}
		else{
			chartData = orderService.getTotalPriceFromTo(day1, end1);
			String text = "Thống kê doanh thu từ ngày " + day1+" đến ngày "+end1 ;
			model.addAttribute("text",text);
			model.addAttribute("chartData",chartData);
		}
		//Biểu đồ cột doanh thu theo loại hàng
		String dayCates = request.getParameter("dayCates");
	    String endCates = request.getParameter("endCates");
	    model.addAttribute("dayCates",dayCates);
	    model.addAttribute("endCates",endCates);
		String[][]statsRevenueProductsByCates ;
		if(ObjectUtils.isEmpty(dayCates) && ObjectUtils.isEmpty(endCates)) {
			statsRevenueProductsByCates = orderService.statsRevenueProductsByCates();
			model.addAttribute("statsRevenueProductsByCates",statsRevenueProductsByCates);
		}
		else{
			statsRevenueProductsByCates = orderService.getProductsByCatesFromTo(dayCates, endCates);
			model.addAttribute("statsRevenueProductsByCates",statsRevenueProductsByCates);
		}
		//Tình trạng đơn hàng
		String[][]statsOrderStatus = orderService.statsOrderStatus();
		model.addAttribute("statsOrderStatus", statsOrderStatus);
		//disable tự động discount hết hạn
		try {
			disableDiscountAuto();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "/admin/index";
	}
	
	
	@RequestMapping({"/admin/inventory/{cName}"})
	public String adminReportDetail(Model model,HttpServletRequest request,@PathVariable("cName") String categoryName) { 
		List<Report>inventoryDetail = productRepo.getInventoryByCategoryDetail(categoryName);
		model.addAttribute("inventoryDetail",inventoryDetail);
		return "/admin/index2";
	}
	
	public void disableDiscountAuto() {
		try {
			long millis=System.currentTimeMillis();   
			List<Discount>discounts = discountService.findAll();
			java.sql.Date date=new java.sql.Date(millis); 
			TimerTask timerTask = new TimerTask() {
	            @Override
	            public void run() {
	            	for (int i = 0; i < discounts.size(); i++) {
	            		//ngày kết thúc = ngày hiện tại thì disable
	            		if(discounts.get(i).getEnd_time().before(date)) {
	                		discountService.deleteLogical(discounts.get(i).getDiscount_id());
	                	}else if(discounts.get(i).getStart_time().after(date)) {
	                		discountService.deleteLogical(discounts.get(i).getDiscount_id());
	                	}else if(discounts.get(i).getStart_time().before(date)) {
	                		discountService.updateLogical(discounts.get(i).getDiscount_id());
	                	}
					} 
	            }
	        };
	        long delay = 100000L;
	        Timer timer = new Timer("Timer");
	        timer.schedule(timerTask, 0, delay);
		} catch (Exception e) {
			System.out.println("");
		} 
	}
}