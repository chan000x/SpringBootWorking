package com.chandana;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.bouncycastle.jcajce.provider.asymmetric.dsa.DSASigner.stdDSA;

import com.github.javafaker.Faker;


@Testcontainers
public abstract class AbstractTestcontainers {
    
    @BeforeAll
    static void beforeClass() {
        
    Flyway flyway = Flyway.configure().dataSource(
            postgreSQLContainer.getJdbcUrl(),
            postgreSQLContainer.getUsername(),
            postgreSQLContainer.getPassword()
        )
        .load();
        flyway.migrate();
    }


    @SuppressWarnings("resource")
    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
          new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("postgres-dao-unit-text")
                .withUsername("postgres")
                .withPassword("34221719");


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

    private static DataSource getDataSource(){
        DataSourceBuilder builder = DataSourceBuilder.create()
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword());
        return builder.build();
    }

    protected static JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(getDataSource());
    }

    protected static final Faker faker = new Faker();
    
}
