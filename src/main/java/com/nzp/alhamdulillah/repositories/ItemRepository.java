package com.nzp.alhamdulillah.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nzp.alhamdulillah.entities.Item;


public interface ItemRepository extends JpaRepository<Item, Long>{
	
	Page<Item> findByCodeOrderByCodeAscItemNameDesc(String code, Pageable pageable);
	
	Page<Item> findByItemNameContainsOrderByItemNameDescItemColorDescItemGramDesc(String itemName, Pageable pageable);
	
	List<Item> findAllOrderByItemPrice(Double itemPrice);
	
	Item findOneByCode(String code);

	
}
	