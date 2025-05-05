package com.shann.ecom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shann.ecom.models.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {}
