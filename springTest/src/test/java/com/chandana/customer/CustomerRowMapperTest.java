package com.chandana.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

public class CustomerRowMapperTest {


    @Test
    void testMapRow() throws SQLException {

        CustomerRowMapper customerRowMapper = new CustomerRowMapper();

        ResultSet resultSet =  mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("age")).thenReturn(17);
        when(resultSet.getString("name")).thenReturn("Jamila");
        when(resultSet.getString("email")).thenReturn("jamila@gmail.com");
        

        Customer actual =  customerRowMapper.mapRow(resultSet, 1);

        // Then
        Customer expected = new Customer(
            1,"Jamila","jamila@gmail.com",17
        );


       // assertth
        assertThat(actual).isEqualTo(expected);
    }
}
