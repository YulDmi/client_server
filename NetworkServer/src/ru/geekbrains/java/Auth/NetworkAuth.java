package ru.geekbrains.java.Auth;

import java.sql.SQLException;
import java.util.List;

public class NetworkAuth implements Authentication {
private JdbcApp jdbc;
//    private static final List<UserData> users = List.of(
//            new UserData("username1", "log1", "pass1"),
//            new UserData("username2", "log2", "pass2"),
//            new UserData("username3", "log3", "pass3"));


    @Override
    public void start() {
        jdbc = new JdbcApp();
        try {
            jdbc.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Аутентификачия запущена");

    }

    @Override
    public void stop(){
        jdbc.stop();
        System.out.println("Аутентификачия закрыта");
    }

    @Override
    public String UsernameByLoginAndPassword(String login, String password) {
        try {
         String name =   jdbc.readEx(login, password);
         return name;
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        for (UserData ud : users) {
//            if (ud.login.equals(login) && ud.password.equals(password))
//                return ud.name;
//        }
         return null;

    }

    private static class UserData {
        private String name;
        private String login;
        private String password;

        public UserData(String name, String login, String password) {
            this.name = name;
            this.login = login;
            this.password = password;

        }

    }
}
