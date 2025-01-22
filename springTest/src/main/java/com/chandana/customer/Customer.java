package com.chandana.customer;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "customer",
    uniqueConstraints = {
        @UniqueConstraint (
            name = "customer_eamil_unique",
            columnNames = "email"
        )
    }
)
public  class Customer{
    @Id
    @SequenceGenerator(
        name = "customer_id_seq",
        sequenceName = "customer_id_seq", // This must match with flyway migration sequence table name. Beacuse hibernate with spring data jpa does not used here to make schemas.  
        initialValue = 1,
        allocationSize = 1
     )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "customer_id_seq"
    )
    private Integer id;  // id set to primary key and it is value start from 1 to bigint.

    @Column(
        nullable = false  // this will add constrains to the when connecting to the database postgres.
    )
    private String name;

    @Column(
        nullable = false // this will add constrains to the when connecting to the database postgres.
        //unique = true // this constraint must present in sequence table in flyway. otherwise it does not work. ex : "customer_email_unique" UNIQUE CONSTRAINT
        // the constraints defined the top of the table.
   
        )
    private String email;

    @Column(
        nullable = false  // this will add constrains to the when connecting to the database postgres.
    )
    private Integer age;

    
    public Customer() {
    }

    
    public Customer(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }


    public Customer(Integer id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public Integer getAge() {
        return age;
    }
    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + ", email=" + email + ", age=" + age + "]";
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((age == null) ? 0 : age.hashCode());
        return result;
    }
    
@Override
public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null || getClass() != obj.getClass())
        return false;
    Customer other = (Customer) obj;
    return Objects.equals(id, other.id) &&
           Objects.equals(name, other.name) &&
           Objects.equals(email, other.email) &&
           Objects.equals(age, other.age);
}

    private Customer getEnclosingInstance() {
        return Customer.this;
    }

    

}
