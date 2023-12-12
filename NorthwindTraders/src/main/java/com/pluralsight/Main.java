package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Database (/Query) Information
        Connection connection = null;
        String url = "jdbc:mysql://localhost:3306/northwind";
        String username = "root";
        String password = "~CONFIDENTIAL~";

        try (Scanner scan = new Scanner(System.in)){
            connection = DriverManager.getConnection(url, username, password);
            int option;

            do {
                // Menu Display
                System.out.println("""
                        -Main Menu-
                        What would you like to do?
                        \t1. Display All Products
                        \t2. Display All Customers
                        \t0. Exit
                        Select An Option:\s""");
                option = scan.nextInt();

                switch (option) {
                    case 1:
                        option1(connection);
                        break;
                    case 2:
                        option2(connection);
                        break;
                    case 0:
                        System.out.println("Exiting now...");
                        break;
                    default:
                        System.out.println("Invalid Option. Redirecting...");
                        break;
                }
            } while (option != 0);

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //  Display all Products
    private static void option1(Connection connection) {
        String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("\t\t\t\t\t-----Product Inventory-----\n");
            System.out.printf("%-10s %-40s %-10s %-10s%n", "ID", "Name", "Price", "Stock");
            System.out.println("--------------------------------------------------------------------------");

            while (resultSet.next()) {
                int id = resultSet.getInt("ProductID");
                String name = resultSet.getString("ProductName");
                double price = resultSet.getDouble("UnitPrice");
                int stock = resultSet.getInt("UnitsInStock");

                System.out.printf("%-10d %-40s %-10.2f %-10d%n", id, name, price, stock);
            }
            System.out.println("\n\t\t\t\t-----Redirecting to Main Menu-----\n");
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 
    private static void option2(Connection connection) {
        String query = "SELECT ContactName, CompanyName, City, Country, Phone FROM Customers ORDER BY Country";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("\t\t\t\t\t\t\t\t\t\t\t------Customer Inventory-----\n");
            System.out.printf("%-25s %-37s %-20s %-20s %-15s%n", "Contact Name", "Company Name", "City", "Country", "Phone");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------");

            while (resultSet.next()) {
                String nameContact = resultSet.getString("ContactName");
                String nameCompany = resultSet.getString("CompanyName");
                String city = resultSet.getString("City");
                String country = resultSet.getString("Country");
                String phone = resultSet.getString("Phone");

                System.out.printf("%-25s %-37s %-20s %-15s %-15s%n", nameContact, nameCompany, city, country, phone);
            }
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t------Redirecting to Main Menu-----\n");
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
