package com.nzp.alhamdulillah.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nzp.alhamdulillah.entities.Expense;


public interface ExpenseRepository extends JpaRepository<Expense, Long>
{

	List<Expense> findByDateAndEnable(LocalDate date, Boolean enable);
	
	Page<Expense> findByEnableOrderByDateDescIdDesc(Boolean enable, Pageable pageable);
	
	@Query("select e from Expense e where year(e.date) = ?1 and month(e.date) = ?2")
	List<Expense> getByYearAndMonth(int year, int month);
	
	List<Expense> findByDateOrderByIdAsc(LocalDate date);
	
	
}
