package com.chandana.customer;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



//  This is the API Layer
@RestController  
@RequestMapping("api/v1/customers") // safely  remove path from getMapping, POST mapping.
public class CustomerController {

    private final CustomerService customerService;


    
  public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

   @GetMapping // Automatically path assigned because of requestmapping annotation in class.
    public List<Customer> getCustomers(){
        return customerService.getAllCustomers();
    }


    @GetMapping("{customerId}") 
    public Customer getCustomer(
        @PathVariable("customerId") Integer customerId){

         return customerService.getCustomer(customerId);
        }
   
    @PostMapping
    public void registerCustomer(
        @RequestBody CustomerRegistrationRequest request){
        customerService.addCustomer(request); // @Requestbody retrieves the json object from the post body client sent.
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(
        @PathVariable("customerId") Integer customerId){
            customerService.deleteCustomerById(customerId);
        }

        @PutMapping("{customerId}")
        public void editCustomer(
            @RequestBody CustomerEditRequest request,
            @PathVariable("customerId") Integer customerId){
            customerService.editCustomer(customerId,request); // @Requestbody retrieves the json object from the post body client sent.
        }
    
}
