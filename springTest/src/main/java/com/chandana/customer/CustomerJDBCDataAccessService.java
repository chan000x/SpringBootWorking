package com.chandana.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
        // In the ApplicationContext there is a instance of the JDBCTemplete beacuse of the spring data JDBC in class path and it will instantiate a instance of the JDBCTemplete.
        // That JDBCTemplete  can inject it in other places like here.
   
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
               SELECT id,name,email,age
                FROM customer;
               """;

        //  Row mapper created using lambda expression is also possible like below.   
        // RowMapper<Customer> customerRowMapper = (resultSet, rowNum) -> {
        //     Customer customer = new Customer(
        //         resultSet.getInt("id"), // also you can pass column indexes if you likes.
        //         resultSet.getString("name"),
        //         resultSet.getString("email"),
        //         resultSet.getInt("age")
        //     );
        //     return customer;
        //    };


       List<Customer> customers = jdbcTemplate.query(sql,customerRowMapper );
        return customers;
    }



@Override
public Optional<Customer> selectCustomerById(Integer id) {
    String sql = """
            SELECT * FROM customer WHERE id = ?
            """;
       // Customer customer = jdbcTemplate.queryForObject(sql, customerRowMapper, id);

        return jdbcTemplate.query(sql,customerRowMapper,id)
            .stream()
            .findFirst();
        // return Optional.ofNullable(customer); 

}




    @Override
    public void insertCustomer(Customer customer) {
       var sql = """
               INSERT INTO customer(name,email,age)
               VALUES(?,?,?) 
               """;  
         // question mark is used because values are retriving from the customer object on each execution. This is the prepared statement.
        int result = jdbcTemplate.update(
            sql,customer.getName(), 
            customer.getEmail(),
            customer.getAge());
        
            System.out.println("jdbcTemplate.update : "+ result);
    }




    @Override
    public boolean existsPersonWithEmail(String email) {
       var sql = """
               SELECT count(id) 
               FROM customer
                WHERE email = ?
               """;
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,email);
        return count != null && count > 0;
    }



    @Override
    public boolean existsPersonWithId(Integer id) {
        var sql = """
            SELECT count(id) 
            FROM customer
             WHERE id = ?
            """;
     Integer count = jdbcTemplate.queryForObject(sql,Integer.class,id);
     return count != null && count > 0;
    }




    @Override
    public void deleteCustomerById(Integer customerId) {
      var sql = """
              DELETE 
              FROM customer 
              WHERE id = ?
              """;
        int result = jdbcTemplate.update(sql, customerId);
        System.out.println("deleteCustomerById result : "+result);
    }




    @Override
    public void updateCustomer(Customer update) {
        if(update.getName() != null){
            String sql = "UPDATE customer SET name = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                sql,
                update.getName(),
                update.getId()
            );
            System.out.println("update customer name result = "+result);
        }
        if(update.getAge() != null){
            String sql = "UPDATE customer SET age = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                sql,
                update.getAge(),
                update.getId()
            );
            System.out.println("update customer age result = "+result);
        }
        if(update.getEmail() != null){
            String sql = "UPDATE customer SET email = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                sql,
                update.getEmail(),
                update.getId()
            );
            System.out.println("update customer email result = "+result);
        }
    }

    

}
