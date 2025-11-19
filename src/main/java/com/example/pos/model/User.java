package com.example.pos.model;

public class User {
    private int id;
    private String username;
    private String role;
    private String createdAt;

    public User(int id, String username, String role, String createdAt) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.createdAt = createdAt;
    }

    public User() {

    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getCreatedAt() { return createdAt; }
}
