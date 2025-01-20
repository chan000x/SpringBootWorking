package com.chandana;

import static org.assertj.core.api.Assertions.*;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
//@SpringBootTest // For unit test should not use this annotation. otherwise spin up the entire application if not configured correctly.
//Also load the application context and you know in the application context there is bunch of beans which we are not care about within tests.
//In unit testing we only care about DAO layer. so no need of bunch of beans not required.
public class TestcontainersTest {

   // @Autowired
   // private ApplicationContext applicationContext; // no need this. created because count to total beans.

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
          new PostgreSQLContainer<>("postgres:latest")
             .withDatabaseName("postgres-dao-unit-text")
             .withUsername("postgres")
             .withPassword("34221719");
    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        //assertThat(postgreSQLContainer.isHealthy()).isTrue();
    }
// this @DynamicPropertySource annotation helps to change the data setting in application.yml
    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry){
        registry.add(
            "spring.datasource.url",
            postgreSQLContainer::getJdbcUrl
        );
        registry.add(
            "spring.datasource.username",
            () -> postgreSQLContainer.getUsername()
        );
        registry.add(
            "spring.datasource.password",
            postgreSQLContainer::getPassword
        );
    }
    @Test
    void canApplyDBMigrateWithFlyway() {


        Flyway flyway = Flyway.configure().dataSource(
            postgreSQLContainer.getJdbcUrl(),
            postgreSQLContainer.getUsername(),
            postgreSQLContainer.getPassword()
        )
        .load();
        flyway.migrate();

        // System.out.println(applicationContext.getBeanDefinitionCount()); // This will display total beans created if @SpringBootTest annotation used in unit testing and it is wastage.
        // for (String name : applicationContext.getBeanDefinitionNames()) {
        //     System.out.println(name);
        // }
        
        System.out.println();
    }

}
