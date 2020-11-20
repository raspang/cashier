package com.nzp.alhamdulillah.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nzp.alhamdulillah.entities.ExpenseDescription;



public interface ExpenseDescriptionRepository extends JpaRepository<ExpenseDescription, Long>
{
	
	List<ExpenseDescription> findByEnable(Boolean enable);
	

}
