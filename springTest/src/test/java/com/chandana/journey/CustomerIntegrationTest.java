package com.chandana.journey;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.assertj.core.api.Assertions.assertThat;
import com.chandana.customer.Customer;
import com.chandana.customer.CustomerEditRequest;
import com.chandana.customer.CustomerRegistrationRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;

import reactor.core.publisher.Mono;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random random = new Random();
    private static final String CUSTOMER_URI = "/api/v1/customers";

    @Test
    void canRegisterACustomer() {
        Faker faker = new Faker();

        // Create a registration request
        String name = faker.name().fullName();
        String email = faker.internet().emailAddress();
        int age = random.nextInt(1, 100);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            name, email, age
        );

        // Send a POST request to register a customer
        webTestClient.post()
            .uri(CUSTOMER_URI)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), CustomerRegistrationRequest.class) // Attach the request
            .exchange()  // Send the request
            .expectStatus() // Expected result
            .isOk(); // Expect status to be OK

        // Get all customers
        List<Customer> allCustomers = webTestClient.get()
            .uri(CUSTOMER_URI)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(new ParameterizedTypeReference<Customer>() {})
            .returnResult()
            .getResponseBody();

        // Validate that the newly registered customer is present
        Customer expectedCustomer = new Customer(name, email, age); 


        assertThat(allCustomers)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .contains(expectedCustomer);

        int id = allCustomers.stream()
            .filter(customer -> customer.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();

        // Get customer by id
        expectedCustomer.setId(id);
        webTestClient.get()
            .uri(CUSTOMER_URI+"/{id}",id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(new ParameterizedTypeReference<Customer>() {})
            .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer(){
        Faker faker = new Faker();

        // Create a registration request
        String name = faker.name().fullName();
        String email = faker.internet().emailAddress();
        int age = random.nextInt(1, 100);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            name, email, age
        );

        // Send a POST request to register a customer
        webTestClient.post()
            .uri(CUSTOMER_URI)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), CustomerRegistrationRequest.class) // Attach the request
            .exchange()  // Send the request
            .expectStatus() // Expected result
            .isOk(); // Expect status to be OK

        // Get all customers
        List<Customer> allCustomers = webTestClient.get()
            .uri(CUSTOMER_URI)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(new ParameterizedTypeReference<Customer>() {})
            .returnResult()
            .getResponseBody();


        int id = allCustomers.stream()
            .filter(customer -> customer.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();

        // Delete Customer
        webTestClient.delete()
                .uri(CUSTOMER_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
      
         // Get customer by id

        webTestClient.get()
            .uri(CUSTOMER_URI+"/{id}",id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();

    }

    @Test
    void canUpdateCustomer() {
        Faker faker = new Faker();
    
        // Create a registration request
        String name = faker.name().fullName();
        String email = faker.internet().emailAddress();
        int age = random.nextInt(1, 100);
    
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            name, email, age
        );
    
        // Send a POST request to register a customer
        webTestClient.post()
            .uri(CUSTOMER_URI)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), CustomerRegistrationRequest.class)
            .exchange()
            .expectStatus()
            .isOk();
    
        // Get all customers
        List<Customer> allCustomers = webTestClient.get()
            .uri(CUSTOMER_URI)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(new ParameterizedTypeReference<Customer>() {})
            .returnResult()
            .getResponseBody();
    
        assertThat(allCustomers).isNotEmpty();
    
        // Find the ID of the customer
        int id = allCustomers.stream()
            .filter(customer -> customer.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();
        String uniqueEmail = "updated_" + UUID.randomUUID() + "@gmail.com";

        // Create an update request
        CustomerEditRequest updateRequest = new CustomerEditRequest(
            "Updated Name", uniqueEmail, random.nextInt(1, 100)
        );
    
        // Send a PUT request to update the customer
        webTestClient.put()
            .uri(CUSTOMER_URI + "/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(updateRequest), CustomerEditRequest.class)
            .exchange()
            .expectStatus()
            .isOk();
    
        // Validate that the customer was updated
        Customer expectedCustomer = new Customer(id, updateRequest.name(), updateRequest.email(), updateRequest.age());
    
        Customer updatedCustomer = webTestClient.get()
            .uri(CUSTOMER_URI + "/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Customer.class)
            .returnResult()
            .getResponseBody();
    
        assertThat(updatedCustomer)
            .usingRecursiveComparison()
            .isEqualTo(expectedCustomer);
    }
    
}
