package com.nzp.alhamdulillah.web;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nzp.alhamdulillah.entities.Expense;
import com.nzp.alhamdulillah.entities.Item;
import com.nzp.alhamdulillah.entities.Payment;
import com.nzp.alhamdulillah.entities.PaymentItem;
import com.nzp.alhamdulillah.repositories.ExpenseRepository;
import com.nzp.alhamdulillah.repositories.ItemRepository;
import com.nzp.alhamdulillah.repositories.PaymentItemRepository;
import com.nzp.alhamdulillah.repositories.PaymentRepository;
import com.nzp.alhamdulillah.services.ReportService;
import net.sf.jasperreports.engine.JRException;


@Controller
@RequestMapping("/report")
public class ReportController{

	@Autowired
	private ReportService reportService;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private PaymentItemRepository paymentItemRepository;
	
	@Autowired
	private ExpenseRepository expenseRepository;
	
	@GetMapping("/list")
	public String showReport(HttpServletRequest request, Model theModel) {
		return "report/reports";
	}
	
	@GetMapping("/products")
	public String inventory(HttpServletResponse resp, HttpServletRequest request, Model theModel) throws JRException, IOException {
	
		List<Item> newItems = new ArrayList<>();
		for(Item item : itemRepository.findAll(Sort.by(Order.asc("itemName")))) {
			Item newItem = new Item( item.getCode(),  item.getItemCode(),  item.getItemName(),  item.getItemGram(),  item.getItemColor(), 
					item.getItemPrice(),
					item.getMinWholeSalePieces(),  item.getItemAddPriceRetail(),  item.getQuantity());
			newItem.setRetailPrice(newItem.getItemPrice() + newItem.getItemAddPriceRetail());
			Integer sold = 0;
			for(PaymentItem paymentItem : paymentItemRepository.findByItemId(item.getId())) {
				sold += paymentItem.getPieces();
			}
			newItem.setSold(sold);
			if(item.getQuantity() != null && item.getQuantity() != 0) {
				newItem.setTotalQuantity(item.getQuantity() - sold);
			}
			
			newItems.add(newItem);
		}
		Collections.sort(newItems);
		
		reportService.exportReportInventory(resp, newItems);
		return null;
	}

	
	@GetMapping("/payment")
	public String payments(HttpServletResponse resp, HttpServletRequest request, Model theModel) throws JRException, IOException, ParseException {
		String myDate = request.getParameter("date");
	
		LocalDate date = LocalDate.parse(myDate);
	
		List<Payment> payments = paymentRepository.findByDateOrderByIdAsc(date);
		reportService.exportReportPayment(resp, payments, myDate);
		
		return null;
	}
	
	@GetMapping("/expense")
	public String expenses(HttpServletResponse resp, HttpServletRequest request, Model theModel) throws JRException, IOException, ParseException {
		String myDate = request.getParameter("date");
	
		LocalDate date = LocalDate.parse(myDate);
	
		List<Expense> expenses = expenseRepository.findByDateOrderByIdAsc(date);
		reportService.exportReportExpense(resp, expenses, myDate);
		
		return null;
	}

}
