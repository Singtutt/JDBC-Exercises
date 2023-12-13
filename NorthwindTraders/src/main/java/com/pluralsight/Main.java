package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String URL = "jdbc:mysql://localhost:3306/northwind";
    private static final String USER = "root";
    private static final String PASS = "~CONFIDENTIAL~";

    public static void main(String[] args) {
        // Database (/Query) Information |  Data Source Implementation.
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(URL);
        dataSource.setUsername(USER);
        dataSource.setPassword(PASS);

        //  Main Menu Flow
        try (Scanner scan = new Scanner(System.in)) {
            int option;

            do {
                // Menu Display.
                System.out.println("""
                        -Main Menu-
                        What would you like to do?
                        \t1. Display All Products
                        \t2. Display All Customers
                        \t3. Display All Categories
                        \t0. Exit
                        Select An Option:\s""");
                option = scan.nextInt();

                switch (option) {
                    case 1:
                        //  "Display All Products"
                        option1(dataSource);
                        break;
                    case 2:
                        //  "Display All Customers"
                        option2(dataSource);
                        break;
                    case 3:
                        //  "Display All Categories"
                        option3(dataSource, scan);
                        break;
                    case 0:
                        System.out.println("Exiting now... Have A Good Day!");
                        break;
                    default:
                        System.out.println("Invalid Option. Redirecting...\n");
                        break;
                }

            } while (option != 0);
        }
    }

    //  Display all Product Information.
    private static void option1(BasicDataSource dataSource) {
    //   SQL Query
        String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
    //  Header Display
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Display all Customer Information.
    private static void option2(BasicDataSource dataSource) {
    //   SQL Query
        String query = "SELECT ContactName, CompanyName, City, Country, Phone FROM Customers ORDER BY Country";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
    //  Header Display
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //  Display All Categories.
    private static void option3(BasicDataSource dataSource, Scanner scan) {
    //   SQL Query
        String query = "SELECT CategoryID, CategoryName FROM Categories ORDER BY CategoryID";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
    //  Header Display
            System.out.println("\t\t------Category Inventory-----\n");
            System.out.printf("%-20s %-40s%n", "Category ID", "Category Name");
            System.out.println("-------------------------------------------------------");

            while (resultSet.next()) {
                int id = resultSet.getInt("CategoryID");
                String name = resultSet.getString("CategoryName");

                System.out.printf("%-20d %-40s%n", id, name);
            }

    //  Prompt For Category ID --> For Product Information Under Specified Category <--> '0' To Go Back to Main Menu.
            System.out.println("\nChoose A Category (ID) To View Products Information | Enter '0' To Go Back To The Main Menu: \n");
            int inputID = scan.nextInt();

            if (inputID != 0) {
                categoryProducts(dataSource, inputID);
            } else {
                System.out.println("\n\t\t\t------Redirecting to Main Menu-----\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //  Display All Products From Specified Category.
    private static void categoryProducts(BasicDataSource dataSource, int categoryID) {
    //   SQL Queries
        String queryProducts = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products WHERE CategoryID = ?";
        String queryCategoryName = "SELECT CategoryName FROM Categories WHERE CategoryID = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryCategoryName)) {

            preparedStatement.setInt(1, categoryID);
            ResultSet resultSet = preparedStatement.executeQuery();
            String categoryName = "";

            if (resultSet.next()) {
                categoryName = resultSet.getString("CategoryName");
            }
    //  Header Display
            System.out.println("\t\t\t\t------Inventory For " + categoryName + "-----\n");
            System.out.printf("%-10s %-40s %-10s %-10s%n", "ID", "Name", "Price", "Stock");
            System.out.println("-----------------------------------------------------------------------------");

            try (PreparedStatement productsStatement = connection.prepareStatement(queryProducts)) {

                productsStatement.setInt(1, categoryID);
                ResultSet productSet = productsStatement.executeQuery();

                while (productSet.next()) {
                    int id = productSet.getInt("ProductID");
                    String name = productSet.getString("ProductName");
                    double price = productSet.getDouble("UnitPrice");
                    int stock = productSet.getInt("UnitsInStock");

                    System.out.printf("%-10d %-40s %-10.2f %-10d%n", id, name, price, stock);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("\n\t\t\t\t------Redirecting to Main Menu-----\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
