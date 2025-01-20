package com.chandana.customer;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository 
        extends JpaRepository<Customer,Integer> {
        boolean existsCustomerByEmail(String email); // created our method to check data availabe in the repository by using passing value by us.
        boolean existsCustomerById(Integer id);
}
