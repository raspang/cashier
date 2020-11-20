package com.nzp.alhamdulillah.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="payment_item")
public class PaymentItem {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String code;
	
	@Column(name= "item_id")
	private Long itemId;
	
	@Column(name= "item_name")
	private String itemName;
	
	@Column(name= "item_gram")
	private String itemGram;
	
	@Column(name= "item_color")
	private String itemColor;

	@Column(name= "item_price")
	private Double itemPrice;
	
	private Integer pieces;
	
	@Column(name= "total_amount")
	private Double totalAmount;
	
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="payment_id")
	private Payment payment;
	
	
	public PaymentItem() {
		this.pieces = 1;
	}

	public PaymentItem(String code, String itemName, String itemGram, 
			String itemColor, Double itemPrice) {
		this.code = code;
		this.itemName = itemName;
		this.itemGram = itemGram;
		this.itemColor = itemColor;
		this.itemPrice = itemPrice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemGram() {
		return itemGram;
	}

	public void setItemGram(String itemGram) {
		this.itemGram = itemGram;
	}

	public String getItemColor() {
		return itemColor;
	}

	public void setItemColor(String itemColor) {
		this.itemColor = itemColor;
	}

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Integer getPieces() {
		return pieces;
	}

	public void setPieces(Integer pieces) {
		this.pieces = pieces;
	}
	

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	
	
}


	
	
	