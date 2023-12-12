package com.pluralsight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        // Database (/Query) Information
        String url = "jdbc:mysql://localhost:3306/northwind";
        String username = "root";
        String password = "~CONFIDENTIAL~";
        String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products";

        try {
            // Connection To Database
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();

            // Processes "Query" for product information
            ResultSet resultSet = statement.executeQuery(query);

            // Formatting Header Display
            System.out.printf("%-10s %-40s %-10s %-10s%n", "ID", "Name", "Price", "Stock");
            System.out.println("----------------------------------------------------------------");

            // Connect And Display Each Product's Information
            while (resultSet.next()) {
                int id = resultSet.getInt("ProductID");
                String name = resultSet.getString("ProductName");
                double price = resultSet.getDouble("UnitPrice");
                int stock = resultSet.getInt("UnitsInStock");

            // Formatting Product's Information Display
                System.out.printf("%-10d %-40s %-10.2f %-10d%n", id, name, price, stock);
            }

            // Closer
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}