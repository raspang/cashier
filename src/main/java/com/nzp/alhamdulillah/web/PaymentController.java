package com.nzp.alhamdulillah.web;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.nzp.alhamdulillah.entities.Item;
import com.nzp.alhamdulillah.entities.Payment;
import com.nzp.alhamdulillah.entities.PaymentItem;
import com.nzp.alhamdulillah.exception.ResourceNotFoundException;
import com.nzp.alhamdulillah.repositories.ItemRepository;
import com.nzp.alhamdulillah.repositories.PaymentRepository;
import com.nzp.alhamdulillah.services.ReportService;

import net.sf.jasperreports.engine.JRException;


@Controller
@RequestMapping("/payments")
@SessionAttributes("payment")
public class PaymentController
{

	@Autowired
	private PaymentRepository paymentRepository;


	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ReportService reportService;
	
	@GetMapping("/list")
	public String showPayments(HttpServletRequest request, Model model) {		
        int page = 0; 
        int size = 10; 
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        Double totalAmountToday = 0d;
        for(Payment p : paymentRepository.findByDateAndEnable(LocalDate.now(), true)) {
        	totalAmountToday += p.getTotalAmount();
        }
       
        model.addAttribute("totalAmountToday", totalAmountToday);
		model.addAttribute("payments", paymentRepository.findByEnableOrderByDateDescIdDesc(true, PageRequest.of(page, size)));	
		return "payment/payments";
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(@ModelAttribute("payment") Payment thePayment, 
			Model theModel) {
		
		Double total = 0.0;
		for(PaymentItem paymentItem : thePayment.getPaymentItems())
			total += paymentItem.getItemPrice();		
		thePayment.setTotalAmount(total);
		
		theModel.addAttribute("paymentItems", thePayment.getPaymentItems());
		theModel.addAttribute("payment", thePayment);
		  Double totalPI = 0d;
		  for(PaymentItem tempPI : thePayment.getPaymentItems()) {
			  totalPI +=  tempPI.getTotalAmount();
		  }
		
		theModel.addAttribute("totalAmount",totalPI);
		return "payment/payment-form";
	}
	

	@GetMapping("/showFormForAddItem")
	public String showFormForAddItem( @ModelAttribute("payment") Payment thePayment, HttpServletRequest request, Model theModel) {
		  Item item = itemRepository.findOneByCode(request.getParameter("code"));
		  String markAsWholeSale = request.getParameter("markAsWholeSale") != null ? request.getParameter("markAsWholeSale") : "";
		  
		  Integer pieces = 1;
		  try {
			  pieces = Integer.parseInt(request.getParameter("pieces"));
		  }catch(Exception e){ }
		  
		  if(item != null) {
			  PaymentItem paymentItem = new PaymentItem();
			  paymentItem.setItemId(item.getId());
			  paymentItem.setCode(item.getCode());
			  paymentItem.setItemName(item.getItemName());
			  paymentItem.setItemGram(item.getItemGram());
			  paymentItem.setItemColor(item.getItemColor());
			  if(pieces == 0)
				  pieces = 1;
			  paymentItem.setPieces(pieces);
			  if(item.getMinWholeSalePieces2() != null && pieces >= item.getMinWholeSalePieces2()) {
				  paymentItem.setItemPrice(item.getItemPrice());
				  paymentItem.setTotalAmount(pieces * item.getItemAddPriceRetail2());		  
			  }
			  else {
				  if(pieces >= item.getMinWholeSalePieces() || markAsWholeSale.equalsIgnoreCase("true")) { 
					paymentItem.setItemPrice(item.getItemPrice());
				  	paymentItem.setTotalAmount(pieces * item.getItemPrice());
				  }else{
					  paymentItem.setItemPrice(item.getItemPrice() + item.getItemAddPriceRetail());
					  paymentItem.setTotalAmount(pieces * ( item.getItemPrice() + item.getItemAddPriceRetail()));
				  }
			  }
				  
			  thePayment.add(paymentItem);
		  }

		  theModel.addAttribute("payment", thePayment);
		  
		return "redirect:/payments/showFormForAdd";
	}
	
	@GetMapping("/showFormForAddClear")
	public String showFormForAddClear( HttpServletRequest request, Model theModel) {	
		theModel.addAttribute("payment", new Payment());
		return "redirect:/payments/showFormForAdd";
	}
	
	@GetMapping("/showFormForRemoveItem")
	public String showFormForRemoveItem( @ModelAttribute("payment") Payment thePayment, HttpServletRequest request, Model theModel) {
		 
		  
		  List<PaymentItem> list = thePayment.getPaymentItems();
		 

		  for(PaymentItem item : thePayment.getPaymentItems()) {
			  if(item.getCode().equals(request.getParameter("code"))) {
				  list.remove(item);
				  break;
			  }
		  }
		  thePayment.setPaymentItems(list);
		  
		  theModel.addAttribute("payment", thePayment);
		  
		return "redirect:/payments/showFormForAdd";
	}
	@PostMapping("/save")
	public String savePayment(@Valid @ModelAttribute("payment") Payment thePayment, BindingResult bindingResult, Model theModel) {
		String success = "created";
		if(thePayment.getId() != null) {
			success = "updated";
		}	
		if(thePayment.getDate().isAfter(LocalDate.now())) {
			thePayment.setDate(LocalDate.now());
		}
		Double total = 0.0;
		for(PaymentItem paymentItem : thePayment.getPaymentItems())
			total += paymentItem.getItemPrice() * paymentItem.getPieces();
		thePayment.setTotalAmount(total);
		
		if(bindingResult.hasErrors()) {
			theModel.addAttribute("paymentItems", thePayment.getPaymentItems());			
			return "payment/payment-form";
		}
		thePayment.setIssuedBy(getUsername());
		paymentRepository.save(thePayment);
		theModel.addAttribute("payment", new Payment());

		return "redirect:/payments/list?success="+success;
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("paymentId") Long theId) {
		paymentRepository.deleteById(theId);	
		return "redirect:/payments/list";
	}
	
	@ModelAttribute("payment")
	public Payment getPayment() {
		return new Payment();	
	}
	
	/*
	 * @RequestMapping(value="/products") public @ResponseBody List<Item>
	 * getItems(Model model) { return (List<Item>)itemRepository.findAll(); }
	 * 
	 * @ModelAttribute("products") public List<Item> getProducts() { return
	 * (List<Item>)itemRepository.findAll(); }
	 */

	@GetMapping("/showReceipt")
	public String receipt( HttpServletResponse resp, HttpServletRequest request, Model theModel) throws JRException, IOException {	
		Payment payment = paymentRepository.findById(Long.parseLong(request.getParameter("id"))).orElseThrow(
				() -> new ResourceNotFoundException("Payment", "id",Long.parseLong(request.getParameter("id")) ));
		
		reportService.exportRecept(resp, payment);
		
		return null;
	}
	public String getUsername() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
		   return ((UserDetails)principal).getUsername();
		} else {
		  return principal.toString();
		}
	}
}
