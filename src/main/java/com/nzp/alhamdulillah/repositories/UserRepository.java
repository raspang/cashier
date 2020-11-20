package com.nzp.alhamdulillah.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nzp.alhamdulillah.entities.User;



public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String email);
	


}
