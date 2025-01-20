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
    void testDeleteCustomerById() {

    }

    @Test
    void testExistsPersonWithEmail() {

    }

    @Test
    void testExistsPersonWithId() {

    }

    @Test
    void testInsertCustomer() {

    }

    @Test
    void testSelectAllCustomers() {

        Customer customer = new Customer(
            faker.name().fullName(),
            faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID(),
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
    void testUpdateCustomer() {

    }
}
