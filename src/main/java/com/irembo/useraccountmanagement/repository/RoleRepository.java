package com.irembo.useraccountmanagement.repository;

import java.util.Optional;

import com.irembo.useraccountmanagement.models.ERole;
import com.irembo.useraccountmanagement.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}

