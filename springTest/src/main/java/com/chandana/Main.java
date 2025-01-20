package com.chandana;

import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.chandana.customer.Customer;
import com.chandana.customer.CustomerRepository;
import com.github.javafaker.Faker;


@SpringBootApplication(scanBasePackages = "com.chandana.customer")
public class Main {  
   
    public static void main(String[] args){
            SpringApplication.run(Main.class, args);
 

    } 

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository){
        return args ->{
        Faker faker = new Faker();
        var name = faker.name();
        String firstName = name.firstName();
        String lastName = name.lastName();
        Random random = new Random();
        Customer customer = new Customer(
            firstName + " " +lastName,
            firstName.toLowerCase()+"."+lastName.toLowerCase()+"@gmail.com",
            random.nextInt(18,100)
        );
        customerRepository.save(customer);
        };
    }
}  
