package com.example.pos.session;

public class Session {
    private static String username;
    private static String role;

    public static void setCurrentUser(String user, String r) {
        username = user;
        role = r;
    }

    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }
}

