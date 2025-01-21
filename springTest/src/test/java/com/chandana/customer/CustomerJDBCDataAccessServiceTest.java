package com.chandana.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.chandana.AbstractTestcontainers;

public class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers{

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();
    
    @BeforeEach
    void setUp() {
        // We intialise this object here because for each test case it needs a fresh object.
        // This method execute before each method.
        // CustomerRow Mapper does not need fresh instance each time run a test case.
        underTest = new CustomerJDBCDataAccessService(
            getJdbcTemplate(),
            customerRowMapper
        );
    }


    @Test
    void testSelectAllCustomers() {

        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        // Arrange: Create a customer instance
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20
        );

        underTest.insertCustomer(customer);

        List<Customer> customers =  underTest.selectAllCustomers();
        
        assertThat(customers).isNotEmpty();
    }

    @Test
    void testSelectCustomerById() {
        String email = faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20
        );

        underTest.insertCustomer(customer);
       // Long id = customer.getId();
        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());

        });
    }

    @Test
    void willReturnWhenSelectCustomerById() {
        int id = -1;
       
        var actual = underTest.selectCustomerById(id);

        assertThat(actual).isEmpty();
    }


    @Test
    void testDeleteCustomerById() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        // Arrange: Create a customer instance
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20
        );
    
        // Save the customer to the database
        underTest.insertCustomer(customer);

        boolean exist = underTest.existsPersonWithId(1);

        underTest.deleteCustomerById(1);
        Boolean afterDelete = underTest.existsPersonWithId(1);
               // Assert the result
        assertThat(exist).isNotEqualTo(afterDelete);
    }

    @Test
    void testExistsPersonWithEmail() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        // Create a customer instance
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20
        );
    
        // Save the customer to the database
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();
        boolean exist = underTest.existsPersonWithEmail(customer.getEmail());

        assertThat(exist).isTrue();

    }

    @Test
    void testExistsPersonWithId() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        // Arrange: Create a customer instance
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20
        );
    
        // Save the customer to the database
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers()
        .stream()
        .filter(c -> c.getEmail().equals(email))
        .map(c -> c.getId())
        .findFirst()
        .orElseThrow();
        // Fetch the ID of the saved customer
        Optional<Customer> actual = underTest.selectCustomerById(id);
    

        assertThat(actual).isNotEmpty();
    }
    

    @Test
    void testInsertCustomer() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        // Arrange: Create a customer instance
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20
        );
    
        // Save the customer to the database
        underTest.insertCustomer(customer);
    
        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

         // Validate if the customer exists by ID
        boolean personExists = underTest.existsPersonWithId(id);
    
        // Assert the result
        assertThat(personExists).isTrue();
    }



    @Test
    void testUpdateCustomer() {
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        // Arrange: Create a customer instance
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20
        );
    
        // Act: Save the customer to the database
        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        Customer returnCustomer = underTest.selectCustomerById(id).get();
    
        // Update email and verify
        returnCustomer.setEmail("newEmail@gmail.com");
        underTest.updateCustomer(returnCustomer);

        Customer updatedCustomer = underTest.selectCustomerById(id).orElseThrow();
        assertThat(updatedCustomer.getEmail())
            .as("Email should be updated")
            .isEqualTo("newEmail@gmail.com");
    
        // Update age and verify
        returnCustomer.setAge(23);
        underTest.updateCustomer(returnCustomer);
        updatedCustomer = underTest.selectCustomerById(id).orElseThrow();
        assertThat(updatedCustomer.getAge())
            .as("Age should be updated")
            .isEqualTo(23);
    
        // Update name and verify
        returnCustomer.setName("newName");
        underTest.updateCustomer(returnCustomer);
        updatedCustomer = underTest.selectCustomerById(id).orElseThrow();
        assertThat(updatedCustomer.getName())
            .as("Name should be updated")
            .isEqualTo("newName");
    
        // Assert final state matches the expected customer
        assertThat(updatedCustomer)
            .as("Final customer should match the expected customer")
            .isEqualTo(returnCustomer);
    }
    
}
