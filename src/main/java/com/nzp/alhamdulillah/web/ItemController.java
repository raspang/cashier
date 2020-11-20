package com.nzp.alhamdulillah.web;

import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nzp.alhamdulillah.entities.Item;
import com.nzp.alhamdulillah.repositories.ItemRepository;
import com.nzp.alhamdulillah.services.ReportService;

import net.sf.jasperreports.engine.JRException;


@Controller
@RequestMapping("/items")
public class ItemController{

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ReportService reportService;
	
	@GetMapping("/list")
	public String showItems(HttpServletRequest request,Model theModel) {
		
		int page = 0; 
        int size = 15; 
        Pageable sortedBy;
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) 
            page = Integer.parseInt(request.getParameter("page")) - 1;
        

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) 
            size = Integer.parseInt(request.getParameter("size"));
           

        if (request.getParameter("code") != null && !request.getParameter("code").isEmpty())  {
        	sortedBy  =  PageRequest.of(page, size, Sort.by("code"));
        	theModel.addAttribute("items", itemRepository.findByCodeOrderByCodeAscItemNameDesc(request.getParameter("code"), sortedBy));
        	theModel.addAttribute("code", request.getParameter("code"));
        	theModel.addAttribute("itemName", "");
        } 
        else if (request.getParameter("itemName") != null && !request.getParameter("itemName").isEmpty())  {
        	 sortedBy  =  PageRequest.of(page, size, Sort.by("itemName"));
        	theModel.addAttribute("items", itemRepository.findByItemNameContainsOrderByItemNameDescItemColorDescItemGramDesc(request.getParameter("itemName"), sortedBy));
        	theModel.addAttribute("code", "");
        	theModel.addAttribute("itemName", request.getParameter("itemName"));
        }else{
        	 sortedBy  =  PageRequest.of(page, size, Sort.by("itemName"));
        	theModel.addAttribute("items", itemRepository.findAll(sortedBy));
		
        }
        theModel.addAttribute("itemName", request.getParameter("itemName"));
        theModel.addAttribute("code", request.getParameter("code"));
		return "item/items";
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		Item item = new Item();
		theModel.addAttribute("item", item);
		return "item/item-form";
	}
	
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("itemId") Long theId, Model theModel) {
		Optional<Item> item = itemRepository.findById(theId);
		theModel.addAttribute("item", item);
		return "item/item-form";	
	}
	
	@PostMapping("/save")
	public String saveItem(@Valid @ModelAttribute("item") Item theItem,
			BindingResult bindingResult) {
		
		if(bindingResult.hasErrors())
			return "item/item-form";

		String success = "created";
		if(theItem.getId() != null) {
			success = "updated";
		}
		
		itemRepository.save(theItem);
		return "redirect:/items/list?success="+success;
	} 
	
	@GetMapping("/delete")
	public String delete(@RequestParam("itemId") Long theId) {
		itemRepository.deleteById(theId);
		return "redirect:/items/list";
	}
	
	@GetMapping("/barcode")
	public String barcode(HttpServletResponse resp, HttpServletRequest request, Model theModel) throws JRException, IOException, ParseException {
		Long id = Long.parseLong(request.getParameter("id"));
		Integer num = Integer.parseInt(request.getParameter("num"));
	
		reportService.exportBarcode(resp, num, id);
		
		return null;
	}

		
}
