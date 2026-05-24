package com.photo.booking.config;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseCheck {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection connection = java.sql.DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/social_shoot?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai",
                "root",
                "Kun0802"
            );
            
            DatabaseMetaData metaData = connection.getMetaData();
            
            System.out.println("=== Checking role table ===");
            ResultSet roleTable = metaData.getTables(null, null, "role", null);
            if (roleTable.next()) {
                System.out.println("Role table exists.");
                
                ResultSet roleColumns = metaData.getColumns(null, null, "role", null);
                while (roleColumns.next()) {
                    System.out.println("Column: " + roleColumns.getString("COLUMN_NAME") + ", Type: " + roleColumns.getString("TYPE_NAME"));
                }
                
                Statement stmt = connection.createStatement();
                ResultSet roleData = stmt.executeQuery("SELECT * FROM role");
                System.out.println("Role data:");
                while (roleData.next()) {
                    System.out.println("ID: " + roleData.getInt("id") + ", Name: " + roleData.getString("name"));
                }
            } else {
                System.out.println("Role table does not exist.");
            }
            
            System.out.println("\n=== Checking user table ===");
            ResultSet userTable = metaData.getTables(null, null, "user", null);
            if (userTable.next()) {
                System.out.println("User table exists.");
                
                ResultSet userColumns = metaData.getColumns(null, null, "user", null);
                while (userColumns.next()) {
                    System.out.println("Column: " + userColumns.getString("COLUMN_NAME") + ", Type: " + userColumns.getString("TYPE_NAME"));
                }
                
                ResultSet foreignKeys = metaData.getImportedKeys(null, null, "user");
                System.out.println("\nForeign keys in user table:");
                while (foreignKeys.next()) {
                    System.out.println("FK Column: " + foreignKeys.getString("FKCOLUMN_NAME") + ", PK Table: " + foreignKeys.getString("PKTABLE_NAME") + ", PK Column: " + foreignKeys.getString("PKCOLUMN_NAME"));
                }
            } else {
                System.out.println("User table does not exist.");
            }
            
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}