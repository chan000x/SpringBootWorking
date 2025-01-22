package com.chandana.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chandana.exception.ChangeNotFoundException;
import com.chandana.exception.DuplicateResourceException;
import com.chandana.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
      // AutoCloseable autoCloseable =  MockitoAnnotations.openMocks(this); // No need to manually configure mockito test because in the class level we used the annotation @ExtendWith(MockitoExtension.class)
        underTest = new CustomerService(customerDao);

    }

    @AfterEach
    void tearDown() {
        // we do not nned this one to close resources because the anootaion @ExtendWith(MockitoExtension.class) used to the all the setup for us.
    }



    @Test
    void testGetAllCustomers() {

        underTest.getAllCustomers();

        verify(customerDao).selectAllCustomers();
    }

    @Test
    void testGetCustomer() {

        // Given
        int id = 1;
        Customer customer = new Customer(
            id,"Alex","Alex@gmail.com",21
        );
        // When customerDao which is mock invoke method on it it does not know how to reply.so we have to tell exactly what to do.in below it return customer object.
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
       
        // When
        Customer actual = underTest.getCustomer(id);

        // Then
        assertThat(actual).isEqualTo(customer);
    }
    @Test
    void testGetCustomerWhenReturnOptional() {

        // Given
        int id = 10;

         when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found ".formatted(id));
    }


    @Test
    void testAddCustomer() {

        String email = "alex@gmail.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("foo", email, 21);
        
        underTest.addCustomer(request);

        ArgumentCaptor<Customer> customeArgumentCaptor = ArgumentCaptor.forClass(
            Customer.class
            );

        verify(customerDao).insertCustomer(customeArgumentCaptor.capture());

        Customer capturedCustomer = customeArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void testAddCustomerWhenEmailExist() {

        String email = "alex@gmail.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);
        
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("foo", email, 21);
        
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken : ");


        verify(customerDao,never()).insertCustomer(any());

    }

    @Test
    void testDeleteCustomerById() {

        Integer id = 10;
        when(customerDao.existsPersonWithId(id)).thenReturn(true);

        underTest.deleteCustomerById(id);
        verify(customerDao).deleteCustomerById(id);

    }
    @Test
    void testDeleteCustomerByIdWhenNoCustomerExist() {

        Integer id = 1;
        when(customerDao.existsPersonWithId(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
        
        verify(customerDao,never()).deleteCustomerById(id);

    }

    @Test
    void testEditCustomerAllProperties() {
        // Given
        int id = 1;
        Customer existingCustomer = new Customer(
            id, "Alex", "Alex@gmail.com", 21
        );
        CustomerEditRequest request = new CustomerEditRequest("foo", "foo@gmail.com", 22);
    
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(existingCustomer));
        when(customerDao.existsPersonWithEmail(request.email())).thenReturn(false);
    
        // When
        underTest.editCustomer(id, request);
    
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
    
        Customer capturedCustomer = customerArgumentCaptor.getValue();
    
        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }
    @Test
    void testEditCustomerNameProperties() {
        // Given
        int id = 1;
        Customer existingCustomer = new Customer(
            id, "Alex", "Alex@gmail.com", 21
        );
        CustomerEditRequest request = new CustomerEditRequest("foo",null,null);
    
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(existingCustomer));
        //when(customerDao.existsPersonWithEmail(request.email())).thenReturn(false);
    
        // When
        underTest.editCustomer(id, request);
    
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
    
        Customer capturedCustomer = customerArgumentCaptor.getValue();
    
        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
    }
    @Test
    void testEditCustomerEmailProperties() {
        // Given
        int id = 1;
        Customer existingCustomer = new Customer(
            id, "Alex", "Alex@gmail.com", 21
        );
        CustomerEditRequest request = new CustomerEditRequest(null,"foo@gmail.com",null);
    
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(existingCustomer));
        when(customerDao.existsPersonWithEmail(request.email())).thenReturn(false);
    
        // When
        underTest.editCustomer(id, request);
    
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
    
        Customer capturedCustomer = customerArgumentCaptor.getValue();
    
        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
    }
    @Test
    void testEditCustomerAgeProperties() {
        // Given
        int id = 1;
        Customer existingCustomer = new Customer(
            id, "Alex", "Alex@gmail.com", 21
        );
        CustomerEditRequest request = new CustomerEditRequest(null,null,67);
    
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(existingCustomer));
       // when(customerDao.existsPersonWithEmail(request.email())).thenReturn(false);
    
        // When
        underTest.editCustomer(id, request);
    
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
    
        Customer capturedCustomer = customerArgumentCaptor.getValue();
    
        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }
    
    @Test
    void testEditCustomerWhenEmailAlreadyTaken() {

        int id = 1;
        Customer customer = new Customer(
            id, "Alex", "Alex@gmail.com", 21
        );
        CustomerEditRequest request = new CustomerEditRequest("foo", "foo@gmail.com", 22);
    
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existsPersonWithEmail(request.email())).thenReturn(true);

       // underTest.editCustomer(id, request);
        assertThatThrownBy(() -> underTest.editCustomer(id,request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");


        verify(customerDao,never()).updateCustomer(any());
    }


    @Test
    void testEditCustomerWhenNoChanges() {

        int id = 1;
        Customer customer = new Customer(
            id, "Alex", "Alex@gmail.com", 21
        );
        CustomerEditRequest request = new CustomerEditRequest(customer.getName(),customer.getEmail(), customer.getAge());
    
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> underTest.editCustomer(id,request))
                .isInstanceOf(ChangeNotFoundException.class)
                .hasMessage("No data was changed");


        verify(customerDao,never()).updateCustomer(any());
    }
}
