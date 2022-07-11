package com.example.dao;

import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.example.model.Customer;

@Configuration
public class CustomerDAO {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void init() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void save(Customer customer) throws SQLException {
        SqlParameterSource insertStatementParams = new MapSqlParameterSource()
                .addValue("name", customer.getName())
                .addValue("email", customer.getEmail());

        int customerId = (int) new SimpleJdbcInsert(jdbcTemplate).withTableName("customer")
                .usingGeneratedKeyColumns("id").executeAndReturnKey(insertStatementParams);
        customer.setId(customerId);
    }

    public Customer findById(int customerId) throws SQLException {
        String selectQuery = "select * from customer where id = ?";

        return jdbcTemplate.query(selectQuery, resultSet -> {
            Customer customer = null;
            if(resultSet.next()){
                customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setName(resultSet.getString("name"));
                customer.setEmail(resultSet.getString("email"));
            }

            return customer;
         }, customerId);
    }
}
