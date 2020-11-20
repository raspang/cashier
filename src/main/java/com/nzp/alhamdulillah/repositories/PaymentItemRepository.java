package com.nzp.alhamdulillah.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nzp.alhamdulillah.entities.PaymentItem;


public interface PaymentItemRepository extends JpaRepository<PaymentItem, Long>
{
	PaymentItem findOneByCode(String code);
	
	List<PaymentItem> findByItemId(Long itemId);
}
