package com.chandana.customer;

public record CustomerRegistrationRequest(
    String name,
    String email,
    Integer age
) {


}
