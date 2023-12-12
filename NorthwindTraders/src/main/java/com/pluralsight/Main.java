package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class Main {
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        // Database (/Query) Information.
        Connection connection = null;
        String url = "jdbc:mysql://localhost:3306/northwind";
        String username = "root";
        String password = "~CONFIDENTIAL~";

        //  Main Menu Flow
        try {
            connection = DriverManager.getConnection(url, username, password);
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
                        option1(connection);
                        break;
                    case 2:
                        //  "Display All Customers"
                        option2(connection);
                        break;
                    case 3:
                        //  "Display All Categories"
                        option3(connection);
                        break;
                    case 0:
                        System.out.println("Exiting now... Have A Good Day!");
                        break;
                    default:
                        System.out.println("Invalid Option. Redirecting...\n");
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

    //  Display all Product Information.
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
            resultSet.close();
            System.out.println("\n\t\t\t\t-----Redirecting to Main Menu-----\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Display all Customer Information.
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
            resultSet.close();
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t------Redirecting to Main Menu-----\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //  Display All Categories.
    private static void option3(Connection connection) {
        String query = "SELECT CategoryID, CategoryName FROM Categories ORDER BY CategoryID";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("\t\t------Category Inventory-----\n");
            System.out.printf("%-20s %-40s%n", "Category ID", "Category Name");
            System.out.println("-------------------------------------------------------");

            while (resultSet.next()) {
                int id = resultSet.getInt("CategoryID");
                String name = resultSet.getString("CategoryName");

                System.out.printf("%-20d %-40s%n", id, name);
            }
            resultSet.close();

            System.out.println("\nChoose A Category (ID) To View Products Information | Enter '0' To Go Back To The Main Menu: \n");
            int inputID = scan.nextInt();

            if (inputID != 0) {
                categoryProducts(connection, inputID);
            } else {
                System.out.println("\n\t\t\t------Redirecting to Main Menu-----\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //  Display All Products From Specified Category.
    private static void categoryProducts(Connection connection, int categoryID) {
        String queryProducts = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products WHERE CategoryID = ?";
        String queryCategoryName = "SELECT CategoryName FROM Categories WHERE CategoryID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryCategoryName)) {
            preparedStatement.setInt(1, categoryID);
            ResultSet resultSet = preparedStatement.executeQuery();
            String categoryName = "";

            if (resultSet.next()) {
            categoryName = resultSet.getString("CategoryName");
            }

            System.out.println("\t\t\t\t------Product Inventory In " + categoryName + "-----\n");
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
                productSet.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("\n\t\t\t\t------Redirecting to Main Menu-----\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
