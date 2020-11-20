package com.nzp.alhamdulillah.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nzp.alhamdulillah.entities.Role;



public interface RoleRepository extends JpaRepository<Role, Long>{

	public Role findByName(String name);
}
