package ru.geekbrains.java.Auth;

import java.sql.*;

import org.sqlite.JDBC;

public class JdbcApp {
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;

    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:demobase.db");
        System.out.println("base is connect");
        statement = connection.createStatement();

    }

    public void stop() {

        try {
            if (statement != null)
                statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String readEx(String log, String pass) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE login=? and password=?");
        preparedStatement.setString(1, log);
        preparedStatement.setString(2, pass);

        ResultSet rs = preparedStatement.executeQuery();
       if (rs.next()){
           String name = rs.getString(2);
           return name;
       }else return null;

//    ResultSet rs = statement.executeQuery("SELECT * FROM Users;");
//        while (rs.next()){
//            System.out.println(rs.getString(2) + " " + rs.getString(3));
//    }
    }

}
