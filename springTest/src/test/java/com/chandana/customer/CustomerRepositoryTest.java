package com.chandana.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import com.chandana.AbstractTestcontainers;

@DataJpaTest // This annotation is loading anything that needed to run our DataJpa. 
            //It means like Application Context loaded from @SpringBootTest annotation 
            //load more than 200 beans loaded but in here it is only load neccessary. 
            //Manually Configuration is Defficult so we use this annotaion.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Preventing to AutoConnect to embedded Database.
public class CustomerRepositoryTest extends AbstractTestcontainers{

    @Autowired
    private CustomerRepository underTest;

     // @Autowired do the below intialization by it self to the varialbe from AppicationContext.
     // to set up application context @SpringBootTest must defined in top of the class.
    // public CustomerRepositoryTest(CustomerRepository underTest) {
    //     this.underTest = underTest;
    // }

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();// If you want fresh database before test use this one. because in main method in each run it adds a new customer to database.
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void testExistsCustomerByEmail() {
        String email = faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20
        );

        underTest.save(customer);

        var actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isTrue();
    }
    @Test
    void testExistsCustomerByEmailFailsEmailNotPresent() {
        String email = faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID();

        var actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isFalse();
    }

    @Test
    void testExistsCustomerById() {
        String email = faker.internet().safeEmailAddress() +"-"+ UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20
        );

        underTest.save(customer);
       // Long id = customer.getId();
        Integer id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();
        var actual = underTest.existsCustomerById(id);

        assertThat(actual).isTrue();
    }
    @Test
    void testExistsCustomerByIdFailsWhnIdNotPresent() {

        Integer id = -1;
        var actual = underTest.existsCustomerById(id);

        assertThat(actual).isFalse();
    }
}
