package com.stan.customer.repository;

import com.stan.customer.entity.Address;
import com.stan.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByCustomer(Customer customer);
}
