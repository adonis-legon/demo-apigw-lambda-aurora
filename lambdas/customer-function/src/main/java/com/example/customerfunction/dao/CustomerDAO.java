package com.example.customerfunction.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.customerfunction.model.Customer;

public class CustomerDAO {

    private DbManager dbManager;

    public CustomerDAO() {
        this(new DbManager());
    }

    public CustomerDAO(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    public void save(Customer customer) throws SQLException {
        this.dbManager.refreshDbConnection();

        String insertQuery = "insert into customer (name, email) values(?, ?)";
        PreparedStatement preparedStatement = this.dbManager.getDbConnection().prepareStatement(insertQuery,
                Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, customer.getName());
        preparedStatement.setString(2, customer.getEmail());
        preparedStatement.execute();

        ResultSet rs = preparedStatement.getGeneratedKeys();
        if (rs.next()) {
            customer.setId(rs.getInt(1));
        }

        preparedStatement.close();
    }

    public Customer findById(int customerId) throws SQLException {
        this.dbManager.refreshDbConnection();

        String selectQuery = "select * from customer where id = ?";
        PreparedStatement preparedStatement = this.dbManager.getDbConnection().prepareStatement(selectQuery);
        preparedStatement.setInt(1, customerId);

        Customer customer = null;
        ResultSet results = preparedStatement.executeQuery();
        if (results.next()) {
            customer = new Customer(customerId, results.getString("name"), results.getString("email"));
        }
    
        return customer;
    }
}
