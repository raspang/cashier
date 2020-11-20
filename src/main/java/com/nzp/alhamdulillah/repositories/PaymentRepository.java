package com.nzp.alhamdulillah.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nzp.alhamdulillah.entities.Payment;


public interface PaymentRepository extends JpaRepository<Payment, Long>{
	
	/*
	 * Page<Payment> findByCodeOrderByCodeAscItemNameDesc(String code, Pageable
	 * pageable);
	 * 
	 * Page<Payment>
	 * findByItemNameContainsOrderByItemNameDescItemColorDescItemGramDesc(String
	 * itemName, Pageable pageable);
	 * 
	 * 
	 */
	Page<Payment> findByEnableOrderByDateDesc(Boolean enable, Pageable pageable);
	
	Page<Payment> findByEnableOrderByDateDescIdDesc(Boolean enable, Pageable pageable);
	
	List<Payment> findByDateAndEnable(LocalDate date, Boolean enable);
	
	@Query("select e from Payment e where year(e.date) = ?1 and month(e.date) = ?2")
	List<Payment> getByYearAndMonth(int year, int month);
	
	List<Payment> findByDateOrderByIdAsc(LocalDate date);
	

	
}
	