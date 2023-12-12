package com.pluralsight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
   public static void main(String[] args) {
       String url = "jdbc:mysql://localhost:3306/northwind";
       String username = "root";
       String password = "~CLASSIFIED~";
       String query = "SELECT * FROM Products";

       try {
           Connection connection = DriverManager.getConnection(url, username, password);
           Statement statement = connection.createStatement();

           ResultSet resultSet = statement.executeQuery(query);

           while (resultSet.next()) {
               System.out.println(resultSet.getString(2));
           }

           //   Closers
           resultSet.close();
           statement.close();
           connection.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}