package com.nzp.alhamdulillah.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name="item")
public class Item  implements Comparable<Item>{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message="is required")
	@NotEmpty(message="is required")
	@Column(name="code", unique=true)
	private String code;
	
	
	@Column(name= "item_code")
	private String itemCode = "no-image";
	
	@NotBlank(message="is required")
	@NotEmpty(message="is required")
	@Column(name= "item_name")
	private String itemName;
	
	@Column(name= "item_gram")
	private String itemGram;
	
	@Column(name= "item_color")
	private String itemColor;

	@Column(name= "item_price")
	private Double itemPrice;
	
	@Column(name= "item_min_whole_sale_pieces")
	private Integer minWholeSalePieces;
	

	@Column(name= "item_addition_price_retail")
	private Double itemAddPriceRetail;
	
	
	@Column(name= "item_min_whole_sale_pieces_2")
	private Integer minWholeSalePieces2 = 12;
	
	
	@Column(name= "item_addition_price_retail_2")
	private Double itemAddPriceRetail2 = 70d;

	@Transient
	private Double retailPrice;
	
	private Integer quantity = 0;
	
	@Transient
	private Integer sold;
	

	
	@Transient
	private Integer totalQuantity = 0;
	
	public Item() {
		itemPrice = 180d;
		minWholeSalePieces = 3;
		itemAddPriceRetail = 50d;
		quantity=0;
	}
	

	public Item(String code, String itemCode, String itemName, String itemGram, String itemColor, Double itemPrice,
			Integer minWholeSalePieces, Double itemAddPriceRetail, Integer quantity) {
	
		this.code = code;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.itemGram = itemGram;
		this.itemColor = itemColor;
		this.itemPrice = itemPrice;
		
		if(minWholeSalePieces != null) {
			this.minWholeSalePieces = minWholeSalePieces;
		}else {
			this.minWholeSalePieces = 1;
		}
		
		if(itemAddPriceRetail != null)
			this.itemAddPriceRetail = itemAddPriceRetail;
		else
			this.itemAddPriceRetail = 0d;
		this.quantity = quantity;
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


	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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

	public Integer getMinWholeSalePieces() {
		return minWholeSalePieces;
	}

	public void setMinWholeSalePieces(Integer minWholeSalePieces) {
		this.minWholeSalePieces = minWholeSalePieces;
	}

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Double getItemAddPriceRetail() {
		return itemAddPriceRetail;
	}

	public void setItemAddPriceRetail(Double itemAddPriceRetail) {
		this.itemAddPriceRetail = itemAddPriceRetail;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void addQuantity(Integer quantity) {
		this.quantity = this.quantity + quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	
	public Integer getSold() {
		return sold;
	}

	public void setSold(Integer sold) {
		this.sold = sold;
	}

	
	public Integer getTotalQuantity() {
		return totalQuantity;
	}


	public void setTotalQuantity(Integer totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	
	


	public Double getRetailPrice() {
		return retailPrice;
	}


	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}

	

	public Integer getMinWholeSalePieces2() {
		return minWholeSalePieces2;
	}


	public void setMinWholeSalePieces2(Integer minWholeSalePieces2) {
		this.minWholeSalePieces2 = minWholeSalePieces2;
	}

	

	public Double getItemAddPriceRetail2() {
		return itemAddPriceRetail2;
	}


	public void setItemAddPriceRetail2(Double itemAddPriceRetail2) {
		this.itemAddPriceRetail2 = itemAddPriceRetail2;
	}


	@Override
	public String toString() {
		return "Item [id=" + id + ", code=" + code + ", itemName=" + itemName + ", itemGram=" + itemGram
				+ ", itemColor=" + itemColor + ", itemPrice=" + itemPrice + ", minWholeSalePieces=" + minWholeSalePieces
				+ ", itemAddPriceRetail=" + itemAddPriceRetail + "]";
	}


	@Override
	public int compareTo(Item item1) {
		return item1.sold.compareTo(this.sold);
	}

	
}

