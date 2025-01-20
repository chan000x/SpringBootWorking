package com.chandana.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;


// DataBase Connection for Data Retrieve

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {

     // db for now
    private static List<Customer> customers;
    static{
        customers = new ArrayList<>();
        Customer alex = new Customer(
            1,
            "Alex",
            "Alex@gmail.com",
            21
        );
        Customer jamila = new Customer(
            2,
            "Jamila",
            "Jamila@gmail.com",
            19
        );
        customers.add(alex);
        customers.add(jamila);
    }


    @Override
    public List<Customer> selectAllCustomers() {
      return customers;
 }


    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return  customers.stream()
        .filter(c -> c.getId().equals(id))
        .findFirst();
        
    }


    @Override
    public void insertCustomer(Customer customer) {
     
         customers.add(customer);
    }


    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream().anyMatch(c -> c.getEmail().equals(email));
    }


    @Override
    public boolean existsPersonWithId(Integer id) {
       return customers.stream()
              .anyMatch(c -> c.getId().equals(id));
    }


    @Override
    public void deleteCustomerById(Integer customerId) {
        customers.stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst()
                .ifPresent(customers::remove); 
        // This is the same thing as below code
        /* 
        customers.stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst()
                .ifPresent(c -> customers.remove(c));
                */
    }


    @Override
    public void updateCustomer(Customer customer) {
        // Integer customerId = customer.getId();
        // customers.stream()
        // .filter(c -> c.getId().equals(customerId))
        // .findFirst()
        // .ifPresent(           
        //      existingCustomer -> {
        //     // Update the existing customer's fields
        //     if (customer.getName() != null) {
        //         existingCustomer.setName(customer.getName());
        //     }
        //     if (customer.getEmail() != null) {
        //         existingCustomer.setEmail(customer.getEmail());
        //     }
        //     if (customer.getAge() != null) {
        //         existingCustomer.setAge(customer.getAge());
        //     }
        // });
        // or below method do the same thing like this
        customers.add(customer);
    }

}
