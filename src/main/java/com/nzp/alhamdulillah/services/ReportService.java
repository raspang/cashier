package com.nzp.alhamdulillah.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.nzp.alhamdulillah.entities.Expense;
import com.nzp.alhamdulillah.entities.ExpenseDetail;
import com.nzp.alhamdulillah.entities.Item;
import com.nzp.alhamdulillah.entities.Payment;
import com.nzp.alhamdulillah.entities.PaymentItem;
import com.nzp.alhamdulillah.exception.ResourceNotFoundException;
import com.nzp.alhamdulillah.repositories.ItemRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportService {
	
	
	@Autowired 
	private ItemRepository itemReposity;

	public String exportRecept(HttpServletResponse resp, Payment payment) throws JRException, IOException {

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("totalAmount", payment.getTotalAmount());
		parameters.put("dateStr", payment.getDateStr());
		parameters.put("serialNo", "P000"+payment.getId());
        File file = ResourceUtils.getFile("classpath:receipt.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(payment.getPaymentItems());
       
       
        
        byte[] bytes = null;
        bytes = JasperRunManager.runReportToPdf(jasperReport, parameters, dataSource);
        	
    	resp.reset();
    	resp.resetBuffer();
    	resp.setContentType("application/pdf");
    	resp.setContentLength(bytes.length);
    	ServletOutputStream ouputStream = resp.getOutputStream();
    	ouputStream.write(bytes, 0, bytes.length);
    	ouputStream.flush();
    	ouputStream.close();
    	
    	return null;
	}

	public String exportReportInventory(HttpServletResponse resp, List<Item> items) throws JRException, IOException {

		Map<String, Object> parameters = new HashMap<>();
        File file = ResourceUtils.getFile("classpath:list_of_items.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);
       
       
        
        byte[] bytes = null;
        bytes = JasperRunManager.runReportToPdf(jasperReport, parameters, dataSource);
        	
    	resp.reset();
    	resp.resetBuffer();
    	resp.setContentType("application/pdf");
    	resp.setContentLength(bytes.length);
    	ServletOutputStream ouputStream = resp.getOutputStream();
    	ouputStream.write(bytes, 0, bytes.length);
    	ouputStream.flush();
    	ouputStream.close();
    	
    	return null;
	}
	public String exportReportPayment(HttpServletResponse resp, List<Payment> payments, String date) throws JRException, IOException {
		List<PaymentItem> paymentItems = new ArrayList<>();
		Double totalAmount = 0d;
		
		for(Payment p : payments) {
			
			for(PaymentItem pItem : p.getPaymentItems()) {
				paymentItems.add(pItem);
				totalAmount += pItem.getTotalAmount();
			}
		
		}
		
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("totalAmount", totalAmount);
		parameters.put("date", date);
		
        File file = ResourceUtils.getFile("classpath:payment.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(paymentItems);
       
        
        byte[] bytes = null;
        bytes = JasperRunManager.runReportToPdf(jasperReport, parameters, dataSource);
        	
    	resp.reset();
    	resp.resetBuffer();
    	resp.setContentType("application/pdf");
    	resp.setContentLength(bytes.length);
    	ServletOutputStream ouputStream = resp.getOutputStream();
    	ouputStream.write(bytes, 0, bytes.length);
    	ouputStream.flush();
    	ouputStream.close();
    	
    	return null;
	}
	
	public String exportReportExpense(HttpServletResponse resp, List<Expense> expenses, String date) throws JRException, IOException {
		List<ExpenseDetail> expenseDetails = new ArrayList<>();
		Double totalAmount = 0d;
		
		for(Expense e : expenses) {
			
			for(ExpenseDetail eDetail : e.getExpenseDetails()) {
				expenseDetails.add(eDetail);
				totalAmount += eDetail.getAmount();
			}
		
		}
		
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("totalAmount", totalAmount);
		parameters.put("date", date);
		
        File file = ResourceUtils.getFile("classpath:expense.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(expenseDetails);
       
        
        byte[] bytes = null;
        bytes = JasperRunManager.runReportToPdf(jasperReport, parameters, dataSource);
        	
    	resp.reset();
    	resp.resetBuffer();
    	resp.setContentType("application/pdf");
    	resp.setContentLength(bytes.length);
    	ServletOutputStream ouputStream = resp.getOutputStream();
    	ouputStream.write(bytes, 0, bytes.length);
    	ouputStream.flush();
    	ouputStream.close();
    	
    	return null;
	}
	
	public String exportBarcode(HttpServletResponse resp, Integer num, Long id) throws JRException, IOException {

		List<Item> items = new ArrayList<>();
		
		Item item = itemReposity.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
		
		for(int i = 0; i < num; i++) {
			items.add(item);
		}
		

		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("name", item.getItemName());
		parameters.put("code", item.getCode());

        File file = ResourceUtils.getFile("classpath:barcode.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);
       
        
        byte[] bytes = null;
        bytes = JasperRunManager.runReportToPdf(jasperReport, parameters, dataSource);
        	
    	resp.reset();
    	resp.resetBuffer();
    	resp.setContentType("application/pdf");
    	resp.setContentLength(bytes.length);
    	ServletOutputStream ouputStream = resp.getOutputStream();
    	ouputStream.write(bytes, 0, bytes.length);
    	ouputStream.flush();
    	ouputStream.close();
    	
    	item.addQuantity(num);
    	itemReposity.save(item);
    	
    	return null;
	}
	
	
}
