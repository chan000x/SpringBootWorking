package com.chandana.customer;

public record CustomerEditRequest(
    //Integer id, // Customer ID to identify the customer
    String name,
    String email,
    Integer age
) {

}
