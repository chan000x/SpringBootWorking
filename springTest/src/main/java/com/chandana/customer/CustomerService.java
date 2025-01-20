package com.chandana.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.chandana.exception.ChangeNotFoundException;
import com.chandana.exception.DuplicateResourceException;
import com.chandana.exception.ResourceNotFoundException;
import com.chandana.exception.UserNotFoundException;


// Business Layer

  @Service
public class CustomerService {

    private final CustomerDao customerDao;

    
   // @Autowired // this anotation used to dependancy inject in previous springboot version and no longer need it.
    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) { // instead of jpa we used jdbc driver for sql commands.
        this.customerDao = customerDao;
    }


    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id){
        return customerDao.selectCustomerById(id)
        .orElseThrow(
            ()-> new ResourceNotFoundException("customer with id [%s] not found ".formatted(id)
            ));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
       // check if email exists
       String email = customerRegistrationRequest.email();
       if(customerDao.existsPersonWithEmail(email)){
        throw new DuplicateResourceException("email already taken : ");
       }

       // add 
       Customer customer = new Customer(
            customerRegistrationRequest.name(),
            customerRegistrationRequest.email(),
            customerRegistrationRequest.age()
       );
       customerDao.insertCustomer(customer);
    }

    public void deleteCustomerById(Integer customerId){
        if(!customerDao.existsPersonWithId(customerId)){
            throw new ResourceNotFoundException(
                "customer with id [%s] not found".formatted(customerId)
            );
        }
        customerDao.deleteCustomerById(customerId);
    }

    public void editCustomer(Integer customerId, 
                CustomerEditRequest request) {
        // Fetch the customer from the database
        Customer customer = customerDao.selectCustomerById(customerId)
            .orElseThrow(() -> new UserNotFoundException(
                "Customer id [%s] not found".formatted(customerId)
            ));
    
        boolean change = false;
        // Update only the fields that are non-null
        if (request.name() != null && !request.name().equals(customer.getName())) {
            customer.setName(request.name());
            change = true;
        }
        if (request.email() != null && !request.email().equals(customer.getEmail())) {
            if(customerDao.existsPersonWithEmail(request.email())){
                throw new DuplicateResourceException(
                    "Email already taken"
                );
            }
            customer.setEmail(request.email());
            change = true;
        }
        if (request.age() != null && !request.age().equals(customer.getAge())) {
            customer.setAge(request.age());
            change = true;
        }
    
        // Save the updated customer back to the database
        if (change) { 
            customerDao.updateCustomer(customer);
        } else {
            throw new ChangeNotFoundException(
                "No data was changed"
            );
        }
    }
    

}
