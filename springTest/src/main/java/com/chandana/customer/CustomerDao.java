package com.chandana.customer;

import java.util.List;
import java.util.Optional;


// DAO Layer
public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);
    void insertCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
    boolean existsPersonWithId(Integer id);
    void deleteCustomerById(Integer customerId); 
    void updateCustomer(Customer customer); 
}
