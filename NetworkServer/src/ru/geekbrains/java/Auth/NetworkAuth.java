package ru.geekbrains.java.Auth;

import java.util.List;

public class NetworkAuth implements Authentication {

    private static final List<UserData> list=List.of(
            new UserData("username1","login1", "password1"),
            new UserData("username2","login2", "password2"),
            new UserData("username3","login3", "password3"));

    public List<UserData> getList() {
        return list;
    }

    @Override
    public void start() {
        System.out.println("Аутентификачия запущена");

    }

    @Override
    public void stop() {
        System.out.println("Аутентификачия закрыта");
    }

    @Override
    public String UsernameByLoginAndPassword(String login, String password) {

        for (UserData ud : list) {

            if (ud.login.equals(login) && ud.password.equals(password))
                return ud.name;
        }
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
