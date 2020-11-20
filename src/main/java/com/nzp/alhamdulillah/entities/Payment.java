package com.nzp.alhamdulillah.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.nzp.alhamdulillah.utils.DateUtils;

@Entity
@Table(name="payment")
public class Payment {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull(message="is required")
	@DateTimeFormat (pattern="yyyy-MM-dd")
	@Column(name="payment_date")
	private LocalDate date;
	
	@Column(name= "total_amount")
	private Double totalAmount;

	private Boolean enable;
	
	@NotEmpty(message="is required")
	@OneToMany(mappedBy="payment", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private List<PaymentItem> paymentItems  = new ArrayList<>();
	
	@Column(name= "issued_by")
	private String issuedBy;
	
	@Transient
	private String dateStr;
	
	
	public Payment() {
		this.date = LocalDate.now();
		this.enable = true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate(){
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}


	public List<PaymentItem> getPaymentItems() {
		return paymentItems;
	}

	public void setPaymentItems(List<PaymentItem> paymentItems) {
		this.paymentItems = paymentItems;
	}

	public String getDateStr() {
		return DateUtils.displayDate(date);
	}


	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	
	public void add(PaymentItem paymentItem) {
		paymentItems.add(paymentItem);
		paymentItem.setPayment(this);
	}

	public String getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}


	
}
