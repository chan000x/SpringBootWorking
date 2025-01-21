package com.chandana.customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CustomerRepository 
        extends JpaRepository<Customer,Integer> {
        // These queries are already created by JPARepository and we can use them by just writing the name of query like below.
        boolean existsCustomerByEmail(String email); // created our method to check data availabe in the repository by using passing value by us.
        boolean existsCustomerById(Integer id);


        // To write our own queries insteadd of already there
        // 1. JPQL Query
        @Query("SELECT c FROM Customer c WHERE c.email = :email") // This is writing our own queries in JPA 
        Customer findCustomerByEmail(@Param("email") String email);
        //  :email is a named parameter bound to the method argument email.
        //   The method will return a Customer entity matching the email.

        // 2. Native SQL Query
        @Query(value = "SELECT * FROM customer WHERE email = :email", nativeQuery = true)
        Customer findCustomerByEmailNative(@Param("email") String email);


        @Query("SELECT c FROM Customer c WHERE c.age > :age")
        List<Customer> findCustomersOlderThan(@Param("age") int age);

        


}
