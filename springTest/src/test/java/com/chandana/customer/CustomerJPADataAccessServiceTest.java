package com.chandana.customer;

import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.github.javafaker.Faker;

public class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private Faker faker;

    @Mock  private CustomerRepository customerRepository;// This annotaion used to identify these test previously well tested in this one used in outer test scope to test.
    // mocking refers to the process of creating mock objects
    // that simulate the behavior of real objects without requiring
    // a full database setup or actual implementation. 
    //This is particularly useful in unit tests where you 
    //want to isolate the logic of the code under test and 
    //avoid dependencies on external systems like databases.

    private AutoCloseable autoCloseable;
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this); // Initialize the mock itself. This one is return an Instance of Autocloseable. It is used to close the resourse after each use.
        underTest = new CustomerJPADataAccessService(customerRepository);

        faker = new Faker();
    }

    
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close(); // After each test we hace fresh mock.
    }


    @Test
    void testDeleteCustomerById() {

        int id = 1;

        underTest.deleteCustomerById(id);

        verify(customerRepository).deleteById(id);
    }

    @Test
    void testExistsPersonWithEmail() {

        var email = "foo@gmial.com";

        underTest.existsPersonWithEmail(email);

        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void testExistsPersonWithId() {

        var id = 1;

        underTest.existsPersonWithId(id);

        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void testInsertCustomer() {

        Customer customer = new Customer(
            "chanadana",
            "chandna@gmail.com",
            24
        );

        underTest.insertCustomer(customer);

        verify(customerRepository).save(customer);
    }

    @Test
    void testSelectAllCustomers() {

        // When
        underTest.selectAllCustomers();

        // We want to ensure that selectAll method is envoke or not using Mockito object.
        // Then
        verify(customerRepository)
            .findAll();

    }

    @Test
    void testSelectCustomerById() {

        // Given
        int id = 1;

        // When
        underTest.selectCustomerById(id);

        // Then
        verify(customerRepository).findById(id);
    }

    @Test
    void testUpdateCustomer() {

        String email = faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20
        );

        underTest.updateCustomer(customer);

        verify(customerRepository).save(customer);
    }
}
